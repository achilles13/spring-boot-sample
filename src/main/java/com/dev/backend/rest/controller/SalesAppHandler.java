package com.dev.backend.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.dev.backend.client.model.CustomerUIModel;
import com.dev.backend.client.model.OrderLineUIModel;
import com.dev.backend.client.model.ProductUIModel;
import com.dev.backend.client.model.SalesOrderUIModel;
import com.dev.backend.exceptions.ResourceNotFoundException;
import com.dev.backend.exceptions.SalesOrderException;
import com.dev.backend.persistence.dao.CustomerDAO;
import com.dev.backend.persistence.dao.ProductDAO;
import com.dev.backend.persistence.dao.SalesOrderDAO;
import com.dev.backend.persistence.model.Customer;
import com.dev.backend.persistence.model.OrderLine;
import com.dev.backend.persistence.model.Product;
import com.dev.backend.persistence.model.SalesOrder;

/**
 * Handler class for Rest endpoints interacting with internal services such as Data Access Objects. Application logic is included in this class.
 * 
 * This encapsulates the hibernate session for all data access operations. 
 * @author creddy
 *
 */
public class SalesAppHandler {

	private final CustomerDAO customerDao;

	private final ProductDAO productDao;

	private final SalesOrderDAO salesOrderDao;

	private final SessionFactory sessionFactory;

	public SalesAppHandler(CustomerDAO customerDao, ProductDAO productDao,
			SalesOrderDAO salesOrderDao, SessionFactory sessionFactory) {
		this.customerDao = customerDao;
		this.productDao = productDao;
		this.salesOrderDao = salesOrderDao;
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Adds the customer to the system. 
	 * @param customerUi
	 * @return saves customer object.
	 */
	public CustomerUIModel addCustomer(CustomerUIModel customerUi) {
		Customer cust = (Customer) performOperation(ObjectType.CUSTOMER, OperationType.CREATE, Utils.convertCompanyUIToServerModel(customerUi));
		return Utils.convertCompanyServerToUIModel(cust);
	}

	/**
	 * Gets the customer with the given code. 
	 * @param code customer identifier
	 * @return
	 * @throws ResourceNotFoundException when customer with the code is not found in system.
	 */
	public CustomerUIModel getCustomerByCode(String code) {
		Customer cust = (Customer) performOperation(ObjectType.CUSTOMER, OperationType.GET, code);
		if (cust != null) {
			return Utils.convertCompanyServerToUIModel(cust);
		}
		return null;
	}

	/**
	 * Deletes the customer with the given code
	 * @param code customer identifier
	 * @return true|false on whether the customer is deleted or not.
	 */
	public boolean deleteCustomerById(String code) {
		Boolean isDeleted = (Boolean) performOperation(ObjectType.CUSTOMER, OperationType.DELETE, code);
		return isDeleted;
	}

	/**
	 * Gets all the customers present in the system.
	 * @return list of customer UI model objects.
	 */
	public List<CustomerUIModel> getAllCustomers() {
		List<Customer> customers = (List<Customer>) performOperation(ObjectType.CUSTOMER, OperationType.GET_ALL, null);

		List<CustomerUIModel> uiModels = new ArrayList<CustomerUIModel>(customers.size());

		for (Customer cust : customers) {
			uiModels.add(Utils.convertCompanyServerToUIModel(cust));
		}

		return uiModels;
	}

	/**
	 * Performs the Operation specified on the requested object with provided input.
	 * This eliminates the boiler plate code for creating a session and starting transaction for all operations performed on persistence models.
	 * @param objectType
	 * @param op
	 * @param input
	 * @return
	 */
	private Object performOperation(ObjectType objectType, OperationType op, Object input) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Object result = null;
		try {
			tx = session.beginTransaction();
			result = performDao(objectType, op, input, session);
			tx.commit();
			return result;
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			throw new RuntimeException(e);
		} finally {
			session.close();
		}
	}

	private Object performDao(ObjectType objectType, OperationType op,
			Object input, Session session) {
		Object result = null;
		switch (objectType) {
		case CUSTOMER:
			result = customerDao.performCustomerOperation(op, input, session);
			break;
		case PRODUCT:
			result = productDao.performProductOperation(op, input, session);
			break;
		case SALES_ORDER:
			result = salesOrderDao.performSalesOrderOperation(op, input, session);
			break;
		}
		return result;
	}

	/**
	 * Adds the product to the system
	 * @param productUi
	 * @return saved product UI model
	 */
	public ProductUIModel addProduct(ProductUIModel productUi) {
		Product prod = (Product) performOperation(ObjectType.PRODUCT, OperationType.CREATE, Utils.convertProductUIToServerModel(productUi));
		return Utils.convertProductServerToUIModel(prod);
	}

	/**
	 * Fetches all products in the system.
	 * @return list of product UI models.
	 */
	public List<ProductUIModel> getAllProducts() {
		List<Product> products = (List<Product>) performOperation(ObjectType.PRODUCT, OperationType.GET_ALL, null);
		List<ProductUIModel> uiModels = new ArrayList<ProductUIModel>(products.size());
		for (Product prod : products) {
			uiModels.add(Utils.convertProductServerToUIModel(prod));
		}

		return uiModels;
	}

	/**
	 * Deletes the product with specified code.
	 * @param code product identifier.
	 * @return true|false 
	 */
	public boolean deleteProductById(String code) {
		Boolean isDeleted = (Boolean) performOperation(ObjectType.PRODUCT, OperationType.DELETE, code);
		return isDeleted;
	}

	/**
	 * Fetches product with specified identifier.
	 * @param code product identifier
	 * @return product UI model
	 * @throws ResourceNotFoundException if product with specified identifier doesn't exist in the system.
	 */
	public ProductUIModel getProductByCode(String code) {
		Product cust = (Product) performOperation(ObjectType.PRODUCT, OperationType.GET, code);
		if (cust != null) {
			return Utils.convertProductServerToUIModel(cust);
		}
		return null;
	}
	
	/**
	 * Adds the sales order to the system. It follows the below algorithm.
	 * 	- Starts a transaction
	 *  - Fetches all the order lines of a sales order with specified id. This is to handle the update of an existing sales order.
	 *  - Fetches Customer with specified identifier and throws ResourceNotFoundException if one doesn't exist.
	 *  - Checks if customer has sufficient funds to go ahead with the sales order.
	 *  	- This considers the update of the sales order, where customer would've already been debited the amount for his earlier order. 
	 *  		So, only the added amount for updated order is checked.
	 *  - Updates Customer object with new credit
	 *  - Loops through all order lines and checks if product with identifier exists and checks if requested quantities are available for this product.
	 *  - Creates the order lines list
	 *  - Save/Update the sales order
	 *  - Commit transaction.
	 * @param salesOrderUi
	 * @return saved Sales order object.
	 * @throws ResourceNotFoundException if customer or product with specified identifier doesn't exist in the system.
	 * @throws SalesOrderException in case of insufficient funds or product quantities are not available.
	 */
	public SalesOrderUIModel addSalesOrder(SalesOrderUIModel salesOrderUi) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			SalesOrder existingSalesOrder = (SalesOrder) performDao(ObjectType.SALES_ORDER,
					OperationType.GET, salesOrderUi.getCode(), session);
			
			Map<String, OrderLine> existingProductQuantity = getExistingOrderLines(existingSalesOrder);
			
			Customer customer = (Customer) performDao(ObjectType.CUSTOMER,
					OperationType.GET, salesOrderUi.getCustomerCode(), session);
			
			double oldAmount = 0;
			if (existingSalesOrder != null) {
				oldAmount = existingSalesOrder.getAmount();
			}
			
			SalesOrder order = createOrUpdateSalesOrder(salesOrderUi, existingSalesOrder);
			
			if (hasInSufficientFunds(customer, oldAmount, salesOrderUi.getAmount())) {
				throw new SalesOrderException("Insufficient Funds for customer. Current Credit limit is : " + 
						customer.getCurrentCredit() + ". Order amount is: " + salesOrderUi.getAmount());
			}
			
			order.setCustomer(customer);
			customer.setCurrentCredit(customer.getCurrentCredit() + salesOrderUi.getAmount() - oldAmount);
			session.update(customer);
			
			List<OrderLine> orderLines = getNewOrUpdatedOrderLines(salesOrderUi, existingProductQuantity, order, session);
			order.setOrderLines(orderLines);
			
			// Delete order lines removed and update the quantity of the products.
			for (OrderLine removedOrderLine : existingProductQuantity.values()) {
				Product prod = removedOrderLine.getProductId();
				prod.setQuantity(prod.getQuantity() + removedOrderLine.getQuantity());
				session.save(prod);
				session.delete(removedOrderLine);
			}
			
			session.save(order);
			tx.commit();
			return Utils.convertOrderServerToUIModel(order);
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			throw new RuntimeException(e);
		} catch (SalesOrderException e) {
			if (tx!=null) tx.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	private SalesOrder createOrUpdateSalesOrder(SalesOrderUIModel salesOrderUi,
			SalesOrder existingSalesOrder) {
		SalesOrder order = null;
		if (existingSalesOrder != null) {
			order = existingSalesOrder;
			order.getOrderLines().clear();
		} else {
			order = new SalesOrder();
		}
		order.setCode(salesOrderUi.getCode());
		order.setAmount(salesOrderUi.getAmount());
		return order;
	}

	private Map<String, OrderLine> getExistingOrderLines(
			SalesOrder existingSalesOrder) {
		Map<String, OrderLine> existingProducts = new HashMap<String, OrderLine>();
		if (existingSalesOrder != null) {
			for (OrderLine orderLine : existingSalesOrder.getOrderLines()) {
				existingProducts.put(orderLine.getProductId().getCode(), orderLine);
			}
		}
		return existingProducts;
	}
	
	private List<OrderLine> getNewOrUpdatedOrderLines(SalesOrderUIModel salesOrderUi,
			Map<String, OrderLine> existingProductQuantity, SalesOrder order, Session session) {
		List<OrderLine> orderLines = new ArrayList<OrderLine>();
		
		double orderLineTotal = 0;
		
		for (OrderLineUIModel orderLineUi : salesOrderUi.getOrderLines()) {
			Product prod = (Product) performDao(ObjectType.PRODUCT,
					OperationType.GET, orderLineUi.getProductCode(), session);
			
			long requestQuantity = orderLineUi.getQuantity();
			
			long availableQuantity = prod.getQuantity();
			
			// incase of updating an order, first update would have deleted requested quantity from product table.
			// So, adding old quantity to available quantity.
			if (existingProductQuantity.containsKey(orderLineUi.getProductCode())) {
				availableQuantity += existingProductQuantity.get(orderLineUi.getProductCode()).getQuantity();
			}
			
			if (availableQuantity < requestQuantity) {
				throw new SalesOrderException("Insufficient Quantity for product with code: " + prod.getCode() + ". Requested quantity is : " + 
						requestQuantity + ". Available quantity is: " + availableQuantity);
			}
			
			prod.setQuantity(availableQuantity - requestQuantity);
			session.update(prod);
			
			OrderLine orderLine = null;
			
			if (existingProductQuantity.containsKey(orderLineUi.getProductCode())) {
				orderLine = existingProductQuantity.get(orderLineUi.getProductCode());
				orderLine.setPrice(orderLineUi.getPrice());
				orderLine.setTotalPrice(orderLineUi.getTotalPrice());
				orderLine.setQuantity(orderLineUi.getQuantity());
				existingProductQuantity.remove(orderLineUi.getProductCode());
			} else {
				orderLine = new OrderLine(prod, order, orderLineUi.getQuantity(), orderLineUi.getPrice(), orderLineUi.getTotalPrice());
			}
			
			orderLineTotal += orderLine.getTotalPrice();
			orderLines.add(orderLine);
		}
		
		if (orderLineTotal != order.getAmount()) {
			throw new SalesOrderException("Price of total orders doesn't match sales order amount. "
					+ "Price of total order lines is :" + orderLineTotal + ". Sales order amount is: " + order.getAmount());
		}
		return orderLines;
	}
	
	/**
	 * Checks if customer has sufficient funds for the sales order
	 * @param customer
	 * @param oldAmount
	 * @param orderAmount
	 * @return
	 */
	private boolean hasInSufficientFunds(Customer customer, double oldAmount, double orderAmount) {
		return (customer.getCreditLimit() + oldAmount - customer.getCurrentCredit()) < orderAmount;
	}

	/**
	 * Fetches Sales order with specified identifier.
	 * @param code sales order identifier.
	 * @return sales order ui model.
	 * @throws ResourceNotFoundException if sales order with specified identifier doesn't exist in system
	 */
	public SalesOrderUIModel getSalesOrderByCode(long code) {
		SalesOrder salesOrder = (SalesOrder) performOperation(ObjectType.SALES_ORDER, OperationType.GET, code);
		if (salesOrder != null) {
			return Utils.convertOrderServerToUIModel(salesOrder);
		} else {
			throw new ResourceNotFoundException("Sales Order with code: " + code + " doesn't exist in the system.");
		}
	}
	
	/**
	 * Fetches all sales orders in the system.
	 * @return list of sales order ui models.
	 */
	public List<SalesOrderUIModel> getAllSalesOrders() {
		List<SalesOrder> salesOrders = (List<SalesOrder>) performOperation(ObjectType.SALES_ORDER, OperationType.GET_ALL, null);
		List<SalesOrderUIModel> uiModels = new ArrayList<SalesOrderUIModel>(salesOrders.size());
		for (SalesOrder order : salesOrders) {
			uiModels.add(Utils.convertOrderServerToUIModel(order));
		}

		return uiModels;
	}

	/**
	 * Deletes the sales order with specified identifier.
	 * @param code sales order identifier.
	 * @return true|false
	 */
	public boolean deleteSalesOrderById(long code) {
		Boolean isDeleted = (Boolean) performOperation(ObjectType.SALES_ORDER, OperationType.DELETE, code);
		return isDeleted;
	}

	/**
	 * Enumeration for type of the object.
	 * @author creddy
	 *
	 */
	private enum ObjectType {
		CUSTOMER,
		PRODUCT,
		SALES_ORDER
	}
}
