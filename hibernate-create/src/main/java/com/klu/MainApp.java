package com.klu;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;



public class MainApp {
	
	public static void main(String[] args) {
		//load configuration & create session factory
		SessionFactory factory = new Configuration()
				.configure().buildSessionFactory();
					//Open session
					Session session=factory.openSession();
					//begin transaction
					Transaction tx=session.beginTransaction();
					Student s=new Student("Ravi");
					//save object
					session.save(s);
					//commit
					tx.commit();
					//close the resources
					session.close();
					factory.close();
					System.out.println("Data have been inserted successfully");
	}
}
