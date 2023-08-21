package com.rental.nursing.exception;

public class EmployerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmployerNotFoundException(String message) {
		super(message);
	}
}
