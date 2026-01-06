package summerterm24;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaculatorTest {


		Calculator c ;
		
		@BeforeEach
		void setUp() throws Exception {
			
			
			c= new Calculator();
		}

		@AfterEach
		void tearDown() throws Exception {
			c=null;
		}

		@Test
		void testadd() {
			assertEquals(5,c.add(2, 3),"Add method not wotking");
		}
		
		@Test
		void testsub() {
			assertTrue(c.sub(10,3)==7,"Substract method not working");
			
		}

}
