package collection;
import shape.Circle;
public class CircleCollection {
//members 
	private int count;
	private Circle[] cCol;
	private static final int MAX_SIZE =5;// a final member cannot be initialize twice
	
	public CircleCollection() {
		this.count =0;
		cCol =new Circle[MAX_SIZE];
	}
	public boolean addCircle(Circle c) {
		if(c.getRadius()<=0 || this.count>=MAX_SIZE) return false;
		this.cCol[this.count++]=c;
		return true;
    }
	
	public Circle getShortest(Circle c) {
		if(this.count == 0) return null;
		Circle tempCircle = this.cCol[0];
		double sd= c.distanceCircle(tempCircle);
		for(int i=0; i<this.count++;i++) {
			double nsd = c.distanceCircle(this.cCol[i]);
			if(nsd<sd) {
				tempCircle= this.cCol[i];
				sd = nsd;
			}
		}
		System.out.println("This shortest distance ="+sd);
		return tempCircle;
	}
} 