package com.machine.vending.model.monitor;

import com.machine.vending.model.CoinRegistry;
import com.machine.vending.model.VendingBank;
import com.machine.vending.model.VendingMachine;
import com.machine.vending.model.common.CoinGroup;

public class VendingMachineMonitor extends AbstractMachineMonitor<CoinRegistry, VendingBank, VendingMachine>{
	
	
	private static VendingMachineMonitor instance = null;
	
	private VendingMachineMonitor() {
		super();
	}
	
	public static VendingMachineMonitor getInstance() {
		if(instance == null) {
			instance = new VendingMachineMonitor();
		}
		return instance;
	}
	
	@Override
	protected VendingMachine createMachine(String name) {
		return new VendingMachine(name, CoinGroup.UK);
	}

}
