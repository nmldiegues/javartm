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

/**
 * Simple class to help track (approximate) transaction stats.
 * To use it, just call registerResult(status) at the finish of each transaction.
 **/
public final class TransactionStats {
	private static final Object[][] TX_RETURN_CODES = {
		{ Transaction.STARTED, "STARTED/COMMITTED" },
		{ 0, "ABORT OTHER" },
		{ Transaction.ABORT_CAPACITY, "ABORT CAPACITY" },
		{ Transaction.ABORT_CONFLICT, "ABORT CONFLICT" },
		{ Transaction.ABORT_RETRY, "ABORT RETRY" },
		{ Transaction.ABORT_RETRY | Transaction.ABORT_CONFLICT, "ABORT RETRY|CONFLICT" },
		{ Transaction.ABORT_RETRY | Transaction.ABORT_CONFLICT | Transaction.ABORT_CAPACITY, "ABORT RETRY|CONFLICT|CAPACITY" }};

	private static final int[] STATS_COUNT = new int[TX_RETURN_CODES.length];

	private TransactionStats() { }

	public static void registerResult(int status) {
		for (int i = 0; i < TX_RETURN_CODES.length; i++) {
			if (status == (Integer) TX_RETURN_CODES[i][0]) {
				STATS_COUNT[i]++;
				return;
			}
		}
		throw new AssertionError("Unexpected status: " + status);
	}

	public static String getStats() {
		StringBuilder out = new StringBuilder();
		out.append('{');
		for (int i = 0; i < TX_RETURN_CODES.length; i++) {
			if (STATS_COUNT[i] > 0) {
				out.append(TX_RETURN_CODES[i][1] + ": " + STATS_COUNT[i] + ", ");
			}
		}
		if (out.length() > 1) out.setLength(out.length() - 2);
		out.append('}');
		return out.toString();
	}
}
