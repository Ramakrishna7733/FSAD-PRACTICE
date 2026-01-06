package package1;

public class GoldCustomerDiscount implements ICustomerDiscount {
	private final static  double GDP=15;
	private final static  double AGDP=5;
	
	public double calcDiscount(double a)
	{
		
		return (a>1500)?(1-(GDP+AGDP)/100)*a:(1-GDP/100)*a;
	}
}