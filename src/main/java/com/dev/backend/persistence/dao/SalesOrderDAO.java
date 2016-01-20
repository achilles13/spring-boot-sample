package com.dev.backend.persistence.dao;

import java.util.List;

import org.hibernate.Session;

import com.dev.backend.persistence.model.SalesOrder;
import com.dev.backend.rest.controller.OperationType;

/**
 * Data Access layer for Sales Order objects.
 * @author creddy
 *
 */
public class SalesOrderDAO {
	
	private SalesOrder addSalesOrder(SalesOrder salesOrder, Session session) {
		session.save(salesOrder);
		return salesOrder;
	}
	
	private SalesOrder getSalesOrder(long code, Session session) {
		SalesOrder order = (SalesOrder) session.createQuery("FROM SalesOrder where code=" + code).uniqueResult();
		return order;
	}

	public Boolean deleteSalesOrder(long code, Session session) {
		SalesOrder salesOrder = getSalesOrder(code, session);
		if (salesOrder == null) {
			return Boolean.FALSE;
		}
		session.delete(salesOrder);
		return Boolean.TRUE;
	}
	
	private List<SalesOrder> getAllSalesOrders(Session session) {
		List<SalesOrder> salesOrders = session.createQuery("FROM SalesOrder").list();
		return salesOrders;
	}
	
	public Object performSalesOrderOperation(OperationType op, Object input,
			Session session) {
		Object result = null;
		switch(op) {
		case GET:
			result = getSalesOrder((Long) input, session);
			break;
		case GET_ALL:
			result = getAllSalesOrders(session);
			break;
		case DELETE:
			result = deleteSalesOrder((Long) input, session);
			break;
		case CREATE:
			result = addSalesOrder((SalesOrder) input, session);
			break;
		}
		return result;
	}

}
