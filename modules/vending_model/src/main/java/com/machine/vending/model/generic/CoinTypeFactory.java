package com.machine.vending.model.generic;

import com.machine.vending.model.CoinRegistry;
import com.machine.vending.model.VendingBank;
import com.machine.vending.model.common.CoinGroup;

public class CoinTypeFactory {
	
	public static <R extends AbstractCoinRegistry> R createRegistry(CoinGroup group, boolean isEmpty) {
		switch(group) {
			case UK:
				return (R)(isEmpty?new CoinRegistry(CoinGroup.UK):CoinRegistry.getDefault(CoinGroup.UK));
			case US:
				return null;
		}
		//TODO throw an exception.
		return null;
	}
	
	public static <R extends AbstractCoinRegistry, B extends AbstractVendingBank<R>> B createVendingBank(CoinGroup group) {
		switch(group) {
			case UK:
				return (B) new VendingBank(group);
			case US:
				return null;
		}
		return null;
	}
	
}
