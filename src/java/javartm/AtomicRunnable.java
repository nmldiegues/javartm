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
 * An AtomicRunnable contains a block of code which will be executed transactionally, and methods that
 * are before/after the warmup process.
 */
public abstract class AtomicRunnable<V> {
	protected void beforeWarmup() { }
	protected void afterWarmup() { }
	protected void beforeWarmupIteration() { }
	protected void afterWarmupIteration() { }

	public abstract V run();

	public final void warmup() {
		Warmup.doWarmup(this);
		// In some cases, it seems calling doWarmup once is not enough...
		Warmup.doWarmup(this);
	}
}
