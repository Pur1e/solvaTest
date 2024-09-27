package kg.com.transactionservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrencyPair {
	
	KZTUSD("USD/KZT"),
	RUBUSD("USD/RUB");
	
	private final String symbol;
	
}
