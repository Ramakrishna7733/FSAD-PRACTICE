package university;

public class Course {
	//members
		private int code;
		private String title;
		private double credits;
		
		//mutators
		public void setCode(int c) {
			if(c<=9999 && c>=0) this.code = c;
		}
		
		public void setTitle(String t) {
			if(!t.isBlank() && !t.isEmpty()) this.title = t;
		}
		
		public void setCredits(double c) {
			if(c>=0 && c<=10) this.credits = c;
		}
		
		//constructors
		private Course() {
			this.code=0;
			this.title="unknown";
			this.credits = 0;
		}
		
		public Course(String t, int c, double cr) {
			this();
			this.setCode(c);
			this.setTitle(t);
			this.setCredits(cr);
		}
		
		public int getCode() {
			return this.code;
		}
		
		public double getCredits() {
			return this.credits;
		}
		
		public void printCourseSummary() {
			System.out.printf("Code    :%d%n", this.code);
			System.out.printf("Title   :%s%n", this.title.toUpperCase());
			System.out.printf("Credits :%.1f%n", this.credits);		
		}
		
		public boolean isAudit() {
			if(this.credits==0) return true;
			return false;
		}

	}
