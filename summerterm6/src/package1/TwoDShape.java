package package1;

public abstract class TwoDShape {
	//members
	protected double s1;
	//methods
	public void setS1(double s1)
	{
		this.s1 = (s1>0 && s1<=100)?s1:0;
	}
	private TwoDShape()
	{
		this.s1=0;
	}
	

}
