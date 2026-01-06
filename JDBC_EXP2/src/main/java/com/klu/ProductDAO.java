package com.klu;



	import org.hibernate.Session;
	import org.hibernate.Transaction;

	public class ProductDAO {

	    // Insert product
	    public void addProduct(Product p) {
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        Transaction tx = session.beginTransaction();
	        session.save(p);
	        tx.commit();
	        session.close();
	    }

	    // Retrieve product
	    public Product getProduct(int id) {
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        Product p = session.get(Product.class, id);
	        session.close();
	        return p;
	    }

	    // Update product
	    public void updateProduct(Product p) {
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        Transaction tx = session.beginTransaction();
	        session.update(p);
	        tx.commit();
	        session.close();
	    }

	    // Delete product
	    public void deleteProduct(int id) {
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        Transaction tx = session.beginTransaction();

	        Product p = session.get(Product.class, id);
	        if (p != null)
	            session.delete(p);

	        tx.commit();
	        session.close();
	    }
	
}
