package com.machine.vending.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.common.EntityToModelAdapter;
import com.machine.vending.model.entity.CoinRegistryEntity;

public abstract class AbstractCoinRegistry implements EntityToModelAdapter<AbstractCoinRegistry, CoinRegistryEntity>, Cloneable {
	
	private Map<CoinType, Integer> coinStore = new HashMap<CoinType, Integer>();
	private Double amount  = 0.0;
	public AbstractCoinRegistry() {
		coinTypes().stream().forEach(type -> setCoins(type, 0));
	}
	
	//Replace the registry with the passed registry content.
	protected AbstractCoinRegistry(AbstractCoinRegistry registry) {
		coinTypes().stream().forEach(type -> setCoins(type, registry.getCoins(type)));
		this.getAmount();
	}
	
	protected abstract List<CoinType> coinTypes();
	
	protected abstract CoinGroup getGroup();
	
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
	
	public AbstractCoinRegistry setCoins(CoinType type, Integer noCoins) {
		this.coinStore.put(type, noCoins);
		this.getAmount();
		return this;
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
		final CoinGroup grp = this.getGroup();
		AbstractCoinRegistry copy = new AbstractCoinRegistry(this) {

			@Override
			public List<CoinType> coinTypes() {
				return types;
			}

			@Override
			protected CoinGroup getGroup() {
				return grp;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((coinStore == null) ? 0 : coinStore.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractCoinRegistry other = (AbstractCoinRegistry) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (coinStore == null) {
			if (other.coinStore != null)
				return false;
		} else if (!coinStore.equals(other.coinStore))
			return false;
		return true;
	}


}
