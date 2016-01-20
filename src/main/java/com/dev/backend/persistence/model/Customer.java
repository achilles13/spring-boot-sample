package com.dev.backend.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Database Entity for Customer objects.
 * @author creddy
 *
 */
@Entity
@Table(name = "customer")
public class Customer {
	
	@Column(name = "cust_code", unique = true)
	@Id
	private String code;
	
	@Column(name = "cust_name")
	private String name;
	
	@Column(name = "addr")
	private String address;
	
	@Column(name = "phone1", length = 50)
	private String phone1;
	
	@Column(name = "phone2", length = 50)
	private String phone2;
	
	@Column(name = "credit_limit")
	private double creditLimit;
	
	@Column(name = "curr_credit")
	private double currentCredit;
	
	public Customer() {}

	public Customer(String code, String name, String address, String phone1,
			String phone2, double creditLimit, double currentCredit) {
		super();
		this.code = code;
		this.name = name;
		this.address = address;
		this.phone1 = phone1;
		this.phone2 = phone2;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
		long temp;
		temp = Double.doubleToLongBits(creditLimit);
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
		Customer other = (Customer) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (Double.doubleToLongBits(creditLimit) != Double
				.doubleToLongBits(other.creditLimit))
			return false;
		return true;
	}


}
