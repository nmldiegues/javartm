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

public final class Test2 {
	public static int x, y, z;

	public static void simpleTx() {
//		dummy();
//		dummy();
//		dummy();
		if (Transaction.begin() == Transaction.STARTED) {
			x = 2;
			y = 2;
			z = 2;
//			dummy();
			Transaction.commit();
		}
	}

//	public static void dummy() { }

	public static void main(String[] args) {
		long iters = 0;
		while (x == 0) {
			iters++;
			simpleTx();
//			if (iters == 10) return;
			if (iters % 100000000 == 0) System.out.println("Trying... " + iters);
		}
		//System.out.println("Transaction succeeded  " + iters + " iters (" + x + ", " + y + ", " + z + ")");
		System.out.println("Transaction succeeded (x: " + x + ", y: " + y + ", z: " + z + ")");
	}
}
