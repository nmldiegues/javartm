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

public class Test7 {

	private static int[] array;

	public static void main(String[] args) {
		testWrite();
		testRead();
		testWrite();
		testRead();
	}

	private static void testWrite() {
		System.out.println("testWrite");

		AtomicRunnable<Boolean> transaction = new AtomicRunnable<Boolean>() {
			@Override
			public Boolean run() {
				for (int i = 0; i < array.length; i++) {
					array[i] = array.length;
				}
				return true;
			}
		};

		array = new int[100];
		transaction.warmup();

		for (int size : new int[] { 100, 500, 1000, 2500, 5000, 10000, 20000, 50000 }) {
			array = new int[size];
			int tries = 0;
			Boolean res = null;
			for (tries = 0; tries < 100; tries++) {
				res = Transaction.doTransactionally(transaction);
				if (res != null) break;
			}
			System.out.println("Res for size " + size + " is " + res + " (" + tries + ")");
		}
	}

	private static void testRead() {
		System.out.println("testRead");

		AtomicRunnable<Integer> transaction = new AtomicRunnable<Integer>() {
			@Override
			public Integer run() {
				int j = 0;
				for (int i = 0; i < array.length; i++) {
					j += array[i];
				}
				return j;
			}
		};

		array = new int[100];
		transaction.warmup();

		for (int size : new int[] { 100, 500, 1000, 2500, 5000, 10000, 20000, 50000 }) {
			array = new int[size];
			array[50] = 42;
			Integer res = null;
			int tries;
			for (tries = 0; tries < 100; tries++) {
				res = Transaction.doTransactionally(transaction);
				if (res != null) break;
			}
			System.out.println("Res for size " + size + " is " + res + " (" + tries + ")");
		}
	}
}
