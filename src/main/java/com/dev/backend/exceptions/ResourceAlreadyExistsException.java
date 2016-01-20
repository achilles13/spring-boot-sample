package com.dev.backend.exceptions;

/**
 * This Exception is thrown when trying to create a resource that already exists in the system.
 * @author creddy
 *
 */
public class ResourceAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = -6330891301565168754L;

	public ResourceAlreadyExistsException(String message) {
		super(message);
	}

}
