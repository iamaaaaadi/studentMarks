package com.assignment.marks.exception;

import org.springframework.http.HttpStatus;

public class MarksApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6126343442635539797L;
	private HttpStatus httpStatus;
	private String message; 

	public MarksApplicationException(HttpStatus httpStatus, String message) {
		super();
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
