package com.dev.backend.client.model;

/**
 * UI Model for Customer.
 * @author creddy
 *
 */
public class CustomerUIModel {

	private String code;
	
	private String name;
	
	private String address;
	
	private String phone1;
	
	private String phone2;
	
	private double creditLimit;

	private double currentCredit;
	
	public CustomerUIModel() {}
	
	public CustomerUIModel(String code, String name, String address, String phone1,
			String phone2,  double creditLimit,
			double currentCredit) {
		super();
		this.code = code;
		this.name = name;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.address = address;
		this.creditLimit = creditLimit;
		this.currentCredit = currentCredit;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public double getCurrentCredit() {
		return currentCredit;
	}

	public void setCurrentCredit(double currentCredit) {
		this.currentCredit = currentCredit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		CustomerUIModel other = (CustomerUIModel) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
