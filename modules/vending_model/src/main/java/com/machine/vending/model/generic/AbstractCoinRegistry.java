package com.machine.vending.model.generic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;

import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.common.EntityToModelAdapter;
import com.machine.vending.model.entity.CoinRegistryEntity;

public abstract class AbstractCoinRegistry implements EntityToModelAdapter<AbstractCoinRegistry, CoinRegistryEntity> {
	private Map<CoinType, Integer> coinStore = new HashMap<CoinType, Integer>();
	private Double amount  = 0.0;
	public AbstractCoinRegistry() {
		coinTypes().stream().forEach(type -> setCoins(type, 0));
	}
	
	private AbstractCoinRegistry(AbstractCoinRegistry registry) {
		coinTypes().stream().forEach(type -> setCoins(type, 0));
		this.getAmount();
	}
	
	protected abstract List<CoinType> coinTypes();
	
	@Override
	public CoinRegistryEntity entity() {
		CoinRegistryEntity entity = new CoinRegistryEntity();
		entity.setAmount(getAmount());
		entity.setCoinRegistry(coinStore);
		return entity;
	}

	@Override
	public AbstractCoinRegistry populate(CoinRegistryEntity entity) {
		System.out.println("Setting payment entity "+entity);
		this.amount = entity.getAmount();
		this.coinStore = entity.getCoinRegistry();
		return this;
	}

	
	public void updateRegistry(CoinType type, Integer amount) {
		this.coinStore.put(type, this.getCoins(type)+amount);
		this.getAmount();
	}
	
	public void setCoins(CoinType type, Integer noCoins) {
		this.coinStore.put(type, noCoins);
	}
	
	public Integer getCoins(CoinType type) {
		return this.coinStore.get(type);
	}
	
	public Integer totalPences() {
		return Integer.valueOf((int)Math.round(getAmount()*100.0));
	}
	
	public Map<CoinType, Integer> getCoinRegistry() {
		return this.coinStore;
	}
	
	public boolean containCoins(CoinType type) {
		if(this.coinStore.containsKey(type)) {
			return getCoins(type)>0;
		}
		return false;
	}
	
	
	public Double getAmount() {
		this.amount = 0.0;
		for(CoinType type : coinTypes()) {
			Integer coins = getCoins(type);
			amount+=(type.getValue()* (coins == null ? 0 : coins));
		}
		return this.amount/100.0;
	}
	
	@Override
	public AbstractCoinRegistry clone() {
		final List<CoinType> types = this.coinTypes();
		AbstractCoinRegistry copy = new AbstractCoinRegistry(this) {

			@Override
			public List<CoinType> coinTypes() {
				return types;
			}

		};
		return copy;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(CoinType type : coinTypes()) {
			builder.append(String.format("%s : %d, ", type.getName(), coinStore.get(type)));
		}
		return builder.toString();
	}
	
}
