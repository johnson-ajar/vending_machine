package com.machine.vending.app;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.machine.vending.model.CoinRegistry;
import com.machine.vending.model.exception.DuplicateMachineException;
import com.machine.vending.model.exception.InvalidParameterException;
import com.machine.vending.model.monitor.VendingMachineMonitor;

@Component
public class InitialiseVendingMachine {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@PostConstruct
	public void init() {
		LOGGER.info("Started Initializing vending machines.");
		//VendingMachineMonitor monitor = VendingMachineMonitor.getInstance();
		VendingMachineMonitor monitor = VendingMachineMonitor.getInstance();
		try {
			monitor.addMachine("machine1", CoinRegistry.getDefault());
			monitor.addMachine("machine2", CoinRegistry.getDefault());
		} catch (DuplicateMachineException | InvalidParameterException e) {
			LOGGER.error(String.format("Failed initialisation %s", e.getMessage()));
		}
		
		LOGGER.info("Finished Initializing the vending machines");
	}
}
