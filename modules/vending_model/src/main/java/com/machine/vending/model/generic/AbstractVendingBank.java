package com.machine.vending.model.generic;

import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.common.EntityToModelAdapter;
import com.machine.vending.model.entity.VendingBankEntity;
import com.machine.vending.model.exception.InsufficientPaymentException;

public abstract class AbstractVendingBank<R extends AbstractCoinRegistry> implements EntityToModelAdapter<AbstractVendingBank<R>,VendingBankEntity> {
	private R machineRegistry;
	
	private R userRegistry;
	
	public AbstractVendingBank() {
		this.userRegistry = this.createUserRegistry();
		this.machineRegistry = this.createMachineRegistry();
	}
	
	public AbstractVendingBank(R registry) {
		this.machineRegistry = registry;
	}
	
	@Override
	public VendingBankEntity entity() {
		VendingBankEntity entity = new VendingBankEntity();
		entity.setMachineRegistry(machineRegistry.entity());
		entity.setUserRegistry(userRegistry.entity());
		return entity;
	}

	@Override
	public AbstractVendingBank<R> populate(VendingBankEntity entity) {
		this.machineRegistry.populate(entity.getMachineRegistry());
		this.userRegistry.populate(entity.getUserRegistry());
		return this;
	}

	public Double getTotalAmount() {
		return Double.valueOf(this.machineRegistry.getAmount()
				+ this.userRegistry.getAmount());
	}
	
	public R getMachineRegistry() {
		return this.machineRegistry;
	}
	
	public void setMachineRegistry(R registry) {
		this.machineRegistry = registry;
	} 
	
	public R getUserRegistry() {
		return this.userRegistry;
	}
	
	//Update user registry coin by the specified amount.
	public void updateUserRegistry(CoinType type, Integer amount) {
		getUserRegistry().updateRegistry(type, amount);
	}
	
	public void updateMachineRegistry(CoinType type, Integer amount) {
		this.getMachineRegistry().updateRegistry(type, amount);
	}
	
	public R calculateChange(Double change) {
		Integer changeValue = this.convertToPences(change);//Integer.valueOf((int) Math.round(change*100.0));
		return this.calculateChange(changeValue);
	}
	
	public R calculateChange(Integer changeValue) {
		R userChangeRegistry = this.createUserRegistry();
		while(changeValue > 0) {
			for(CoinType type : userChangeRegistry.coinTypes()) {
				if(changeValue >= type.getValue() && this.getMachineRegistry().containCoins(type)) {
					changeValue = changeValue- type.getValue();
					userChangeRegistry.updateRegistry(type, 1);
					this.updateMachineRegistry(type, -1);
				}
			}
		}
		return userChangeRegistry;
	}
	
	public R makePayment(Double amount, R registry) throws InsufficientPaymentException {
		for(CoinType type : registry.coinTypes()) {
			this.updateUserRegistry(type, registry.getCoins(type));
		}
		System.out.println("The user payment in pences "+registry.totalPences());
		System.out.println("Converted payment in pences "+convertToPences(amount));
		Integer change = convertToPences(amount) - registry.totalPences();
		System.out.println("The user should be provided change "+change);
		if (change <= 0) {
			return calculateChange(Math.abs(change));
		}
		throw new InsufficientPaymentException(amount, registry);
	}
	
	private Integer convertToPences(Double amount) {
		return Integer.valueOf((int)Math.round(amount*100));
	}
	
	protected abstract R createMachineRegistry();
	
	protected abstract R createUserRegistry();
	
	protected abstract CoinGroup getCoinGroup();

}
