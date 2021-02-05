package com.machine.vending.api;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.machine.vending.model.CoinTypeFactory;
import com.machine.vending.model.api.CoinRegistry;
import com.machine.vending.model.api.VendingMachine;
import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.entity.CoinRegistryEntity;
import com.machine.vending.model.entity.VendingMachineEntity;
import com.machine.vending.model.exception.DuplicateMachineException;
import com.machine.vending.model.exception.InsufficientFundsException;
import com.machine.vending.model.exception.InsufficientPaymentException;
import com.machine.vending.model.exception.InvalidParameterException;
import com.machine.vending.model.exception.InvalidRegistryException;
import com.machine.vending.model.exception.MachineNotFoundException;
import com.machine.vending.model.monitor.VendingMachineMonitor;

@RestController
@RequestMapping("/vending")
public class VendingMachineChangeServiceImpl implements VendingMachineChangeService{
	
	private static final Logger LOG = LoggerFactory.getLogger(VendingMachineChangeServiceImpl.class);
	
	private final VendingMachineMonitor monitor = VendingMachineMonitor.getInstance();
	
	//TODO This can be changed based on user preference when the service need to support
	//other coin groups.
	private final CoinGroup useGroup = InitialiseVendingMachine.useGroup;
	
	@GetMapping(value="/machine/all", 
			produces="application/json")
	@Override
	public ResponseEntity<List<VendingMachineEntity>> getMachines() {
		LOG.info("Getting all the vending machine");
		List<VendingMachine> machines = monitor.getMachines();
		List<VendingMachineEntity> machineEntities = machines.stream().map(m->m.entity()).collect(Collectors.toList());
		return new ResponseEntity<List<VendingMachineEntity>>(machineEntities, HttpStatus.OK);
	}
	
	@PostMapping(value="/machine/{name}",
			produces="application/json")
	@Override
	public ResponseEntity<VendingMachineEntity> addVendingMachine(@PathVariable String name) {
		LOG.info("Adding a new vending machine to be monitor service");
		VendingMachine machine = new VendingMachine(name, useGroup);
		machine.setMachineRegistry(CoinRegistry.getDefault(useGroup));
		try {
			machine = monitor.addMachine(machine);
		} catch (DuplicateMachineException e) {
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.CONFLICT);
		} catch (InvalidParameterException e) {
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<VendingMachineEntity>(machine.entity(), HttpStatus.OK);
	}
	
	@PutMapping(value="/machine/{name}/registry",
			consumes="application/json")
	@Override
	public ResponseEntity<VendingMachineEntity> updateCoinRegistry(@PathVariable(required=true) String name, @RequestParam(required=true) String type, @RequestBody(required=true) CoinRegistryEntity registry) {
		LOG.info(String.format("Updating a vending machine %s coin registry %s ", name, type));
		try {
			//TODO: Use a factory method to create
			CoinRegistry ukRegistry = CoinTypeFactory.createRegistry(useGroup, true);
			ukRegistry.populate(registry);
			VendingMachine machine = monitor.updateRegistry(name, type.trim(), ukRegistry);
			return new ResponseEntity<VendingMachineEntity>(machine.entity(), HttpStatus.OK);
		} catch(InvalidParameterException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.BAD_REQUEST);
		} catch(MachineNotFoundException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.NOT_FOUND);
		} catch (InvalidRegistryException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value="/machine/{name}",
			produces = "application/json")
	@Override
	public ResponseEntity<VendingMachineEntity> getMachine(@PathVariable(required=true)String name) {
		LOG.info(String.format("Getting the vending machine %s", name));
		try {
			VendingMachine machine = monitor.getMachine(name);
			return new ResponseEntity<VendingMachineEntity>(machine.entity(), HttpStatus.OK);
		} catch(MachineNotFoundException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<VendingMachineEntity>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping(value="/machine/{name}/coins",
			produces = "application/json")
	@Override
	public ResponseEntity<CoinRegistryEntity> getMachineCoinRegistry(@PathVariable(required=true)  String name) {
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
	
	@GetMapping(value="/machine/{name}/user/coins",
			produces ="application/json")
	@Override
	public ResponseEntity<CoinRegistryEntity> getUserCoinRegistry(@PathVariable(required=true) String name) {
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
	
	@PutMapping(value="/machine/{name}/payment",
			consumes="application/json",
			produces="application/json")
	@Override
	public ResponseEntity<CoinRegistryEntity> submitPaymentAmount(@PathVariable(required=true) String name, @RequestParam(required=true)Double amount,
			@RequestBody(required=true) CoinRegistryEntity coins) {
		LOG.info(String.format("Making payment to the machine %s for the amount %f %s",name, amount, coins));
		try {
			CoinRegistry registry = CoinTypeFactory.createRegistry(useGroup, true);
			registry.populate(coins);
			CoinRegistry change = monitor.makePayment(name, amount, registry);
			return new ResponseEntity<CoinRegistryEntity>(change.entity(), HttpStatus.OK);
		} catch(MachineNotFoundException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(HttpStatus.NOT_FOUND);
		} catch (InvalidParameterException e) {
			LOG.error(String.format("%s", e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(HttpStatus.BAD_REQUEST);
		} catch (InsufficientPaymentException e) {
			LOG.error(String.format("Machine : {%s} %s",name, e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(coins, HttpStatus.BAD_REQUEST);
		} catch (InsufficientFundsException e) {
			LOG.error(String.format("Machine : {%s} %s",name, e.getMessage()));
			return new ResponseEntity<CoinRegistryEntity>(coins, HttpStatus.BAD_REQUEST);
		}
	}

}
