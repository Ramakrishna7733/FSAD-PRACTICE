package client;

public class consumer {
		private final Buffer b;
		
		public consumer(Buffer b) {
			this.b = b;
		}
		
		public void run() {
			try {
			while(true) {
				b.consume();
				Thread.sleep(1500);
			}
			}
			catch(InterruptedException ie){
				System.out.println("Thread interrupted");
			}
		}
	}
