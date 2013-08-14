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

package javartm.tests.nortm;

import javartm.Transaction;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;

/** Tests for the dummy fallback library, on machines that have no RTM support **/
@RunWith(JUnit4.class)
public class DummyTest1 {

	@BeforeClass
	public static void checkRtmStatus() {
		Assert.assertFalse(DummyTest1.class.getName() + " can only be run on machines without RTM support",
			Transaction.RTM_AVAILABLE);
	}

	@Test(expected=RuntimeException.class) public void testInTransaction() { Transaction.inTransaction(); }
	@Test(expected=RuntimeException.class) public void testBegin() { Transaction.begin(); }
	@Test(expected=RuntimeException.class) public void testCommit() { Transaction.commit(); }
	@Test(expected=RuntimeException.class) public void testAbort() { Transaction.abort(); }
	@Test(expected=RuntimeException.class) public void testAbort2() { Transaction.abort(0); }
	@Test(expected=RuntimeException.class) public void testDoTransactionally() { Transaction.doTransactionally(null); }

}
