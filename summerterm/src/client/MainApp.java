package client;
import university.Student;
import university.Course;
public class MainApp {
public static void main(String[] args) {
	Student s1, s2, s3;
	s1 = new Student("Ravi", 34567);
	Course c1 = new Course("OOPA",2006, 6.5);
	Course c2 = new Course("OOP",2005, 4.5);
	Course c3 = new Course("DBMS", 3215,4);
	Course c4 = new Course("GenAI",2001,0);
	
	s1.registerCourse(c1);
	s1.registerCourse(c2);
	s1.registerCourse(c3);
	s1.registerCourse(c4);
	
	s2 = new Student("Harshita",45678);
	s2.registerCourse(c2);
	s2.registerCourse(c3);
	s2.registerCourse(c4);
	
	s3 = new Student("Satish", 34678);
	s3.registerCourse(c1);
	s3.registerCourse(c4);
	
	s1.printStudentSummary();
	s2.printStudentSummary();
	s3.printStudentSummary();
}
}

