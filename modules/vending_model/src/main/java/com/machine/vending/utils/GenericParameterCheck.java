package com.machine.vending.utils;

import com.machine.vending.model.AbstractCoinRegistry;
import com.machine.vending.model.AbstractVendingMachine;
import com.machine.vending.model.exception.InvalidParameterException;

public class GenericParameterCheck {
	public static void check(String parameter, String value) throws InvalidParameterException {
		if(value == null || value.length() == 0) {
			throw new InvalidParameterException(parameter, value);
		}
	}
	
	public static <M extends AbstractVendingMachine<?,?>> void check(String parameter, M machine) throws InvalidParameterException {
		if(machine == null) {
			throw new InvalidParameterException(parameter, machine);
		}
	}
	
	public static  void check(String parameter, AbstractCoinRegistry registry) throws InvalidParameterException {
		if(registry == null) {
			throw new InvalidParameterException(parameter, registry);
		}
	}
	
	public static void check(String parameter, double amount) throws InvalidParameterException{
		if(amount < 0.0) {
			throw new InvalidParameterException(parameter, Double.toString(amount));
		}
	}
}
