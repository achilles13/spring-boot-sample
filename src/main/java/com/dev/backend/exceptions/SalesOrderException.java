package com.dev.backend.exceptions;

/**
 * This exception is thrown when creating or updating a sales order fails.
 * @author creddy
 *
 */
public class SalesOrderException extends RuntimeException {

	private static final long serialVersionUID = 5964806042793151068L;

	public SalesOrderException(String message) {
		super(message);
	}
}
