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

package javartm.tests.rtm;

import javartm.AtomicRunnable;
import javartm.Transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;

@RunWith(JUnit4.class)
public class TransactionTest1 {

	@Test(expected=IllegalStateException.class)
	public void testCommitWithoutActiveTransaction() {
		Transaction.commit();
	}

	@Test(expected=IllegalStateException.class)
	public void testAbortWithoutActiveTransaction() {
		Transaction.abort();
	}

	@Test(expected=IllegalStateException.class)
	public void testAbort2WithoutActiveTransaction() {
		Transaction.abort(0);
	}

	@Test
	public void testInTransaction() {
		boolean inTransaction = false;
		if (Transaction.begin() == Transaction.STARTED) {
			inTransaction = Transaction.inTransaction();
			Transaction.commit();
			Assert.assertTrue(inTransaction);
		} else {
			Assert.assertFalse(inTransaction);
		}
		Assert.assertFalse(Transaction.inTransaction());
	}

	@Test
	public void testDoTransactionally() {
		AtomicRunnable<Boolean> transaction = new AtomicRunnable<Boolean>() {
			@Override
			public Boolean run() {
				return true;
			}
		};
		transaction.warmup();
		Boolean result = Transaction.doTransactionally(transaction);
		Assert.assertTrue(result);
	}

	@Test
	public void testExplicitAbort() {
		int status = Transaction.begin();
		if (status == Transaction.STARTED) {
			Transaction.abort();
		} else {
			if (status == 0) {
				testExplicitAbort();
				return;
			}
			Assert.assertEquals(status, 1);
		}
	}

	@Test
	public void testExplicitAbort2() {
		for (int i = 0; i <= 255; i++) {
			int txStatus = Transaction.begin();
			if (txStatus == Transaction.STARTED) {
				Transaction.abort(i);
			} else {
				if ((txStatus & 1) == 0) {
					// Something went wrong, retry
					i--;
					continue;
				}
				Assert.assertEquals(Transaction.getAbortReason(txStatus), i);
			}
		}
	}

}
