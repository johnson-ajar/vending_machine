package com.machine.vending.model.exception;

public class DuplicateMachineException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message = "";
	public DuplicateMachineException(String name) {
		this.message = String.format("Machine by the name {%s} is already present, please create one with a different name", name);
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
