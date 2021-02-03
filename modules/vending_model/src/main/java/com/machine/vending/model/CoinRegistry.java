package com.machine.vending.model;

import java.util.Arrays;
import java.util.List;

import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.generic.AbstractCoinRegistry;

public class CoinRegistry extends AbstractCoinRegistry{
	private final CoinGroup group;
	
	public CoinRegistry(CoinGroup group) {
		this.group = group;
	}
	
	protected CoinRegistry(AbstractCoinRegistry registry) {
		super(registry);
		this.group = this.getGroup();
	}
	
	@Override
	protected List<CoinType> coinTypes() {
		//Maintain this order higher denomination to lowest denomination
		return Arrays.asList(
				CoinType.ONE_POUND,
				CoinType.FIFTY_PENCE,
				CoinType.TWENTY_PENCE,
				CoinType.TEN_PENCE,
				CoinType.FIVE_PENCE,
				CoinType.TWO_PENCE,
				CoinType.ONE_PENCE
				);
	}
	
	
	public static CoinRegistry getDefault(CoinGroup group) {
		CoinRegistry registry = new CoinRegistry(group);
		registry.setCoins(CoinType.ONE_POUND, 15);
		registry.setCoins(CoinType.FIFTY_PENCE, 60);
		registry.setCoins(CoinType.TWENTY_PENCE, 100);
		registry.setCoins(CoinType.TEN_PENCE, 100);
		registry.setCoins(CoinType.FIVE_PENCE, 200);
		registry.setCoins(CoinType.TWO_PENCE, 500);
		registry.setCoins(CoinType.ONE_PENCE, 500);
		return registry;
	}


	@Override
	protected CoinGroup getGroup() {
		return this.group;
	}
	
	@Override
	public final CoinRegistry clone() {
		AbstractCoinRegistry registry = super.clone();
		return new CoinRegistry(registry);
	}
}
