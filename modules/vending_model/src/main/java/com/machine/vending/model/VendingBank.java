package com.machine.vending.model;

import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.generic.AbstractVendingBank;
import com.machine.vending.model.generic.CoinTypeFactory;

public class VendingBank extends AbstractVendingBank<CoinRegistry>{

	@Override
	protected CoinRegistry createMachineRegistry() {
		return CoinTypeFactory.createRegistry(getCoinGroup());
	}

	@Override
	protected CoinRegistry createUserRegistry() {
		return CoinTypeFactory.createRegistry(getCoinGroup());
	}

	@Override
	protected CoinGroup getCoinGroup() {
		return CoinGroup.UK;
	}

}
