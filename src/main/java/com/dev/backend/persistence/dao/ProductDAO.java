package com.dev.backend.persistence.dao;

import java.util.List;

import org.hibernate.Session;

import com.dev.backend.exceptions.ResourceAlreadyExistsException;
import com.dev.backend.exceptions.ResourceNotFoundException;
import com.dev.backend.persistence.model.Product;
import com.dev.backend.rest.controller.OperationType;

/**
 * Data Access layer for Product objects.
 * @author creddy
 *
 */
public class ProductDAO {

	private Product addProduct(Product product, Session session) {
		Product existingProd = getProduct(product.getCode(), session);
		if (existingProd != null) {
			throw new ResourceAlreadyExistsException("Product with code: " + product.getCode() + " already exists");
		} 
		session.save(product);
		return product;
	}

	private Product getProduct(String code, Session session) {
		Product product= (Product) session.createQuery("FROM Product where code=" + code).uniqueResult();
		return product;
	}

	private Boolean deleteProduct(String code, Session session) {
		Product prod = getProduct(code, session);
		if (prod == null) {
			return Boolean.FALSE;
		}
		session.delete(prod);
		return Boolean.TRUE;
	}

	private List<Product> getAllProducts(Session session) {
		List<Product> products = session.createQuery("FROM Product").list();
		return products;
	}

	public Object performProductOperation(OperationType op, Object input,
			Session session) {
		Object result = null;
		switch(op) {
		case GET:
			result = getProduct((String) input, session);
			if (result == null) {
				throw new ResourceNotFoundException("Product with code: " + (String) input + " doesn't exist in the system.");
			}
			break;
		case GET_ALL:
			result = getAllProducts(session);
			break;
		case DELETE:
			result = deleteProduct((String) input, session);
			break;
		case CREATE:
			result = addProduct((Product) input, session);
			break;
		}
		return result;
	}
}
