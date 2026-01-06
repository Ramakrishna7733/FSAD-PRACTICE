package college;

public class Class {
	private int id;
	private String name;
	private int[] marks;
	private int count;

	//constructors3
	
	private Class() {
		id=0;
		name ="unknown";
		marks = new int[5];
		count =0;
	}
	public Class(String n,int id) {
		this();
		this.name =n;
		this.id=id;
		
	}
	public boolean addMarks(int m) {
		if(count>=5||m<0)return false;
		marks[count++]=m;return true;
	}
	public double averageMarks() {
		double sum =0;
		if(count ==0)return 0;
		for(int i=0;i<count++;i++)sum+=marks[i];
		return sum/count;
	}
	public void printSummary() {
		System.out.printf("ID :%d%n",this.id);
		System.out.printf("Name :%s%n",this.name);
		System.out.printf("Average Marks: %2.f%n", this.averageMarks());
	}

}
