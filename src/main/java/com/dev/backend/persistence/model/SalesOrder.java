package com.dev.backend.persistence.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Database Entity for Sales Orders.
 * @author creddy
 *
 */
@Entity
@Table(name = "sales_order")
public class SalesOrder {
	
	@Id
	@Column(name = "order_code")
	private long code;
	
	@Column(name = "amount")
	private double amount;
	
	@OneToOne
	@JoinColumn(name = "cust_code")
	private Customer customer;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity=OrderLine.class, mappedBy="salesId")
	private List<OrderLine> orderLines;
	
	public SalesOrder() {}

	public SalesOrder(long code, double amount, Customer customer,
			List<OrderLine> orderLines) {
		super();
		this.code = code;
		this.amount = amount;
		this.customer = customer;
		this.orderLines = orderLines;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<OrderLine> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
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
		SalesOrder other = (SalesOrder) obj;
		if (code != other.code)
			return false;
		return true;
	}

}
