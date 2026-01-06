package Loan;

public class InterestCalculator {

//members
private static double principle;
private static double years;
private static double roi;
private static String choice;

//methods
public static void setPrinciple(double p) {
	principle =(p>=10000 && p<=50000)?p:0;
}
public static void setYears(double y) {
	years=(y>=2 && y<=10)?y:0;
}
public static void setROI(double r) {
	roi=(r>=5 && r<=15)?r:0;
}
public static void setChoice(String s) {
	choice =(s.toLowerCase().equals("simple")||s.toLowerCase().equals("compound"))?s.toUpperCase():"unkown";
} 
private static double calcSimpleInterest() {
	return principle*years*roi/100;
}
private static double calcCompoundInterest() {
	return principle+Math.pow((1+roi/100),years)-principle;
}
public static double calInterest(String c) {
	double interest;
	//if else
	if(choice.equals("COMPOUND")) interest=calcCompoundInterest();
	else if(choice.equals("SIMPLE")) interest=calcSimpleInterest();
	else interest=0;
	// terinary
	interest=(choice.equals("SIMPLE"))?calcSimpleInterest():(choice.equals("COMPOUND"))?calcCompoundInterest():0;
		//SWITCH CASE
	switch(c.toUpperCase()) {
	case "SIMPLE": return calcSimpleInterest();
	case "COMPOUND":return calcCompoundInterest();
	default:return 0;
	}
}
}
