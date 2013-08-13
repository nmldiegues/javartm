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

// Due to issues with JIT, this test needs to be run with
// java -XX:+UnlockDiagnosticVMOptions -XX:CompileCommand=exclude,javartm/Test5,simpleTx javartm.Test5

public final class Test5 {
	public static long x, y, z;

	public static void simpleTx(int iter) {
		int status = Transaction.begin();
		if (status == Transaction.STARTED) {
			x++;
			y++;
			z++;
			Transaction.commit();
		}
		// Register stats
		TransactionStats.registerResult(status);
	}

	public static void main(String[] args) throws Throwable {
		final int ITERS = 5000000;

		Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];

		synchronized (Test5.class) {
			for (int i = 0; i < threads.length; i++) {
				System.out.println("Creating thread " + i);
				final int myId = i;
				threads[i] = new Thread() {
					@Override public void run() {
						// Poor man's barrier
						synchronized (Test5.class) { new Object(); }

						for (int i = 0; i < ITERS; i++) simpleTx(i);

						System.out.println("Thread " + myId + " done " + ITERS);
					}
				};
				threads[i].start();
			}
		}

		for (Thread t : threads) t.join();

		if (x != y || y != z) throw new Error("Final values differ");

		int total = ITERS * threads.length;

		System.out.println("Final stats: " + x + " successful iters, " + total + " tried (" +
					(x/((double) total) * 100) + "% commit rate)");
		System.out.println(TransactionStats.getStats());
	}
}
