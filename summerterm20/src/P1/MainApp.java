package P1;


public class MainApp {
		public static void main(String[] args) {
			Thread t1=new Thread(new printNum());
			Thread t2=new Thread(new Alpha());
			
			t1.start();
			t2.start();
		}
	}

