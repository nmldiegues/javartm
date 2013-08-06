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
