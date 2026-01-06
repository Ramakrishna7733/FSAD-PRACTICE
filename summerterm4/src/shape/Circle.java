package shape;

public class Circle {
//members
		private double rad;
		private Point cc;
		//methods 
		private Circle() {
			this.rad=0;
			this.cc= new Point(0,0);
		}
		public Circle (double r, double x,double y) {
			this();
			this.setRadius(r);
			this.setCentre(new Point(x,y));
		} 
		public void setRadius(double r) {
			this.rad=(r>=0&& r<=10)?r:0;
					
		}
		public void setCentre(Point p) {
			this.cc=p;
		}
		public double getRadius() {
			return this.rad;
		}
		public Point getCentreOfCircle() {
			return this.cc;
		}

	    public double distanceCircle(Circle c) {
	    	double sr = this.rad+c.getRadius();
	    	double dr = Math.abs(this.rad-c.getRadius());
	    	double dc = this.cc.distance(c.getCentreOfCircle());
	    	if(dc>sr) return dc-sr;
	    	if(dc<dr) return dc-dr;
	    	return 0;
	    
	    }
	    public void printcircle() {
	    	System.out.printf("Radius : %.1f%n", this.rad);
	    	System.out.print("Centre of Circle :");
	    	this.cc.printPoint();
	    }
		
	
}
