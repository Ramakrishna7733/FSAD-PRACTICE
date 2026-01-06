package billingsystem;

public class BillingSystem {
	
	    public static void main(String[] args) {
	        customer c = new customer("Ram");

	        Product[] items = {
	            new Product("Pen"),
	            new Product("Book"),
	            new Product("Bag")
	        };

	        Order order = new Order(c, items);
	        order.printSummary();
	    }
	}