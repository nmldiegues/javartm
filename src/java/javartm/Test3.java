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

public final class Test3 {
	public static int x, y;

	public static void main(String[] args) throws Throwable {
		final AtomicRunnable<Void> transaction = new AtomicRunnable<Void>() {
			@Override
			public Void run() {
				if (!Transaction.inTransaction()) return null;
				x++;
				y++;
				return null;
			}
		};

		transaction.warmup();

		x = 0;
		y = 0;

		System.out.println("Starting test...");

		long iters = 0;
		while (x == 0) {
			iters++;
			Transaction.doTransactionally(transaction);
			if (iters == 1000000) {
				System.out.println("Given up after " + iters + " iterations");
				return;
			}
		}
		System.out.println("Transaction succeeded after " + iters + " iters (" + x + ", " + y + ")");
	}
}
