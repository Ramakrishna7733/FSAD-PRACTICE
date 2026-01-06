package university;

public class Student {
	//members
	private String name;
	private int id;
	private Course[] regCourses;//declaration statement
	private int countCourses;
	private double countCredits;
	
	private Student() {
		this.name = "unknown";
		this.id = 0;
		this.regCourses = new Course[3];//initialization statement
		this.countCredits = 0;
	}
	
	public Student(String n, int i) {
		this();
		this.setName(n);
		this.setID(i);
	}
	
	public void setName(String n) {
		if(!n.isBlank() && !n.isEmpty()) this.name = n;
	}
	
	private void setID(int i) {
		if(i<=99999 && i>=10000) this.id = i;
	}
	
	//functional methods
	
	private boolean isCodeRepeat(Course c) {
		for(int i=0; i<this.countCourses;i++) {
			Course tempCourse = this.regCourses[i];
			if(c.getCode()==tempCourse.getCode()) return true;
		}
		return false;
	}
	
	private boolean isCreditsFull(Course c) {
		if(this.countCredits+c.getCredits()<=10) return false;
		return true;
	}
	
	public boolean registerCourse(Course c) {
		if(this.countCourses<3 && !isCreditsFull(c) && !this.isCodeRepeat(c)) {
			//Course newCourse = new Course(c,t,cr);
			this.regCourses[this.countCourses] = c;
			this.countCourses++;
			this.countCredits+=c.getCredits();
		return true;
		}
		return false;
	}
	
	public void printStudentSummary() {
		System.out.printf("Name :%S%n" , this.name);
		System.out.printf("ID   :%d%n" , this.id);
		for(int i=0; i<this.countCourses; i++)
			this.regCourses[i].printCourseSummary();
		System.out.println();
	}
	
	
}
