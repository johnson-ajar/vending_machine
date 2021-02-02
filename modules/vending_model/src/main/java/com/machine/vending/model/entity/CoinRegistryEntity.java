package com.machine.vending.model.entity;

import java.util.HashMap;
import java.util.Map;

import com.machine.vending.model.common.CoinType;

public final class CoinRegistryEntity {
	Map<CoinType, Integer> coinStore = new HashMap<CoinType, Integer>();
	private Double amount = 0.0;
	
	public CoinRegistryEntity() {
		
	}
	
	public void setCoinRegistry(Map<CoinType, Integer> registry) {
		this.coinStore = registry;
	}
	
	public Map<CoinType, Integer> getCoinRegistry() {
		return this.coinStore;
	}
	
	public Double getAmount() {
		return this.amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(CoinType type : coinStore.keySet()) {
			builder.append(String.format("%s : %d,", type.getName(), coinStore.get(type)));
		}
		return builder.toString();
	}
}
