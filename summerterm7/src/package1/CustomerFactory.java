package package1;

public class CustomerFactory {


public static ICustomerDiscount getCustomerDiscount(String c)

{switch(c.toLowerCase())
	{
	case "gold": return new GoldCustomerDiscount();
	case "silver": return  new SilverCustomerDiscount();
	case "bronze": return  new BronzeCustomerDiscount();
	case "platinum": return  new PlatinumCustomerDiscount();
	default : return null;
	}
}
}
