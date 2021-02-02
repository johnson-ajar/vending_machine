package com.machine.vending.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import com.machine.vending.model.common.CoinType;


public class CoinTypeConverter  implements Converter<String, CoinType> {

	@Override
	public CoinType convert(String source) {
		return CoinType.coinTypeByName(source);
	}

}
