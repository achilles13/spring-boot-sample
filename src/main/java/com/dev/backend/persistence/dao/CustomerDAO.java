package com.dev.backend.persistence.dao;

import java.util.List;

import org.hibernate.Session;

import com.dev.backend.exceptions.ResourceAlreadyExistsException;
import com.dev.backend.exceptions.ResourceNotFoundException;
import com.dev.backend.persistence.model.Customer;
import com.dev.backend.rest.controller.OperationType;

/**
 * Data Access layer for Customer objects.
 * @author creddy
 *
 */
public class CustomerDAO {
	
	public Customer addCustomer(Customer customer, Session session) {
		Customer dbCustomer = getCustomer(customer.getCode(), session);
		if (dbCustomer != null) {
			throw new ResourceAlreadyExistsException("Customer with code: " + customer.getCode() + " already exists in the repo");
		}
		customer.setCurrentCredit(0);
		session.save(customer);
		
		return customer;
	}
	
	public Customer getCustomer(String code, Session session) {
		Customer customer= (Customer) session.createQuery("FROM Customer where code=" + code).uniqueResult();
		return customer;
	}
	
	public Boolean deleteCustomer(String code, Session session) {
		Customer customer = getCustomer(code, session);
		if (customer == null) {
			return Boolean.FALSE;
		}
		session.delete(customer);
		return Boolean.TRUE;
	}

	public List<Customer> getAllCustomers(Session session) {
		List<Customer> customers = session.createQuery("FROM Customer").list();
		return customers;
	}

	public Object performCustomerOperation(OperationType op, Object input,
			Session session) {
		Object result = null;
		switch(op) {
		case GET:
			result = getCustomer((String) input, session);
			if (result == null) {
				throw new ResourceNotFoundException("Customer with code: " + (String) input + " doesn't exist in the system.");
			}
			break;
		case GET_ALL:
			result = getAllCustomers(session);
			break;
		case DELETE:
			result = deleteCustomer((String) input, session);
			break;
		case CREATE:
			result = addCustomer((Customer) input, session);
			break;
		}
		return result;
	}

}
