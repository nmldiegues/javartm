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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Warmup {
	private static final Logger Log = LoggerFactory.getLogger(Warmup.class);

	// Default JIT compiler threshold for current hotspot versions
	// See also http://tinyurl.com/lgeabt3
	private static final int HOTSPOT_JIT_THRESHOLD = 10000;

	// In some cases, the value above is not enough, so let's arbitrarily do a bit more
	private static final int ITERATIONS = HOTSPOT_JIT_THRESHOLD * 3;

	/** Warms up the received runnable by calling it repeatedly until the VM JIT kicks in **/
	public static void doWarmup(Runnable r) {
		for (int i = 0; i < ITERATIONS; i++) r.run();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) { throw new RuntimeException(e); }
		Log.info("Warmup for " + r.getClass().getName() + " complete");
	}

	/** Warms up the received runnable by calling it repeatedly until the VM JIT kicks in **/
	public static void doWarmup(AtomicRunnable<?> r) {
		r.beforeWarmup();
		for (int i = 0; i < ITERATIONS; i++) {
			r.beforeWarmupIteration();
			r.run();
			r.afterWarmupIteration();
		}
		r.afterWarmup();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) { throw new RuntimeException(e); }
		Log.info("Warmup for " + r.getClass().getName() + " complete");
	}
}
