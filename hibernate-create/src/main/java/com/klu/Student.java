package com.klu;
import javax.persistence.*;

@Entity
@Table(name="student")
public class Student {
   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
   private String name;
   public Student() {};
   public Student(String name)
   {
	   this.name=name;
   }
	
}
