package com.dev.backend.client.model;

import java.util.List;

/**
 * UI Model for Sales Order
 * @author creddy
 *
 */
public class SalesOrderUIModel {
	
	private long code;
	
	private double amount;
	
	private String customerCode;
	
	private List<OrderLineUIModel> orderLines;
	
	public SalesOrderUIModel() {}

	public SalesOrderUIModel(long code, double amount, String customerCode,
			List<OrderLineUIModel> orderLines) {
		super();
		this.code = code;
		this.amount = amount;
		this.customerCode = customerCode;
		this.orderLines = orderLines;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public List<OrderLineUIModel> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLineUIModel> orderLines) {
		this.orderLines = orderLines;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (code ^ (code >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SalesOrderUIModel other = (SalesOrderUIModel) obj;
		if (code != other.code)
			return false;
		return true;
	}


}
