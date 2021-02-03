package com.machine.vending.model.monitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.machine.vending.model.common.RegistryType;
import com.machine.vending.model.exception.DuplicateMachineException;
import com.machine.vending.model.exception.InsufficientFundsException;
import com.machine.vending.model.exception.InsufficientPaymentException;
import com.machine.vending.model.exception.InvalidParameterException;
import com.machine.vending.model.exception.InvalidRegistryException;
import com.machine.vending.model.exception.MachineNotFoundException;
import com.machine.vending.model.generic.AbstractCoinRegistry;
import com.machine.vending.model.generic.AbstractVendingBank;
import com.machine.vending.model.generic.AbstractVendingMachine;
import com.machine.vending.utils.GenericParameterCheck;

public abstract class AbstractMachineMonitor<R extends AbstractCoinRegistry, B extends AbstractVendingBank<R>, M extends AbstractVendingMachine<R,B>> implements MachineMonitorInterface<R,B,M> {
	
	private final Map<String, M> machines;
	
	protected AbstractMachineMonitor() {
		this.machines = new HashMap<String, M>();
	}
	
	protected abstract M createMachine(String name);
	
	public List<M> getMachines() {
		return this.machines.values().stream().collect(Collectors.toUnmodifiableList());
	}
	
	public M getMachine(String name) throws MachineNotFoundException {
		if(this.machines.containsKey(name)) {
			return machines.get(name);
		}
		throw new  MachineNotFoundException(name);
	}
   
	@Override
	public M addMachine(M machine) throws DuplicateMachineException, InvalidParameterException {
		GenericParameterCheck.check("machine", machine);
		if(this.machines.containsKey(machine.getName())) {
			throw new DuplicateMachineException(machine.getName());
		}
		this.machines.put(machine.getName(), machine);
		return this.machines.get(machine.getName());
	}
	
	@Override
	public M addMachine(String name, R registry) throws DuplicateMachineException, InvalidParameterException {
		GenericParameterCheck.check("name", name);
		GenericParameterCheck.check("registry", registry);
		M machine = this.createMachine(name);
		machine.setMachineRegistry(registry);
		return this.addMachine(machine);
	}

	@Override
	public M updateRegistry(String name, String type, R registry) throws InvalidParameterException, MachineNotFoundException, InvalidRegistryException {
		GenericParameterCheck.check("name",name);
		GenericParameterCheck.check("registry", registry);
		M machine = this.getMachine(name);
		RegistryType rType = RegistryType.getType(type);
		switch(rType) {
			case MACHINE:
				machine.setMachineRegistry(registry);
			break;
			case USER:
				machine.setUserRegistry(registry);
			break;
			case UNDEFINED:
				throw new InvalidRegistryException(name, type);
		}
		this.machines.put(name, machine);
		return machine;
	}
	
	public Double getTotalAmount(String machine_name) throws MachineNotFoundException, InvalidParameterException {
		GenericParameterCheck.check("machine_name", machine_name);
		return this.getMachine(machine_name).getTotalAmount();
	}

	@Override
	public R getMachineRegistry(String machine_name) throws MachineNotFoundException, InvalidParameterException {
		GenericParameterCheck.check("machine_name", machine_name);
		return this.getMachine(machine_name).getMachineRegistry();
	}

	@Override
	public R getUserRegistry(String machine_name) throws MachineNotFoundException, InvalidParameterException {
		GenericParameterCheck.check("machine_name", machine_name);
		return this.getMachine(machine_name).getUserRegistry();
	}

	@Override
	public void removeVendingMachine(String machine_name) throws MachineNotFoundException {
		if(this.machines.containsKey(machine_name)) {
			this.machines.remove(machine_name);
		}
		throw new MachineNotFoundException(machine_name);
	}

	@Override
	public R makePayment(String machine_name, Double amount, R payment) throws InvalidParameterException, MachineNotFoundException, InsufficientPaymentException, InsufficientFundsException {
		GenericParameterCheck.check("machine_name", machine_name);
		GenericParameterCheck.check("name", amount);
		GenericParameterCheck.check("payment", payment);
		if(amount == 0.0) {
			return payment;
		}
		return this.getMachine(machine_name).makePayment(amount, payment);
	}

	@Override
	public Integer noMachines() {
		return Integer.valueOf(this.machines.size());
	}

}
