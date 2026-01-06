package client;

public class mainApp {
	
		public static void main(String[] args) {
			
			Buffer b = new Buffer(3);
			
			Thread pt =new Thread(new producer(b));
			Thread ct = new Thread(new consumer(b));
			
			pt.start();
			ct.start();
		}
	}
