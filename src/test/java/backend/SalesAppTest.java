package backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dev.backend.client.model.CustomerUIModel;
import com.dev.backend.client.model.OrderLineUIModel;
import com.dev.backend.client.model.ProductUIModel;
import com.dev.backend.client.model.SalesOrderUIModel;
import com.dev.backend.exceptions.ResourceNotFoundException;
import com.dev.backend.exceptions.SalesOrderException;
import com.dev.backend.persistence.dao.CustomerDAO;
import com.dev.backend.persistence.dao.ProductDAO;
import com.dev.backend.persistence.dao.SalesOrderDAO;
import com.dev.backend.persistence.management.DBManagerUtil;
import com.dev.backend.rest.controller.SalesAppHandler;

public class SalesAppTest {

	private static SalesAppHandler appHandler;
	
	private static SessionFactory sessionFactory;
	
	@BeforeClass
	public static void setup() {
		DBManagerUtil managerUtil = new DBManagerUtil();
		sessionFactory = managerUtil.getSessionFactory();
		appHandler = new SalesAppHandler(new CustomerDAO(), new ProductDAO(), new SalesOrderDAO(), sessionFactory);
	}
	
	@AfterClass
	public static void tearDown() {
		sessionFactory.close();
	}


	@Test
	public void testCreateCustomer() {
		CustomerUIModel cust = appHandler.addCustomer(new CustomerUIModel("1","Chanakya", "Bangalore", "9740601562", "", 50000, 40000));
		assertNotNull("Response for add customer should not be null", cust);
		
		CustomerUIModel fetchedCust = appHandler.getCustomerByCode(cust.getCode());
		assertEquals("Chanakya", fetchedCust.getName());
		
		List<CustomerUIModel> customers = appHandler.getAllCustomers();
		assertTrue(customers.size() > 1);
	}
	
	@Test
	public void testCreateProduct() {
		ProductUIModel prod = appHandler.addProduct(new ProductUIModel("1", "Product 1", 100, 10));
		assertNotNull("Response for add product should not be null", prod);
		
		ProductUIModel fetchedProd = appHandler.getProductByCode(prod.getCode());
		assertEquals("Product 1", fetchedProd.getDescription());
		
		List<ProductUIModel> products = appHandler.getAllProducts();
		assertTrue(products.size() > 1);
	}
	
	@Test
	public void testCreateSalesOrder() {
		try {
			CustomerUIModel cust = appHandler.addCustomer(new CustomerUIModel("2","Chanakya2", "Bangalore", "9740601562", "", 50000, 0));
			assertNotNull("Response for add customer should not be null", cust);
			
			ProductUIModel prod = appHandler.addProduct(new ProductUIModel("2", "Product 2", 100, 10));
			assertNotNull("Response for add product should not be null", prod);
			
			SalesOrderUIModel salesOrderUi = new SalesOrderUIModel();
			salesOrderUi.setCode(1);
			salesOrderUi.setAmount(1000);
			salesOrderUi.setCustomerCode("2");
			
			List<OrderLineUIModel> orderLines = new ArrayList<OrderLineUIModel>();
			orderLines.add(new OrderLineUIModel("2", 10, 100, 1000));
			salesOrderUi.setOrderLines(orderLines);
			
			SalesOrderUIModel savedOrderUi = appHandler.addSalesOrder(salesOrderUi);
			assertEquals("Failure while comparing code of created and saved order ", 
					salesOrderUi.getCode(), savedOrderUi.getCode());
			
			SalesOrderUIModel fetchedOrderUi = appHandler.getSalesOrderByCode(salesOrderUi.getCode());
			assertNotNull("Sales order with code: " + salesOrderUi.getCode() + " should not be null" , fetchedOrderUi);
			assertEquals("Failure while comparing code of created and fetched order ", 
					salesOrderUi.getCode(), fetchedOrderUi.getCode());
			
			List<SalesOrderUIModel> allOrders = appHandler.getAllSalesOrders();
			assertEquals("Failure while comparing size of total orders in system ", 1, allOrders.size());
			
			ProductUIModel updatedProd = appHandler.getProductByCode("2");
			assertEquals("Product quantity is not reduced", 0, updatedProd.getQuantity());
			
			CustomerUIModel updatedCust = appHandler.getCustomerByCode("2");
			assertEquals("Current credit is not updated for customer", 1000, updatedCust.getCurrentCredit(), 0);
			
		} catch (Throwable th) {
			Assert.fail(th.getMessage());
		}
	}
	
	@Test
	public void testGreaterThanCreditLimitSalesOrder() {
		try {
			CustomerUIModel cust = appHandler.addCustomer(new CustomerUIModel("3","Chanakya3", "Bangalore", "9740601562", "", 5000, 0));
			assertNotNull("Response for add customer should not be null", cust);
			
			ProductUIModel prod = appHandler.addProduct(new ProductUIModel("3", "Product 3", 510, 10));
			assertNotNull("Response for add product should not be null", prod);
			
			SalesOrderUIModel salesOrderUi = new SalesOrderUIModel();
			salesOrderUi.setCode(3);
			salesOrderUi.setAmount(5100);
			salesOrderUi.setCustomerCode("3");
			
			List<OrderLineUIModel> orderLines = new ArrayList<OrderLineUIModel>();
			orderLines.add(new OrderLineUIModel("3", 10, 510, 5100));
			salesOrderUi.setOrderLines(orderLines);
			
			appHandler.addSalesOrder(salesOrderUi);
			Assert.fail("Should fail saving the order as credit limit is less than the order amount");
		} catch (SalesOrderException e) {
			// Expected Error
		} catch (Throwable th) {
			Assert.fail(th.getMessage());
		}
	}
	
	@Test
	public void testGreaterThanProductQuantitySalesOrder() {
		try {
			CustomerUIModel cust = appHandler.addCustomer(new CustomerUIModel("4","Chanakya4", "Bangalore", "9740601562", "", 20000, 0));
			assertNotNull("Response for add customer should not be null", cust);
			
			ProductUIModel prod = appHandler.addProduct(new ProductUIModel("4", "Product 4", 510, 10));
			assertNotNull("Response for add product should not be null", prod);
			
			SalesOrderUIModel salesOrderUi = new SalesOrderUIModel();
			salesOrderUi.setCode(4);
			salesOrderUi.setAmount(10200);
			salesOrderUi.setCustomerCode("4");
			
			List<OrderLineUIModel> orderLines = new ArrayList<OrderLineUIModel>();
			orderLines.add(new OrderLineUIModel("4", 20, 510, 10200));
			salesOrderUi.setOrderLines(orderLines);
			
			appHandler.addSalesOrder(salesOrderUi);
			Assert.fail("Should fail saving the order as requested quantity is greater than the available quantity");
		} catch (SalesOrderException e) {
			// Expected Error
		} catch (Throwable th) {
			Assert.fail(th.getMessage());
		}
	}
	
	@Test
	public void testUpdateExistingOrder() {
		try {
			CustomerUIModel cust = appHandler.addCustomer(new CustomerUIModel("5","Chanakya5", "Bangalore", "9740601562", "", 5000, 0));
			assertNotNull("Response for add customer should not be null", cust);
			
			ProductUIModel prod = appHandler.addProduct(new ProductUIModel("5", "Product 5", 51, 10));
			assertNotNull("Response for add product should not be null", prod);
			
			SalesOrderUIModel salesOrderUi = new SalesOrderUIModel();
			salesOrderUi.setCode(5);
			salesOrderUi.setAmount(51);
			salesOrderUi.setCustomerCode("5");
			
			List<OrderLineUIModel> orderLines = new ArrayList<OrderLineUIModel>();
			orderLines.add(new OrderLineUIModel("5", 1, 51, 51));
			salesOrderUi.setOrderLines(orderLines);
			
			SalesOrderUIModel savedSalesOrderUi = appHandler.addSalesOrder(salesOrderUi);
			assertEquals(salesOrderUi.getCode(), savedSalesOrderUi.getCode());
			assertEquals(salesOrderUi.getCustomerCode(), savedSalesOrderUi.getCustomerCode());
			assertEquals(salesOrderUi.getAmount(), savedSalesOrderUi.getAmount(), 0);
			assertEquals(salesOrderUi.getOrderLines().size(), savedSalesOrderUi.getOrderLines().size());
			
			orderLines.clear();
			salesOrderUi.setAmount(102);
			orderLines.add(new OrderLineUIModel("5", 2, 51, 102));
			
			SalesOrderUIModel updatedSalesOrderUi = appHandler.addSalesOrder(salesOrderUi);
			assertEquals(salesOrderUi.getCode(), updatedSalesOrderUi.getCode());
			assertEquals(salesOrderUi.getCustomerCode(), updatedSalesOrderUi.getCustomerCode());
			assertEquals(salesOrderUi.getAmount(), updatedSalesOrderUi.getAmount(), 0);
			assertEquals(salesOrderUi.getOrderLines().size(), updatedSalesOrderUi.getOrderLines().size());
			
			ProductUIModel updatedProd = appHandler.getProductByCode("5");
			assertEquals("Product quantity is not reduced", 8, updatedProd.getQuantity());
			
			CustomerUIModel updatedCust = appHandler.getCustomerByCode("5");
			assertEquals("Current credit is not updated for customer", 102, updatedCust.getCurrentCredit(), 0);
			
		} catch (Throwable th) {
			Assert.fail(th.getMessage());
		}
	}
	
	@Test
	public void testRemoveOrderLineInExistingSalesOrder() {
		try {
			CustomerUIModel cust = appHandler.addCustomer(new CustomerUIModel("6","Chanakya6", "Bangalore", "9740601562", "", 5000, 0));
			assertNotNull("Response for add customer should not be null", cust);
			
			ProductUIModel prod = appHandler.addProduct(new ProductUIModel("6", "Product 6", 510, 10));
			assertNotNull("Response for add product should not be null", prod);
			
			ProductUIModel prod2 = appHandler.addProduct(new ProductUIModel("7", "Product 7", 210, 10));
			assertNotNull("Response for add product should not be null", prod2);
			
			SalesOrderUIModel salesOrderUi = new SalesOrderUIModel();
			salesOrderUi.setCode(6);
			salesOrderUi.setAmount(51);
			salesOrderUi.setCustomerCode("6");
			
			List<OrderLineUIModel> orderLines = new ArrayList<OrderLineUIModel>();
			orderLines.add(new OrderLineUIModel("6", 1, 51, 51));
			salesOrderUi.setOrderLines(orderLines);
			
			SalesOrderUIModel savedSalesOrderUi = appHandler.addSalesOrder(salesOrderUi);
			assertEquals(salesOrderUi.getCode(), savedSalesOrderUi.getCode());
			assertEquals(salesOrderUi.getCustomerCode(), savedSalesOrderUi.getCustomerCode());
			assertEquals(salesOrderUi.getAmount(), savedSalesOrderUi.getAmount(), 0);
			assertEquals(salesOrderUi.getOrderLines().size(), savedSalesOrderUi.getOrderLines().size());
			
			orderLines.clear();
			orderLines.add(new OrderLineUIModel("7", 1, 210, 210));
			salesOrderUi.setAmount(210);
			
			SalesOrderUIModel updatedSalesOrderUi = appHandler.addSalesOrder(salesOrderUi);
			assertEquals(salesOrderUi.getCode(), updatedSalesOrderUi.getCode());
			assertEquals(salesOrderUi.getCustomerCode(), updatedSalesOrderUi.getCustomerCode());
			assertEquals(salesOrderUi.getAmount(), updatedSalesOrderUi.getAmount(), 0);
			assertEquals(salesOrderUi.getOrderLines().size(), updatedSalesOrderUi.getOrderLines().size());
			
			ProductUIModel updatedProd6 = appHandler.getProductByCode("6");
			assertEquals("Product 6 quantity should be reset to orginial quantity as it is removed.", 10, updatedProd6.getQuantity());
			
			ProductUIModel updatedProd7 = appHandler.getProductByCode("7");
			assertEquals("Product 7 quantity should be reduced", 9, updatedProd7.getQuantity());
			
			CustomerUIModel updatedCust = appHandler.getCustomerByCode("6");
			assertEquals("Current credit is not updated for customer", 210, updatedCust.getCurrentCredit(), 0);
			
		} catch (Throwable th) {
			Assert.fail(th.getMessage());
		}
	}
	
	@Test
	public void testInvalidSalesAndOrderLineTotalPrice() {
		try {
			CustomerUIModel cust = appHandler.addCustomer(new CustomerUIModel("8","Chanakya8", "Bangalore", "9740601562", "", 50000, 0));
			assertNotNull("Response for add customer should not be null", cust);
			
			ProductUIModel prod = appHandler.addProduct(new ProductUIModel("8", "Product 8", 100, 10));
			assertNotNull("Response for add product should not be null", prod);
			
			SalesOrderUIModel salesOrderUi = new SalesOrderUIModel();
			salesOrderUi.setCode(8);
			salesOrderUi.setAmount(900);
			salesOrderUi.setCustomerCode("8");
			
			List<OrderLineUIModel> orderLines = new ArrayList<OrderLineUIModel>();
			orderLines.add(new OrderLineUIModel("8", 10, 100, 1000));
			salesOrderUi.setOrderLines(orderLines);
			
			appHandler.addSalesOrder(salesOrderUi);
			Assert.fail("It should throw exception as sales order amount doesn't match sum of all total order lines amount.");
		} catch(SalesOrderException e) {
			// Expected exception.
		} catch (Throwable th) {
			Assert.fail(th.getMessage());
		}
	}
	
	@Test
	public void testDeleteCustomer() {
		try {
			CustomerUIModel cust = appHandler.addCustomer(new CustomerUIModel("9","Chanakya9", "Bangalore", "9740601562", "", 50000, 0));
			assertNotNull("Response for add customer should not be null", cust);
			
			appHandler.deleteCustomerById(cust.getCode());
			
			appHandler.getCustomerByCode(cust.getCode());
			Assert.fail("It should throw an exception as Customer with code 9 should have been deleted from system");
		} catch (ResourceNotFoundException e) {
			// Do Nothing. Expected Exception
		} catch (Throwable e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testDeleteProduct() {
		try {
			ProductUIModel prod = appHandler.addProduct(new ProductUIModel("10", "Product 10", 100, 10));
			assertNotNull("Response for add product should not be null", prod);
			
			appHandler.deleteProductById(prod.getCode());
			
			appHandler.getProductByCode(prod.getCode());
			Assert.fail("It should throw an exception as Product with code 10 should have been deleted from system");
		} catch (ResourceNotFoundException e) {
			// Do Nothing. Expected Exception
		} catch (Throwable e) {
			Assert.fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testDeleteSalesOrder() {
		try {
			CustomerUIModel cust = appHandler.addCustomer(new CustomerUIModel("11","Chanakya11", "Bangalore", "9740601562", "", 5000, 0));
			assertNotNull("Response for add customer should not be null", cust);
			
			ProductUIModel prod = appHandler.addProduct(new ProductUIModel("11", "Product 11", 51, 10));
			assertNotNull("Response for add product should not be null", prod);
			
			SalesOrderUIModel salesOrderUi = new SalesOrderUIModel();
			salesOrderUi.setCode(11);
			salesOrderUi.setAmount(51);
			salesOrderUi.setCustomerCode("11");
			
			List<OrderLineUIModel> orderLines = new ArrayList<OrderLineUIModel>();
			orderLines.add(new OrderLineUIModel("11", 1, 51, 51));
			salesOrderUi.setOrderLines(orderLines);
			
			SalesOrderUIModel savedSalesOrderUi = appHandler.addSalesOrder(salesOrderUi);
			assertEquals(salesOrderUi.getCode(), savedSalesOrderUi.getCode());
			assertEquals(salesOrderUi.getCustomerCode(), savedSalesOrderUi.getCustomerCode());
			assertEquals(salesOrderUi.getAmount(), savedSalesOrderUi.getAmount(), 0);
			assertEquals(salesOrderUi.getOrderLines().size(), savedSalesOrderUi.getOrderLines().size());
			
			appHandler.deleteSalesOrderById(salesOrderUi.getCode());
			
			appHandler.getSalesOrderByCode(salesOrderUi.getCode());
			
			Assert.fail("It should throw an exception as Sales order with code 11 should have been deleted from system");
		} catch (ResourceNotFoundException e) {
			// Do Nothing. Expected Exception
		} catch (Throwable e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void fetchNonExistentCustomerByCode() {
		try {
			appHandler.getCustomerByCode("1000");
			Assert.fail("It should throw exception as Customer with code 1000 doesn't exist");
		} catch (ResourceNotFoundException e) {
			// Do Nothing. Expected Exception
		} catch (Throwable e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void fetchNonExistentProductByCode() {
		try {
			appHandler.getProductByCode("1000");
			Assert.fail("It should throw exception as Product with code 1000 doesn't exist");
		} catch (ResourceNotFoundException e) {
			// Do Nothing. Expected Exception
		} catch (Throwable e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void fetchNonExistentSalesOrderByCode() {
		try {
			appHandler.getSalesOrderByCode(1000);
			Assert.fail("It should throw exception as Sales Order with code 1000 doesn't exist");
		} catch (ResourceNotFoundException e) {
			// Do Nothing. Expected Exception
		}  catch (Throwable e) {
			Assert.fail(e.getMessage());
		}
	}
	
}
