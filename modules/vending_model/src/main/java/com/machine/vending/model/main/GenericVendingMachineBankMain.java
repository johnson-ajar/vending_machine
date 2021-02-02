package com.machine.vending.model.main;

import com.machine.vending.model.CoinRegistry;
import com.machine.vending.model.VendingBank;
import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.exception.InsufficientPaymentException;

public class GenericVendingMachineBankMain {
	public static void main(String[] args) {
		CoinRegistry registry = new CoinRegistry();
		registry.setCoins(CoinType.ONE_POUND, 0);
		registry.setCoins(CoinType.FIFTY_PENCE, 50);
		registry.setCoins(CoinType.TWENTY_PENCE, 100);
		registry.setCoins(CoinType.TEN_PENCE, 100);
		registry.setCoins(CoinType.FIVE_PENCE, 500);
		registry.setCoins(CoinType.TWO_PENCE, 1000);
		registry.setCoins(CoinType.ONE_PENCE, 2000);
		VendingBank bank = new VendingBank();
		bank.setMachineRegistry(registry);
		
		CoinRegistry payRegistry = new CoinRegistry();
		payRegistry.setCoins(CoinType.ONE_PENCE, 10);
		payRegistry.setCoins(CoinType.TWO_PENCE, 20);
		payRegistry.setCoins(CoinType.FIVE_PENCE, 20);
		payRegistry.setCoins(CoinType.TEN_PENCE, 200);
		payRegistry.setCoins(CoinType.TWENTY_PENCE, 10);
		payRegistry.setCoins(CoinType.FIFTY_PENCE, 6);
		payRegistry.setCoins(CoinType.ONE_POUND, 6);
		System.out.println("Before payment "+bank.getMachineRegistry());
		try {
			System.out.println(bank.makePayment(9.99,payRegistry));
		} catch (InsufficientPaymentException e) {
			e.printStackTrace();
		}
		System.out.println("After payment "+bank.getMachineRegistry());
		
		
		CoinRegistry payRegistry1 = new CoinRegistry();
		payRegistry1.setCoins(CoinType.ONE_PENCE, 0);
		payRegistry1.setCoins(CoinType.TWO_PENCE, 2);
		payRegistry1.setCoins(CoinType.FIVE_PENCE, 1);
		payRegistry1.setCoins(CoinType.TEN_PENCE, 0);
		payRegistry1.setCoins(CoinType.TWENTY_PENCE, 2);
		payRegistry1.setCoins(CoinType.FIFTY_PENCE, 1);
		payRegistry1.setCoins(CoinType.ONE_POUND, 9);
		System.out.println("Before payment "+bank.getMachineRegistry());
		try {
			System.out.println(bank.makePayment(9.99,payRegistry1));
		} catch (InsufficientPaymentException e) {
			e.printStackTrace();
		}
		System.out.println("After payment "+bank.getMachineRegistry());
	}
}
