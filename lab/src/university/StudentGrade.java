package university;
import java.util.Scanner;
public class StudentGrade {
	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	        int marks = scanner.nextInt();

	        if (marks >= 0 && marks <= 100) {
	            if (marks >= 90) {
	                System.out.println("O Grade");
	            } else {
	                if (marks >= 80) {
	                    System.out.println("A+ Grade");
	                } else {
	                    if (marks >= 70) {
	                        System.out.println("A Grade");
	                    } else {
	                        if (marks >= 60) {
	                            System.out.println("B+ Grade");
	                        } else {
	                            if (marks >= 50) {
	                                System.out.println("B Grade");
	                            } else {
	                                System.out.println("F");
	                            }
	                        }
	                    }
	                }
	            }
	        } else {
	            System.out.println("Invalid marks! Please enter a value between 0 and 100.");
	        }

	        scanner.close();
	    }
	}


