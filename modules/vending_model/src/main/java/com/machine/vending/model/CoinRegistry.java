package com.machine.vending.model;

import java.util.Arrays;
import java.util.List;

import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.generic.AbstractCoinRegistry;

public class CoinRegistry extends AbstractCoinRegistry{

	@Override
	protected List<CoinType> coinTypes() {
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
	
	
	public static CoinRegistry getDefault() {
		CoinRegistry registry = new CoinRegistry();
		registry.setCoins(CoinType.ONE_POUND, 100);
		registry.setCoins(CoinType.FIFTY_PENCE, 50);
		registry.setCoins(CoinType.TWENTY_PENCE, 100);
		registry.setCoins(CoinType.TEN_PENCE, 100);
		registry.setCoins(CoinType.FIVE_PENCE, 500);
		registry.setCoins(CoinType.TWO_PENCE, 1000);
		registry.setCoins(CoinType.ONE_PENCE, 2000);
		return registry;
	}
}
