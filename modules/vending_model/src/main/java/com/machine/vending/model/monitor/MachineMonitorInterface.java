package com.machine.vending.model.monitor;

import java.util.List;

import com.machine.vending.model.exception.DuplicateMachineException;
import com.machine.vending.model.exception.InsufficientPaymentException;
import com.machine.vending.model.exception.InvalidParameterException;
import com.machine.vending.model.exception.MachineNotFoundException;
import com.machine.vending.model.generic.AbstractCoinRegistry;
import com.machine.vending.model.generic.AbstractVendingBank;
import com.machine.vending.model.generic.AbstractVendingMachine;

public interface MachineMonitorInterface<R extends AbstractCoinRegistry, B extends AbstractVendingBank<R>, M extends AbstractVendingMachine<R,B>> {
	
	/**
	 * Use this method to get the list of vending machines currently handled by the api.
	 * @return List<VendingMachine>
	 * */
	public List<M> getMachines();
	
	/**
	 * Use this method to get vending name by it name.
	 * @param name {@code name} Name of the machine
	 * @throws MachineNotFoundException if the machine is not found.
	 * @return {@code AbstractVendingMachine}
	 * @see com.machine.vending.model.BaseVendingMachine if the {@code }
	 **/
	public M getMachine(String name) throws MachineNotFoundException;
	
	
	/**
	 * Use this method to add a new machine instance to the api.
	 * @param machine {@code BaseVendingMachine} to be added.
	 * @throws DuplicateMachineException If the machine is already present.
	 * @throws InvalidParameterException If the provided parameter machine is null.
	 * @return {@code AbstractVendingMachine}
	 * */
	public M addMachine(M machine) throws DuplicateMachineException, InvalidParameterException;
	
	/**
	 * Use this method to add a new vending machine, with a specified coin registry state.
	 * @param name The name of the machine to be added.
	 * @param registry The coin registry used to initialise the machine when it is added
	 * @throws DuplicateMachineException If the provided machine is already present.
	 * @throws InvalidParameterException If the provided parameter name and registry is invalid or null.
	 * @return {@code AbstractVendingMachine}
	 * */
	public M addMachine(String name, R registry) throws DuplicateMachineException, InvalidParameterException;
	
	/**
	 * Use this method to update the machine coin registry with a new coin registry.
	 * @param name The name of the machine to be updated.
	 * @param registry The coin registry to be added to the vending machine which is already available
	 * @return VendingMachine returns the vending machine with the updated coin registry.
	 * @throws MachineNotFoundException If the provided machine name is not found.
	 * @throws InvalidParameterException If the provided parameter name and registry is invalid or null.
	 * */
	public M updateMachineCoinRegistry(String name, R registry) throws InvalidParameterException, MachineNotFoundException;
	
	
	/**
	 * Use this method to get the total amount available in a vending machine.
	 * The total amount is calculated using the coins in the machine and user coin registry.
	 * @param machine_name The name of the machine whose amount is required
	 * @return double returns the amount as a double value.
	 * @throws MachineNotFoundException If the provided vending machine is not available.
	 * @throws InvalidParameterException If the provided parameter machine_name is invalid or null 
	 * **/
	public Double getTotalAmount(String machine_name) throws MachineNotFoundException, InvalidParameterException;
	
	
	/**
	 * Use this method to get the vending machine coin registry. It doesn't include
	 * the user coin registry.
	 * @param machine_name The name of the vending machine whose coin registry is required.
	 * @return {@CoinRegistry} It returns a coin registry object which provide details about no of coins by type.
	 * @throws MachineNotFoundException If the provided vending machine is not available.
	 * @throws InvalidParameterException If the provided parameter machine_name is invalid or null.
	 * */
	public R getMachineRegistry(String machine_name) throws MachineNotFoundException, InvalidParameterException;
	
	
	/**
	 * Use this method to get the user coin registry in a vending machine.
	 * @param machine_name The name of the vending machine whose user coin registry is required.
	 * @return {@ CoinRegistry} it returns a coin registry object which provide details about no of coins by type.
	 * @throws MachineNotFoundException If the provided vending machine name is not available 
	 * @throws InvalidParameterException Is thrown if the provided machine_name is invalid or null.
	 * */
	public R getUserRegistry(String machine_name) throws MachineNotFoundException, InvalidParameterException;
	
	/**
	 * Use this method to get the user coin registry in a vending machine
	 * @param machine_name The name of the vending machine to be removed.
	 * @throws MachineNotFoundException if the provided machine_name is not available.
	 * */
	public void removeVendingMachine(String machine_name) throws MachineNotFoundException;
	
	
	public R makePayment(String machine_name, Double amount, R payment) throws InvalidParameterException, MachineNotFoundException, InsufficientPaymentException;
	
	/**
	 * Use this method to get the number of vending machine being monitored by this api.
	 * @return int number of vending machines.
	 * */
	public Integer noMachines();
		
}
