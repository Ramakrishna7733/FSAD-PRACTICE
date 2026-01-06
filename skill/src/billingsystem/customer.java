package billingsystem;
import java.util.*;
public class customer {
	

	    String name;

	    customer(String name) {
	        this.name = name;
	    }
	}

	class Product {
	    String name;

	    Product(String name) {
	        this.name = name;
	    }
	}

	class Order {
	    customer customer;
	    Product[] products;

	    Order(customer customer, Product[] products) {
	        this.customer = customer;
	        this.products = products;
	    }

	    void printSummary() {
	        System.out.println("Customer Name: " + customer.name);
	        System.out.println("Number of Products: " + products.length);
	    }
	}

	
