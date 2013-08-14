/*
 * javartm: a Java library for Restricted Transactional Memory
 * Copyright (C) 2013 Ivo Anjo <ivo.anjo@ist.utl.pt>
 *
 * This file is part of javartm.
 *
 * javartm is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * javartm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with javartm.  If not, see <http://www.gnu.org/licenses/>.
 */

package javartm;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class TestRtmSupport {
	private static final Logger Log = LoggerFactory.getLogger(TestRtmSupport.class);

	static {
		// Bind native methods
		Transaction.loadNativeLibrary("testrtmsupport");
	}

	private TestRtmSupport() { }
	protected native static boolean rtmAvailable();
}

public final class Transaction {
	private static final Logger Log = LoggerFactory.getLogger(Transaction.class);

	public static final int STARTED		= -1;

	public static final int ABORT_EXPLICIT 	= 1 << 0;
	public static final int ABORT_RETRY 	= 1 << 1;
	public static final int ABORT_CONFLICT 	= 1 << 2;
	public static final int ABORT_CAPACITY 	= 1 << 3;
	public static final int ABORT_DEBUG 	= 1 << 4;
	public static final int ABORT_NESTED 	= 1 << 5;

	public static final boolean RTM_AVAILABLE = TestRtmSupport.rtmAvailable();

	static {
		if (!RTM_AVAILABLE) {
			Log.info("RTM not supported by current CPU. Loading dummy library.");
			loadNativeLibrary("javartm-dummy");
		} else {
			loadNativeLibrary("javartm");
			warmup();
		}
	}

	protected static void loadNativeLibrary(String libraryName) {
		// Attempt to load native library from jar
		InputStream libFile = Transaction.class.getResourceAsStream("lib" + libraryName + ".so");
		if (libFile != null) {
			try {
				// Native libraries *have* to be loaded from a file, so we
				// create a temporary file, dump the native library from the
				// jar, and then load it from there
				File f = File.createTempFile("lib" + libraryName, "so");
				f.deleteOnExit();
				FileOutputStream fos = new FileOutputStream(f);
				int read;
				byte[] buffer = new byte[4096];
				while ((read = libFile.read(buffer, 0, buffer.length)) != -1) {
					fos.write(buffer, 0, read);
				}
				fos.close();
				Runtime.getRuntime().load(f.getCanonicalPath());
			} catch (IOException e) {
				Log.warn("Exception trying to load native library", e);
			}
		} else {
			// Embedded library not found, trying to load directly
			System.loadLibrary(libraryName);
		}
	}

	private static void warmup() {
		// Warmup methods
		// This is important for two reasons:
		// 1) Hotspot uses lazy dynamic linking, and otherwise we could be triggering dynamic
		//    linking during a transaction. This can be seen by using -verbose:jni to enable
		//    logging of dynamic linking operations.
		// 2) By default, hotspot JIT-recompiles code at around 10k iterations, and trying
		//    to JIT during a transaction can cause it to keep aborting. The flag
		//    -XX:+PrintCompilation may be used to verify which methods are being recompiled.
		Log.trace("Warming up methods");

		final AtomicRunnable<Void> dummyRunnable =
			new AtomicRunnable<Void>() { @Override public Void run() { return null; } };

		Warmup.doWarmup(new Runnable() { public void run() {
			inTransaction();
			begin();
			// the abort on the next line makes sure no transaction stays active after the begin
			// above, even if the transactional buffer has a bigger capacity than usual
			try { abort();  throw new Error("Should never happen"); }
				catch (IllegalStateException expected) { }
			try { abort(0); throw new Error("Should never happen"); }
				catch (IllegalStateException expected) { }
			try { commit(); throw new Error("Should never happen"); }
				catch (IllegalStateException expected) { }
			doTransactionally(dummyRunnable, true);
		}});

		Warmup.doWarmup(new Runnable() { public void run() {
			doTransactionally(dummyRunnable);
		}});

		Log.info("initialization complete");
	}

	private Transaction() { }

	public native static boolean inTransaction();
	public native static int begin();
	public native static void commit();
	public native static void abort();

	/**
	 * Abort and set returned status code.
	 * Note that reason MUST FIT as unsigned 8bits [0,255], otherwise it will be set to 0.
	 **/
	public native static void abort(long reason);

	public static <V> V doTransactionally(AtomicRunnable<V> r) {
		return doTransactionally(r, false);
	}

	private native static <V> V doTransactionally(AtomicRunnable<V> r, boolean warmup);

	public static short getAbortReason(int txStatus) {
		return (short) (txStatus >>> 24);
	}
}
