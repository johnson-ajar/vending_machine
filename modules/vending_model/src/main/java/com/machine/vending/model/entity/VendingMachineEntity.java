package com.machine.vending.model.entity;

public final class VendingMachineEntity {
	private String name;
	
	private VendingBankEntity bank;
	
	public VendingMachineEntity() {
		bank = new VendingBankEntity();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setBank(VendingBankEntity bank) {
		this.bank = bank;
	}
	
	public VendingBankEntity getBank() {
		return this.bank;
	}
}
