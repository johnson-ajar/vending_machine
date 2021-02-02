package com.machine.vending.model.exception;

public class InvalidParameterException extends Exception {
	private static final long serialVersionUID = 1L;
	private final String message;
	
	public InvalidParameterException(String parameter, Object value) {
		this.message = String.format("Check parameter %s, was assigned value %s", parameter, value == null ? "null": value.toString());
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
