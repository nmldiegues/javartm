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
import java.util.concurrent.Callable;

public final class Test3 {
	public static int x, y;

	public static void main(String[] args) {
		Callable<Boolean> simpletx = new Callable<Boolean>() {
			public Boolean call() {
				Test3.x = 1;
				Test3.y = 1;
				return true;
			}
		};
		Callable<Boolean> fallback = new Callable<Boolean>() {
			public Boolean call() { return false; }
		};


		long iters = 0;
		while (x == 0) {
			iters++;
			Transaction.doTransactionally(simpletx, fallback);
			if (iters % 100 == 0) System.out.println("Trying... " + iters);
		}
		System.out.println("Transaction succeeded after " + iters + " iters (" + x + ", " + y + ")");
	}
}*/
