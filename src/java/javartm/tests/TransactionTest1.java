package javartm.tests;

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
		}
		Assert.assertTrue(inTransaction);
	}

}
