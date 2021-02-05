package com.machine.vending.model.exception;

import com.machine.vending.model.AbstractCoinRegistry;

public class InsufficientPaymentException  extends Exception {
	private static final long serialVersionUID = 1L;
	private String message = "";
	
	//TODO: Localise this message based on coin group.
	public InsufficientPaymentException(Double amount, AbstractCoinRegistry registry) {
		this.message = String.format("Machine has been provided insufficient payment %s require %s",
						registry.getAmount().toString(), amount.toString());
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
	
}
