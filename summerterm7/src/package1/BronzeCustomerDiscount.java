package package1;

public class BronzeCustomerDiscount implements ICustomerDiscount
{
	private final static  double BDP=5;
	public double calcDiscount(double a)
	{
		return (1-BDP/100)*a;
	}

}
