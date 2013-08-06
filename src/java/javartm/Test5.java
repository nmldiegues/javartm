package javartm;

public final class Test5 {
	public static long x, y, z;

	public static void simpleTx() {
		if (Transaction.begin() == Transaction.STARTED) {
			x++;
			y++;
			z++;
			Transaction.commit();
		}
		if (x > 9500 && x < 9505) {
			System.out.println("Temp stats: " + "x: " + x + ", y: " + y + ", z: " + z);
		}
	}

	public static void main(String[] args) throws Throwable {
		//Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];
		Thread[] threads = new Thread[1];

		synchronized (Test5.class) {
			for (int i = 0; i < threads.length; i++) {
				System.out.println("Creating thread " + i);
				final int myId = i;
				threads[i] = new Thread() {
					@Override
					public void run() {
						// Barreira dos pobres
						synchronized (Test5.class) { new Object(); }

						int i;
						for (i = 0; i < 1000000; i++) {
							simpleTx();
						}

						System.out.println("Thread " + myId + " done " + i);
					}
				};
				threads[i].start();
			}
		}

		for (Thread t : threads) t.join();

		System.out.println("Final stats: " + "x: " + x + ", y: " + y + ", z: " + z);
	}
}
