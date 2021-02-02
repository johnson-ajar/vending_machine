package com.machine.vending.model.exception;

public class MachineNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message = "";
	public MachineNotFoundException(String name) {
		this.message = String.format("Machine {%s} not found, please create one and add it to the monitor", name);
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
