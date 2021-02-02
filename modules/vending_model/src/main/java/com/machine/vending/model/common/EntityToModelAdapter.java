package com.machine.vending.model.common;

public interface EntityToModelAdapter<M,E> {
	public E entity();
	public M populate(E entity);
}
