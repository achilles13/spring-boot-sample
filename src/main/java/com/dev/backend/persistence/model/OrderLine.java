package com.dev.backend.persistence.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Database Entity for Order Lines.
 * @author creddy
 *
 */
@Entity
@Table(name = "order_line")
public class OrderLine {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "order_line_id")
	private long id;
	
	@OneToOne
	@JoinColumn(name = "prod_code")
	private Product productId;
	
	@Column(name = "quantity")
	private long quantity;
	
	@Column(name = "price")
	private double price;
	
	@Column(name = "total_price")
	private double totalPrice;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "sales_id")
	private SalesOrder salesId;
	
	public OrderLine() {}

	public OrderLine(Product productId, SalesOrder salesOrder, long quantity, double price, double totalPrice) {
		super();
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
		this.salesId = salesOrder;
		this.totalPrice = totalPrice;
	}

	public Product getProductId() {
		return productId;
	}

	public void setProductId(Product productId) {
		this.productId = productId;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public long getId() {
		return id;
	}

	public SalesOrder getSalesId() {
		return salesId;
	}

	public void setSalesId(SalesOrder salesId) {
		this.salesId = salesId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		OrderLine other = (OrderLine) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
