
package com.dev.backend.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.backend.client.model.CustomerUIModel;
import com.dev.backend.client.model.ProductUIModel;
import com.dev.backend.client.model.SalesOrderUIModel;

/**
 * Rest endpoints that are exposed to the clients. Interaction with the system will be using these REST endpoints.
 * This exposed following endpoints for each type of entity.
 * 	-CREATE
 * 	-GET BY ID
 * 	-GET ALL
 * 	-DELETE
 * 
 * Entities supported are
 * 	-Customer
 * 	-Product
 * 	-SalesOrder
 * 
 * @author creddy
 *
 */
@RestController
@Lazy
public class SalesAppEndPoint {

	@Autowired
	private SalesAppHandler handler;
	
	@RequestMapping(value = UriManager.CUSTOMER_BASE_PATH, method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public CustomerUIModel createCustomer (@RequestBody CustomerUIModel customer) {
		return handler.addCustomer(customer);
	}
	
	@RequestMapping(value = UriManager.CUSTOMER_BASE_PATH, method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CustomerUIModel> getCustomers () {
		return handler.getAllCustomers();
	}
	
	@RequestMapping(value = UriManager.CUSTOMER_ID_PATH, method = RequestMethod.DELETE,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public String deleteCustomer (@PathVariable String code) {
		return Boolean.toString(handler.deleteCustomerById(code));
	}
	
	@RequestMapping(value = UriManager.CUSTOMER_ID_PATH, method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomerUIModel getCustomer (@PathVariable String code) {
		return handler.getCustomerByCode(code);
	}
	
	@RequestMapping(value = UriManager.PRODUCT_BASE_PATH , method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProductUIModel createProduct (@RequestBody ProductUIModel product) {
		return handler.addProduct(product);
	}
	
	@RequestMapping(value = UriManager.PRODUCT_BASE_PATH, method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProductUIModel> getProducts () {
		return handler.getAllProducts();
	}
	
	@RequestMapping(value = UriManager.PRODUCT_ID_PATH, method = RequestMethod.DELETE,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public String deleteProduct (@PathVariable String code) {
		return Boolean.toString(handler.deleteProductById(code));
	}
	
	@RequestMapping(value = UriManager.PRODUCT_ID_PATH, method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductUIModel getProduct (@PathVariable String code) {
		return handler.getProductByCode(code);
	}

	@RequestMapping(value = UriManager.SALES_ORDER_PATH , method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public SalesOrderUIModel createSalesOrder(@RequestBody SalesOrderUIModel salesOrder) {
		return handler.addSalesOrder(salesOrder);
	}
	
	@RequestMapping(value = UriManager.SALES_ORDER_PATH, method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SalesOrderUIModel> getOrders () {
		return handler.getAllSalesOrders();
	}
	
	@RequestMapping(value = UriManager.SALES_ORDER_ID_PATH, method = RequestMethod.DELETE,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public String deleteSalesOrder (@PathVariable long code) {
		return Boolean.toString(handler.deleteSalesOrderById(code));
	}
	
	@RequestMapping(value = UriManager.SALES_ORDER_ID_PATH, method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public SalesOrderUIModel getSalesOrder(@PathVariable long code) {
		return handler.getSalesOrderByCode(code);
	}
	
	@RequestMapping(value = UriManager.USAGE_PATH, method = RequestMethod.GET)
	public String usage() {
		return "Supported APIs: " + UriManager.CUSTOMER_BASE_PATH;
	}

}