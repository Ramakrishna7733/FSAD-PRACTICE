package client;

import university.Student;

public class MainApp {
	

		 public static void main(String[] args) {
			 
		//declaration statements
			 
			 Student s1,s2;
			 
			 s1 = new Student();
			 s1.setName("  Lakshman");
			 s1.setId(30658);
			 s1.setAttendance(86);
			 s1.setInternalMarks(80);
			 s1.setExternalMarks(84);
			 s1.printSummary();
			 
			 
			 s2 = new Student();
			 s2.setName("  Lucky");
			 s2.setId(30252);
			 s2.setAttendance(90);
			 s2.setInternalMarks(95);
			 s2.setExternalMarks(84);
			 s2.printSummary();
			 
			 
			
			 
		
		 }
	}

