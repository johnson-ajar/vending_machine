package com.machine.vending.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.machine.vending.model.api.CoinRegistry;
import com.machine.vending.model.api.VendingBank;
import com.machine.vending.model.common.CoinGroup;
import com.machine.vending.model.common.CoinType;
import com.machine.vending.model.exception.InsufficientPaymentException;

public class VendingBankTest {
	private VendingBank bank;
	private static final CoinGroup grp = CoinGroup.UK;
	@BeforeEach
	public void setUp() {
		bank = new VendingBank(CoinGroup.UK);
	}
	
	@Test
	public void testDefault() throws Exception {
		assertEquals(bank.getMachineRegistry().getAmount(),100.0);
		assertEquals(bank.getUserRegistry().getAmount(), 0.0);
		
		assertEquals(bank.getTotalAmount(),100.0);
		assertEquals(bank.entity().getMachineRegistry().getAmount(),100.0);
		assertEquals(bank.entity().getUserRegistry().getAmount(), 0.0);
		assertEquals(bank.entity().getTotalAmount(), 100.0);
		
		CoinRegistry pay = CoinTypeFactory.createRegistry(CoinGroup.UK, true);
		pay.setCoins(CoinType.FIFTY_PENCE, 2);
		CoinRegistry change = bank.makePayment(1.0,pay);
		assertEquals(change.getAmount(), 0.0);
	}
	
	@Test
	public void testUpdateMachineRegistry() {
		CoinRegistry nRegistry = CoinTypeFactory.createRegistry(grp, true);
		assertEquals(bank.getMachineRegistry().getAmount(),100.0);
		bank.setMachineRegistry(nRegistry);
		assertEquals(bank.getMachineRegistry().getAmount(),0.0);
		
		assertEquals(bank.getUserRegistry().getAmount(),0.0);
		bank.setUserRegistry((CoinRegistry)bank.getUserRegistry().setCoins(CoinType.ONE_POUND, 1));
	}
	
	@Test
	public void testInsufficientFundsException() throws Exception {
		CoinRegistry registry = CoinTypeFactory.createRegistry(CoinGroup.UK, true);
		CoinRegistry payment = (CoinRegistry)registry.clone()
						.setCoins(CoinType.ONE_PENCE, 1)
						.setCoins(CoinType.TWO_PENCE, 1)
						.setCoins(CoinType.FIVE_PENCE, 1)
						.setCoins(CoinType.TEN_PENCE, 1)
						.setCoins(CoinType.TWENTY_PENCE,1)
						.setCoins(CoinType.FIFTY_PENCE,1)
						.setCoins(CoinType.ONE_POUND,1);
		
		Assertions.assertThrows(InsufficientPaymentException.class, ()->{
			bank.makePayment(1.89, registry);
		});
		Assertions.assertThrows(InsufficientPaymentException.class, ()->{
			bank.makePayment(1.89, payment);
		});
	}
	
	private static Stream<Arguments> providePaymentRegistry() {
		CoinRegistry registry = CoinTypeFactory.createRegistry(CoinGroup.UK, true);
		return Stream.of(
				Arguments.of(Double.valueOf(0),
						registry.clone(),
						registry.clone()
						),
				Arguments.of(Double.valueOf(1.88),
						registry.clone()
						.setCoins(CoinType.ONE_PENCE, 1)
						.setCoins(CoinType.TWO_PENCE, 1)
						.setCoins(CoinType.FIVE_PENCE, 1)
						.setCoins(CoinType.TEN_PENCE, 1)
						.setCoins(CoinType.TWENTY_PENCE,1)
						.setCoins(CoinType.FIFTY_PENCE,1)
						.setCoins(CoinType.ONE_POUND,1),
						registry.clone()
						),
				Arguments.of(Double.valueOf(1.88),
						registry.clone()
						.setCoins(CoinType.ONE_PENCE, 2)
						.setCoins(CoinType.TWO_PENCE, 2)
						.setCoins(CoinType.FIVE_PENCE, 2)
						.setCoins(CoinType.TEN_PENCE, 2)
						.setCoins(CoinType.TWENTY_PENCE,2)
						.setCoins(CoinType.FIFTY_PENCE,2)
						.setCoins(CoinType.ONE_POUND,2),
						registry.clone()
						.setCoins(CoinType.ONE_PENCE, 1)
						.setCoins(CoinType.TWO_PENCE, 1)
						.setCoins(CoinType.FIVE_PENCE, 1)
						.setCoins(CoinType.TEN_PENCE, 1)
						.setCoins(CoinType.TWENTY_PENCE,1)
						.setCoins(CoinType.FIFTY_PENCE,1)
						.setCoins(CoinType.ONE_POUND,1)
						)
				
				);
	}
	
	
	@ParameterizedTest
	@MethodSource("providePaymentRegistry")
	public void testPayment(Double amount, CoinRegistry payment, CoinRegistry change) throws Exception{
		assertTrue(bank.makePayment(amount, (CoinRegistry)payment).equals((CoinRegistry)change));
	}
	
	
}
