package com.dev.backend.rest.controller;

import java.util.ArrayList;
import java.util.List;

import com.dev.backend.client.model.CustomerUIModel;
import com.dev.backend.client.model.OrderLineUIModel;
import com.dev.backend.client.model.ProductUIModel;
import com.dev.backend.client.model.SalesOrderUIModel;
import com.dev.backend.persistence.model.Customer;
import com.dev.backend.persistence.model.OrderLine;
import com.dev.backend.persistence.model.Product;
import com.dev.backend.persistence.model.SalesOrder;

/**
 * Utility class for converting from UI model to Server models.
 * 
 * Server and UI models are separated so that versions of the REST APIs can be maintained if there is any change in APIs in future.
 * @author creddy
 *
 */
public class Utils {
	
	static Customer convertCompanyUIToServerModel(CustomerUIModel customerUi) {
		Customer cust = new Customer(customerUi.getCode(), customerUi.getName(),
				customerUi.getAddress(), customerUi.getPhone1(), customerUi.getPhone2(),
				customerUi.getCreditLimit(), customerUi.getCurrentCredit());
		return cust;
	}

	static CustomerUIModel convertCompanyServerToUIModel(Customer cust) {
		CustomerUIModel custUi = new CustomerUIModel(cust.getCode(), cust.getName(), cust.getAddress()
				, cust.getPhone1(), cust.getPhone2(), cust.getCreditLimit(), cust.getCurrentCredit());
		return custUi;
	}
	
	static Product convertProductUIToServerModel(ProductUIModel productUi) {
		Product prod = new Product(productUi.getCode(), productUi.getDescription(), productUi.getPrice(), productUi.getQuantity());
		return prod;
	}

	static ProductUIModel convertProductServerToUIModel(Product prod) {
		ProductUIModel prodUi = new ProductUIModel(prod.getCode(), prod.getDescription(), prod.getPrice(), prod.getQuantity());
		return prodUi;
	}
	
	static SalesOrderUIModel convertOrderServerToUIModel(SalesOrder salesOrder) {
		SalesOrderUIModel uiModel = new SalesOrderUIModel();
		uiModel.setCode(salesOrder.getCode());
		uiModel.setAmount(salesOrder.getAmount());
		uiModel.setCustomerCode(salesOrder.getCustomer().getCode());

		List<OrderLineUIModel> orderLines = new ArrayList<OrderLineUIModel>();
		for (OrderLine orderLine : salesOrder.getOrderLines()) {
			OrderLineUIModel orderLineUi = new OrderLineUIModel(orderLine.getProductId().getCode(),
					orderLine.getQuantity(), orderLine.getPrice(), orderLine.getTotalPrice());
			orderLines.add(orderLineUi);
		}
		uiModel.setOrderLines(orderLines);
		return uiModel;
	}

}
