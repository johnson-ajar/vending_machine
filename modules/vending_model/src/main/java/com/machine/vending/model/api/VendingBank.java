package com.machine.vending.model.api;

import com.machine.vending.model.AbstractVendingBank;
import com.machine.vending.model.CoinTypeFactory;
import com.machine.vending.model.common.CoinGroup;
/**
 * The Vending Bank machine registry is always default with 100£ worth of
 * coins at different denomination. This can be reset
 * **/
public class VendingBank extends AbstractVendingBank<CoinRegistry>{
	
	public VendingBank(CoinGroup inGroup) {
		super(inGroup);
	}
	
	public VendingBank(CoinRegistry registry) {
		super(registry.getGroup());
		this.setMachineRegistry(registry);
	}
	
	@Override
	protected CoinRegistry createMachineRegistry(CoinGroup group) {
		return CoinTypeFactory.createRegistry(group, false);
	}

	@Override
	protected CoinRegistry createUserRegistry(CoinGroup group) {
		return CoinTypeFactory.createRegistry(group, true);
	}
}
