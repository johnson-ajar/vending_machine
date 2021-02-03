package com.machine.vending.model.entity;

public final class VendingBankEntity {
	private CoinRegistryEntity machineRegistry;
	
	private CoinRegistryEntity userRegistry;
	
	private Double totalAmount = 0.0;
	
	public VendingBankEntity() {
		machineRegistry = new CoinRegistryEntity();
		userRegistry = new CoinRegistryEntity();
	}
	
	public CoinRegistryEntity getMachineRegistry() {
		return this.machineRegistry;
	}
	
	public CoinRegistryEntity getUserRegistry() {
		return this.userRegistry;
	}
	
	public void setMachineRegistry(CoinRegistryEntity registry) {
		this.machineRegistry = registry;
	}
	
	public void setUserRegistry(CoinRegistryEntity registry) {
		this.userRegistry = registry;
	}
	
	public Double getTotalAmount() {
		return this.machineRegistry.getAmount()+this.userRegistry.getAmount();
	}
	
	public void setTotalAmount(Double amount) {
		this.totalAmount = amount;
	}
}
