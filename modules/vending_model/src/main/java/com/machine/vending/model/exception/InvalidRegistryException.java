package com.machine.vending.model.exception;

public class InvalidRegistryException extends Exception {
	
	private String message = "";
	public InvalidRegistryException(String name, String type) {
		this.message = String.format("Machine %s cannot be updated with a invalid registry type %s. Use keyword {machine, user}", name, type);
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
