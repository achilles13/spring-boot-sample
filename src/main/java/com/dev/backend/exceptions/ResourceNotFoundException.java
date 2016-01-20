package com.dev.backend.exceptions;

/**
 * This Exception is thrown when a requested resource is not found in the system.
 * @author creddy
 *
 */
public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 6427673998268803678L;

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
