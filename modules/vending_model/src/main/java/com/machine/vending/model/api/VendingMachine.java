package com.machine.vending.model.api;

import com.machine.vending.model.AbstractVendingMachine;
import com.machine.vending.model.common.CoinGroup;

public class VendingMachine extends AbstractVendingMachine<CoinRegistry, VendingBank>{
	
	public VendingMachine(String name, CoinGroup group) {
		super(name, group);
	}
}
