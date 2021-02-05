package com.machine.vending.model;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;

import com.machine.vending.model.api.CoinRegistry;
import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.entity.CoinRegistryEntity;

public class CoinRegistryTest {
	
	@Test
	public void testDefault() {
		CoinRegistry registry = CoinRegistry.getDefault(CoinGroup.UK);
		assertEquals(registry.getAmount(),100.0);
		assertEquals(registry.getCoins(CoinType.ONE_POUND), 15);
		assertEquals(registry.getCoins(CoinType.FIFTY_PENCE), 60);
		assertEquals(registry.getCoins(CoinType.TWENTY_PENCE), 100);
		assertEquals(registry.getCoins(CoinType.TEN_PENCE), 100);
		assertEquals(registry.getCoins(CoinType.FIVE_PENCE),200);
		assertEquals(registry.getCoins(CoinType.TWO_PENCE), 500);
		assertEquals(registry.getCoins(CoinType.ONE_PENCE),500);
		
		assertEquals(registry.getCoinRegistry().size(), 7);
		
		CoinRegistryEntity entity = registry.entity();
		assertEquals(entity.getAmount(), registry.getAmount());
		assertEquals(entity.getCoinRegistry().size(), registry.getCoinRegistry().size());
		
		entity.getCoinRegistry().keySet().stream().forEach(k -> assertEquals(entity.getCoinRegistry().get(k), registry.getCoins(k)));
	}
}
