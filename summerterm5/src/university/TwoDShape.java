package university;

public abstract class TwoDShape {
//members
	protected double d1;
	//methods
	public void setD1(double d1)
	{
		this.d1 = (d1>0 && d1<=100)?100:0;
	}
	public double getD1()
	{
		return this.d1;
	}
	public String getName() {
		return "line";	}
	private TwoDShape ()
	{
		this.d1=0;
	}
	public TwoDShape(double d1)
{																-
		this();
		this.setD1(d1);
	}
	public abstract double calcArea() {
		return 0;
		
	}
	public double calcperi()
	{
		return this.d1;
	}
	public void printDetails()
	{
		System.out.printf("Name of the shape: %S%n",this.getName());
		System.out.printf("Area             : %.2fsq%n",this.calcArea());
		System.out.printf("perimeter        : %.2f%n",this.calcperi());
		
	}
}
	

