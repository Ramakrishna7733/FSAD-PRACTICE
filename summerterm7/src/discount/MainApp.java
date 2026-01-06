package discount ;
import java.util.Scanner;
import package1.*;
public class MainApp {
	public static void main (String [] args ) {
		Scanner sc = new Scanner (System.in);
		System.out.println("Enter the type of customer ");
		String c = sc.next();
		
		ICustomerDiscount icd = CustomerFactory.getCustomerDiscount(c);
		System.out.println("Enter Amount ");
		double a = sc.nextDouble();
		double pa = icd.calcDiscount(a);
		System.out.println("Final Payable Amount = "+pa );
		sc.close();
	}

}


