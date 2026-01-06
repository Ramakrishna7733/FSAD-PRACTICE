package client;
package package1;
import java.util.Queue;
import java.util.LinkedList;
	
public class buffer {
	private final Queue<Integer> b;
	private final int maxSize;
	
	public Buffer(int mSize) {
		b = new LinkedList<Integer>();
		maxSize = mSize;
	}
	
	public synchronized void production(int e) throws InterruptedException{
		while(b.size()==maxSize) {
			System.out.println("Buffer is full");
			wait();
		}
		
		b.add(e);
		System.out.println("produced : "+e);
		notify();
	}
	public synchronized int consume() throws InterruptedException{
		while(b.size()==0) {
			System.out.println("Buffer is empty");
		}
		int e = b.remove();
		System.out.println("Consumed :"+e);
		notify();
		return e;
}
