package com.machine.vending.app;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.machine.vending.api.VendingMachineChangeService;
import com.machine.vending.model.CoinRegistry;
import com.machine.vending.model.VendingMachine;
import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.entity.CoinRegistryEntity;
import com.machine.vending.model.entity.VendingMachineEntity;
import com.machine.vending.model.exception.DuplicateMachineException;
import com.machine.vending.model.exception.InsufficientPaymentException;
import com.machine.vending.model.exception.InvalidParameterException;
import com.machine.vending.model.exception.MachineNotFoundException;
import com.machine.vending.model.generic.CoinTypeFactory;
import com.machine.vending.model.monitor.VendingMachineMonitor;

@RestController
public class VendingMachineServiceImpl implements VendingMachineChangeService{
	
	private static final Logger LOG = LoggerFactory.getLogger(VendingMachineServiceImpl.class);
	
	private final VendingMachineMonitor monitor = VendingMachineMonitor.getInstance();
	
	//TODO This can be changed based on user preference when the service need to support
	//other coin groups.
	private final CoinGroup useGroup = CoinGroup.UK;
	
	@Override
	public ResponseEntity<List<VendingMachineEntity>> getMachines() {
		LOG.info("Getting all the vending machine");
		List<VendingMachine> machines = monitor.getMachines();
		List<VendingMachineEntity> machineEntities = machines.stream().map(m->m.entity()).collect(Collectors.toList());
		return new ResponseEntity<List<VendingMachineEntity>>(machineEntities, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<VendingMachineEntity> addVendingMachine(String name) {
		LOG.info("Adding a new vending machine to be monitor service");
		VendingMachine machine = new VendingMachine(name, useGroup);
		machine.setMachineRegistry(CoinRegistry.getDefault());
		try {
			machine = monitor.addMachine(machine);
		} catch (DuplicateMachineException e) {
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.CONFLICT);
		} catch (InvalidParameterException e) {
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<VendingMachineEntity>(machine.entity(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<VendingMachineEntity> updateMachineCoinRegistry(String name, CoinRegistryEntity registry) {
		LOG.info("Updating a vending machine coin registry");
		try {
			//TODO: Use a factory method to create
			CoinRegistry ukRegistry = CoinTypeFactory.createRegistry(useGroup);
			ukRegistry.populate(registry);
			VendingMachine machine = monitor.updateMachineCoinRegistry(name, ukRegistry);
			return new ResponseEntity<VendingMachineEntity>(machine.entity(), HttpStatus.OK);
		} catch(InvalidParameterException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.BAD_REQUEST);
		} catch(MachineNotFoundException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<VendingMachineEntity> getMachine(String name) {
		LOG.info(String.format("Getting the vending machine %s", name));
		try {
			VendingMachine machine = monitor.getMachine(name);
			return new ResponseEntity<VendingMachineEntity>(machine.entity(), HttpStatus.OK);
		} catch(MachineNotFoundException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.NOT_FOUND);
		}
		
	}

	@Override
	public ResponseEntity<CoinRegistryEntity> getMachineCoinRegistry(String name) {
		LOG.info(String.format("Getting the coin registry for machine %s", name));
		try {
			CoinRegistry registry = monitor.getMachineRegistry(name);
			return new ResponseEntity<CoinRegistryEntity>(registry.entity(), HttpStatus.OK);
		} catch(MachineNotFoundException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(HttpStatus.NOT_FOUND);
		} catch (InvalidParameterException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<CoinRegistryEntity> getUserCoinRegistry(String name) {
		LOG.info(String.format("Getting the user coin registry for machine %s ", name));
		try {
			CoinRegistry registry = monitor.getUserRegistry(name);
			return new ResponseEntity<CoinRegistryEntity>(registry.entity(), HttpStatus.OK);
		} catch(MachineNotFoundException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(HttpStatus.NOT_FOUND);
		} catch(InvalidParameterException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<CoinRegistryEntity> submitPaymentAmount(String name, Double amount,
			CoinRegistryEntity coins) {
		LOG.info(String.format("Making payment to the machine %s for the amount %f %s",name, amount, coins));
		try {
			System.out.println(coins);
			CoinRegistry registry = CoinTypeFactory.createRegistry(useGroup);
			registry.populate(coins);
			System.out.println("User payment "+registry);
			CoinRegistry change = monitor.makePayment(name, amount, registry);
			System.out.println("User Change "+change);
			return new ResponseEntity<CoinRegistryEntity>(change.entity(), HttpStatus.OK);
		} catch(MachineNotFoundException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(HttpStatus.NOT_FOUND);
		} catch (InvalidParameterException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(HttpStatus.BAD_REQUEST);
		} catch (InsufficientPaymentException e) {
			LOG.error(String.format("Machine : {%s} %s",name, e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(HttpStatus.BAD_REQUEST);
		}
	}

}
