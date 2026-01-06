package com.klu;


	public class MainApp {

	    public static void main(String[] args) {

	        ProductDAO dao = new ProductDAO();

	        // Insert multiple records
	        dao.addProduct(new Product(101,"Laptop","Lenovo Thinkpad",65000,5));
	        dao.addProduct(new Product(102,"Mouse","Wireless Mouse",900,20));
	        dao.addProduct(new Product(103,"Keyboard","Mechanical Keyboard",3500,10));

	        // Retrieve product
	        Product p = dao.getProduct(101);
	        System.out.println("Product = " + p.getName());

	        // Update quantity
	        p.setQuantity(9);
	        dao.updateProduct(p);

	        // Delete discontinued product
	        dao.deleteProduct(103);
	    }
	}

