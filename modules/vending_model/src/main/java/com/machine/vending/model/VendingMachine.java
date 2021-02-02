package com.machine.vending.model;

import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.generic.AbstractVendingMachine;
import com.machine.vending.model.generic.CoinTypeFactory;

public class VendingMachine extends AbstractVendingMachine<CoinRegistry, VendingBank>{
	
	public VendingMachine(String name, CoinGroup group) {
		super(name, group);
	}
}
