package com.machine.vending.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.exception.InsufficientFundsException;
import com.machine.vending.model.generic.CoinTypeFactory;

public class VendingMachineTest {
	private VendingMachine machine = new VendingMachine("machine1", CoinGroup.UK);
	private CoinGroup cgp = CoinGroup.UK;
	
	@Test
	public void testSettingMachineRegistry() throws Exception {
		machine.setMachineRegistry(CoinTypeFactory.createRegistry(cgp, true));
		CoinRegistry payment = (CoinRegistry)CoinTypeFactory.createRegistry(cgp, true).clone()
				.setCoins(CoinType.ONE_PENCE, 1)
				.setCoins(CoinType.TWO_PENCE, 1)
				.setCoins(CoinType.FIVE_PENCE, 1)
				.setCoins(CoinType.TEN_PENCE, 1)
				.setCoins(CoinType.TWENTY_PENCE,1)
				.setCoins(CoinType.FIFTY_PENCE,1)
				.setCoins(CoinType.ONE_POUND,1);
		Assertions.assertThrows(InsufficientFundsException.class, ()->{
			CoinRegistry change = machine.makePayment(1.50, payment);
			System.out.println(change);
		});
		
		
		
	}
	
	@Test
	public void testPaymentInsufficientFunds() throws Exception {
		CoinRegistry machineRegistry = CoinTypeFactory.createRegistry(cgp, true);
		machineRegistry.setCoins(CoinType.TEN_PENCE, 500);
		machine.setMachineRegistry(machineRegistry);
		
		CoinRegistry payment = (CoinRegistry)CoinTypeFactory.createRegistry(cgp, true).clone()
				.setCoins(CoinType.ONE_PENCE, 5)
				.setCoins(CoinType.TWO_PENCE, 1)
				.setCoins(CoinType.FIVE_PENCE, 1)
				.setCoins(CoinType.TEN_PENCE, 1)
				.setCoins(CoinType.TWENTY_PENCE,1)
				.setCoins(CoinType.FIFTY_PENCE,1)
				.setCoins(CoinType.ONE_POUND,2);
		Assertions.assertThrows(InsufficientFundsException.class, ()->{
			CoinRegistry change = machine.makePayment(1.88, payment);
		});
	}
}
