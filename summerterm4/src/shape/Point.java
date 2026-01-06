package shape;

public class Point {
//members
	private double x,y;
	//methods
	private Point() {
		this.x=0;
		this.y=0;
	}
	public Point(double x,double y) {
		this();
		this.setX(x);
		this.setY(y);
	}
	public void setX(double x) {
		this.x=(x>=0 && x<=100)?x:0;
	}
	public void setY(double y) {
		this.y=(y>=0 && y<=100)?y:0;
	}
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public double distanceFromOrigin() {
		return Math.pow(this.x*this.x+this.y*this.y, 0.5);
	}
	public double distance(Point p)
	{
		double xDiff = this.x-p.getX();
		double yDiff = this.y-p.getY();
		return Math.pow( xDiff*xDiff+yDiff*yDiff,0.5);
	}
	public void printPoint() {
		System.out.printf("(X:%.lf, Y:%.1f)%n",this.x,this.y);
	}
}
