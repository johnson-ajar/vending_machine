package com.machine.vending.model;

import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.common.EntityToModelAdapter;
import com.machine.vending.model.entity.VendingMachineEntity;
import com.machine.vending.model.exception.InsufficientFundsException;
import com.machine.vending.model.exception.InsufficientPaymentException;

public abstract class AbstractVendingMachine<R extends AbstractCoinRegistry, B extends AbstractVendingBank<R>> implements EntityToModelAdapter<AbstractVendingMachine<R,B>, VendingMachineEntity> {
	private String name;
	
	private final B bank;
	
	public AbstractVendingMachine(String name, CoinGroup group) {
		this.name = name;
		this.bank = CoinTypeFactory.createVendingBank(group);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setMachineRegistry(R coins) {
		bank.setMachineRegistry(coins);
	}
	
	public void setUserRegistry(R coins) {
		bank.setUserRegistry(coins);
	}
	
	public R getMachineRegistry() {
		return bank.getMachineRegistry();
	}
	
	public Double getTotalAmount() {
		return bank.getTotalAmount();
	}
	
	public R getUserRegistry() {
		return bank.getUserRegistry();
	}
	
	public R makePayment(Double amount, R registry) throws InsufficientPaymentException, InsufficientFundsException {
		return bank.makePayment(amount, registry);
	}
	
	@Override
	public VendingMachineEntity entity() {
		VendingMachineEntity entity = new VendingMachineEntity();
		entity.setName(this.name);
		entity.setBank(this.bank.entity());
		return entity;
	}

	@Override
	public AbstractVendingMachine<R, B> populate(VendingMachineEntity entity) {
		this.name = entity.getName();
		this.bank.populate(entity.getBank());
		return this;
	}
	
}
