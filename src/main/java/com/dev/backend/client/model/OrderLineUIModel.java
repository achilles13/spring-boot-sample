package com.dev.backend.client.model;

/**
 * UI Model for Order Line
 * @author creddy
 *
 */
public class OrderLineUIModel {
	
	private String productCode;
	
	private long quantity;
	
	private double price;
	
	private double totalPrice;
	
	public OrderLineUIModel() {}

	public OrderLineUIModel(String productCode, long quantity, double price, double totalPrice) {
		super();
		this.productCode = productCode;
		this.quantity = quantity;
		this.price = price;
		this.totalPrice = totalPrice;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((productCode == null) ? 0 : productCode.hashCode());
		result = prime * result + (int) (quantity ^ (quantity >>> 32));
		temp = Double.doubleToLongBits(totalPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		OrderLineUIModel other = (OrderLineUIModel) obj;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		if (quantity != other.quantity)
			return false;
		if (Double.doubleToLongBits(totalPrice) != Double
				.doubleToLongBits(other.totalPrice))
			return false;
		return true;
	}

}
