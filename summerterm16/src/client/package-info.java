package client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Vector;
import.java.util.HashSet;

public class MainApp {
	
	public static void main(String[] args) {
		ArrayList<String> a1= new ArrayList();
		a1.add("Krishna");
		a1.add("Jaya");
		Iterator<String> i1 = a1.iterator();
		
		while(i1.hasNext()) {
			String s = i1.next();
			System.out.println(s);
		}
		
		LinkedList<String> l1 = new LinkedList();
		l1.add("Ajith");
		l1.add("Shyam");
        Iterator<String> i2 = l1.iterator();
		
		while(i2.hasNext()) {
			String s = i2.next();
			System.out.println(s);
		}
		
		Vector<Integer> v1 = new Vector();
		v1.add(22);
		v1.add(99);
        Iterator<Integer> i3 = v1.iterator();
		System.out.println(v1.size());
		
		while(i3.hasNext()) {
			Integer i = i3.next();
			if(i%2!=0)
				i3.remove();
			System.out.println(v1.size());
		}
		
		Iterator<Integer> i4 = v1.iterator();
		while(i3.hasNext()) {
			System.out.println(i4.next());
		}
		
		
		Stack<Double> s1 = new Stack();
		s1.add(10.2);
		s1.add(9.9);
	}

}