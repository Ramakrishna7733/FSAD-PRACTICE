package university;

public class Student {
	
			public String name;
			private long id;  
			public int attendance;
			public double intMarks,extMarks;
			public static int count;
			
			//constructor
			 public Student() {
				 // default or initial values
				 this.name="Unknown";
				 this. id=0;
				 this.intMarks=0;
				 this.extMarks=0;
				 this.attendance=0;
			 }
			 
			 public Student(String n,long i)  {
				 this();
				 this.setName(n);
				 this.setId(i);
				 count++;
			 }
			 
			// mutator/setter methods
			public boolean setAttendance(int a) {
				if(a>=0 && a<=100)  {
					 this.attendance = a ;
					 return true;
				}
				else {
					return false;
				}
			}
			
			 public void setInternalMarks(double m) {
				 if(m>=0 && m<=100) this.intMarks = m;
			 }
			 
			 public void setExternalMarks(double m) {
				 if(m>=0 && m<=100) this.extMarks = m;
			 }
			 
			 public void setId(long id) {
				 if(id>=10000 && id<=99999 ) this.id = id;
			 }
			 
			 public void setName(String n)  {
				 if(n.isBlank() || n.isEmpty())  this.name = "Unknown";
				 else   this.name = n.trim();
			 }
			 
			 //accessor methods or getter methods
			 public String getId()
			 {
				 return "24000"+this.id;
			 }
			 
			 public static int getCount() {
				 return count;
			 }
			
			// functional methods
			public String getPromotionStatus()  {
				if(this.attendance <85) return "DETAINED";
						return "PROMOTED";
			}
			
			public double calTotalMarks()  {
				return 0.6*this.intMarks+0.4*this.extMarks;
			}
			
			public char getGrade() {
				double tm=this.calTotalMarks();
				if(this.attendance < 85 || tm< 40 || this.extMarks < 40) return 'F';
				if(tm>=90) return 'O';
				if(tm>=70) return 'A';
				if(tm>=50) return 'B';
				return 'C';
				
			}
			
			 public void printSummary() {
				System.out.printf("Name: %s%n", this.name.toUpperCase());
				System.out.printf("ID: %d%n", this.id);
				System.out.printf("Status: %s%n", this.getPromotionStatus().toUpperCase());
				System.out.printf("Grade: %c%n", this.getGrade());
				
			}
			

}
