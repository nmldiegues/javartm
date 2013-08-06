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
