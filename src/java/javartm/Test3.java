/*package javartm;

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
