package com.machine.vending.model.exception;

import com.machine.vending.model.AbstractCoinRegistry;

public class InsufficientFundsException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message = "";
	
	//TODO: Localise this message based on coin group.
	public InsufficientFundsException(Integer change, AbstractCoinRegistry registry) {
		this.message = String.format("Machine has insufficient funds for change %s,"
				+ " returing the payment %s. Re-purchase and provide correct change.",
						Double.valueOf(change/100.0).toString(), registry.getAmount());
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
	
}
