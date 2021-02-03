package com.machine.vending.model.common;

public enum RegistryType {
	MACHINE("machine"),
	USER("user"),
	UNDEFINED("");
	
	private final String type;
	
	private RegistryType(String inType) {
		this.type = inType;
	}
	
	private String getType() {
		return this.type;
	}
	
	public static RegistryType getType(String inType) {
		for(RegistryType type : values()) {
			if(type.getType().equals(inType)) {
				return type;
			}
		}
		return UNDEFINED;
	}
}
