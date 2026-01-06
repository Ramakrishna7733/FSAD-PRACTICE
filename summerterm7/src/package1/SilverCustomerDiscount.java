package package1;

public class SilverCustomerDiscount implements ICustomerDiscount {
	private final static  double SDP=10;
	public double calcDiscount(double a)
	{
		
		return (1-SDP/100)*a;
	}

}
