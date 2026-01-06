package client;
import Loan.InterestCalculator;
import java.util.Scanner;

public class MainApp {
public static void main(String[] args) {
	System.out.println("Enter Principle amount between 200000 & 500000");
	 Scanner sc = new Scanner(System.in);
	 //double p = sc.nextDouble();
	 InterestCalculator.setPrinciple(sc.nextDouble());
	 System.out.println("Enter Yeras");
	 InterestCalculator.setYears(sc.nextDouble());
	 System.out.println("Enter Rate Of Interest");
	 InterestCalculator.setROI(sc.nextDouble());
	 System.out.println("Enter Choice");
	 InterestCalculator.setChoice(sc.next());
	 double interest = InterestCalculator.calInterest("simple");
	 System.out.printf(" Interest= Rs.%.2f%n",interest);
}
}
