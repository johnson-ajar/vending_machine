package com.machine.vending.model.common;

public enum CoinType {
	ONE_POUND("POUND",100),
	FIFTY_PENCE("FIFTY", 50),
	TWENTY_PENCE("TWENTY",20),
	TEN_PENCE("TEN",10),
	FIVE_PENCE("FIVE",5),
	TWO_PENCE("TWO",2),
	ONE_PENCE("ONE",1),
	UNDEFINED("UNDEFINED",0);
	
	private final String name;
	private final int value;
	private CoinType(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public static CoinType coinTypeByName(String name) {
		for(CoinType type: values()) {
			if(type.getName().equals(name)) {
				return type;
			}
		}
		return CoinType.UNDEFINED;
	}
	
	public static CoinType coinTypeByValue(int value) {
		for(CoinType type: values()) {
			if(type.getValue() == value) {
				return type;
			}
		}
		return CoinType.UNDEFINED;
	}
}
