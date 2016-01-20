package com.dev.backend.client.model;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDetails {

	private String message;
	private String stackTrace;

	/**
	 * Constructor.
	 * 
	 * @param th
	 */
	public ErrorDetails(Throwable th) {
		StringWriter traceWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(traceWriter);
		th.printStackTrace(writer);

		this.message = th.getMessage();
		this.stackTrace = traceWriter.toString();
	}

	ErrorDetails() {
	}

	public String getMessage() {
		return message;
	}

	void setMessage(String message) {
		this.message = message;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
}