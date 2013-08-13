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

import java.util.Arrays;

public final class Test4 {
	public static int[] myarr = new int[100];

	public static void simpleTx() {
		if (Transaction.begin() < 0) {
			for (int i = 0; i < 100; i++) {
				myarr[i] = 2;
			}
			Transaction.commit();
		}
	}

	public static void main(String[] args) {
		long iters = 0;
		while (myarr[0] == 0) {
			iters++;
			simpleTx();
			if (iters % 100000000 == 0) System.out.println("Trying... " + iters);
		}
		System.out.println("Transaction succeeded after " + iters + " iters (" + Arrays.toString(myarr) + ")");
	}
}
