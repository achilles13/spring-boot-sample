package com.dev.backend.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dev.backend.client.model.ErrorDetails;
import com.dev.backend.exceptions.ResourceAlreadyExistsException;
import com.dev.backend.exceptions.ResourceNotFoundException;
import com.dev.backend.exceptions.SalesOrderException;

/**
 * Exception handler class for Rest endpoints in the application.
 * @author creddy
 *
 */
@ControllerAdvice
public class SalesAppAdvice {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorDetails processError(ResourceNotFoundException ex) {
		return new ErrorDetails(ex);
	}
	
	@ExceptionHandler(SalesOrderException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDetails processError(SalesOrderException ex) {
		return new ErrorDetails(ex);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDetails processError(ResourceAlreadyExistsException ex) {
		return new ErrorDetails(ex);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorDetails processError(RuntimeException ex) {
		return new ErrorDetails(ex);
	}

}
