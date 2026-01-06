package package2;
import university.TwoDShape;
public class Rectangle extends TwoDShape {
	//members
	private double d2;
	//getter
	public double getD2()
	{
		return this.d2;
	}
	public double calcArea()
	{
		return this.d2*this.d1;
	}
	public double calcperi()
	{
		return 2*(this.d2+this.d1);
	}
	public String getName() {
		return "Rectangle";
	}				
//	public void printDetails(){
//
//		System.out.printf("Name of the shape: %S%n",this.getName());
//		System.out.printf("Area             : %.2fsq%n",this.calcArea());
//		System.out.printf("perimeter        : %.2f%n",this.calcperi());
//		
// 
 //	}
	private Rectangle()
	{
		super(0);
		this.d2=0;
	}
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Rectangle(double d1,double d2)
	{
		super(d1);
		this.setD2(d2);
	}
}
