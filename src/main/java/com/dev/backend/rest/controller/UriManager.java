package com.dev.backend.rest.controller;

/**
 * Web endpoint paths for the APIs exposed by the application.
 * @author creddy
 *
 */
public interface UriManager {
	
	String BASE_PATH = "/v1";
	
	String USAGE_PATH = BASE_PATH + "/usage"; 
	
	String CUSTOMER_BASE_PATH = BASE_PATH + "/customers";
	
	String PRODUCT_BASE_PATH = BASE_PATH + "/products";
	
	String SALES_ORDER_PATH = BASE_PATH + "/orders";

	String CUSTOMER_ID_PATH = CUSTOMER_BASE_PATH + "/{code}";
	
	String PRODUCT_ID_PATH = PRODUCT_BASE_PATH + "/{code}";
	
	String SALES_ORDER_ID_PATH = SALES_ORDER_PATH + "/{code}";
	
}
