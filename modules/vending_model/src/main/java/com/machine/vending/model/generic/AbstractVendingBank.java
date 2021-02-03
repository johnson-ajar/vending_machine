package com.machine.vending.model.generic;

import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.common.EntityToModelAdapter;
import com.machine.vending.model.entity.VendingBankEntity;
import com.machine.vending.model.exception.InsufficientFundsException;
import com.machine.vending.model.exception.InsufficientPaymentException;

public abstract class AbstractVendingBank<R extends AbstractCoinRegistry> implements EntityToModelAdapter<AbstractVendingBank<R>,VendingBankEntity> {
	private R machineRegistry;
	
	private R userRegistry;
	
	public AbstractVendingBank(CoinGroup group) {
		this.userRegistry = this.createUserRegistry(group);
		this.machineRegistry = this.createMachineRegistry(group);
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
	
	public void setUserRegistry(R registry) {
		this.userRegistry = registry;
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
	
	public R calculateChange(Double change, R registry)  throws InsufficientFundsException {
		Integer changeValue = this.convertToPences(change);//Integer.valueOf((int) Math.round(change*100.0));
		return this.calculateChange(changeValue, registry);
	}
	
	public R calculateChange(Integer changeValue, R registry) throws InsufficientFundsException{
		R userChangeRegistry = this.createUserRegistry(this.machineRegistry.getGroup());
		Integer oldChange = changeValue;
		while(changeValue > 0) {
			for(CoinType type : userChangeRegistry.coinTypes()) {
				if(changeValue >= type.getValue() && this.getMachineRegistry().containCoins(type)) {
					changeValue = changeValue- type.getValue();
					userChangeRegistry.updateRegistry(type, 1);
					this.updateMachineRegistry(type, -1);
				}
			}
			if(oldChange == changeValue) { 
				//This becomes if there are not sufficient funds available
				//to pay the balance.
				//reset the machine registry before throwing the exception.
				for(CoinType type: userChangeRegistry.coinTypes()) {
					this.updateMachineRegistry(type, userChangeRegistry.getCoins(type));
				}
				throw new InsufficientFundsException(changeValue, registry);
				
			}
			oldChange = changeValue;
		}
		return userChangeRegistry;
	}
	
	public R makePayment(Double purchaseAmount, R registry) throws InsufficientPaymentException, InsufficientFundsException {
		//Check if the machine registry has the required amount to pay the change.
		Integer change = registry.totalPences() - convertToPences(purchaseAmount);
		if(machineRegistry.getAmount()<registry.getAmount()) {
			throw new InsufficientFundsException(change, registry);
		} else if(change < 0) {
			throw new InsufficientPaymentException(purchaseAmount, registry);
		}
		
		System.out.println("The user payment in pences "+registry.totalPences());
		System.out.println("Converted payment in pences "+convertToPences(purchaseAmount));
		System.out.println("The user should be provided change "+change);
		//Calculate change if no change is required update the user coin registry.
		if (change >= 0) {
			 R changeRegistry = calculateChange(Math.abs(change), registry);
			 //Update the user registry if the calculated change has been successful.
			 for(CoinType type : registry.coinTypes()) {
				this.updateUserRegistry(type, registry.getCoins(type));
			 }
			 return changeRegistry;
		}
		//At this point you can either return an empty change registry or 
		//user payed registry. This section of core will not be touched.
		return registry;
	}
	
	private Integer convertToPences(Double amount) {
		return Integer.valueOf((int)Math.round(amount*100));
	}
	
	protected abstract R createMachineRegistry(CoinGroup group);
	
	protected abstract R createUserRegistry(CoinGroup group);
	
}
