package client;

public class producer implements Runnable {
		private final Buffer b;
		
		public producer(Buffer b) {
			this.b = b;
		}
		public void run() {
			int value = 0;
			try {
			while(true) {
				b.production(value++);
				Thread.sleep(1000);
			}
			}
			catch(InterruptedException ie) {
				System.out.println("Thread intrrupted");
			}
		}
	}

