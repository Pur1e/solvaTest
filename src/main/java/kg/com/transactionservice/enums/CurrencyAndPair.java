package kg.com.transactionservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrencyAndPair {
	
	KZTUSD("KZT", "USD/KZT"),
	RUBUSD("RUB", "USD/RUB");
	
	private final String currency;
	private final String pair;
	
	public static CurrencyAndPair findByCurrency(String currency) {
		for (CurrencyAndPair value : values()) {
			if (value.getCurrency().equalsIgnoreCase(currency)) {
				return value;
			}
		}
		return null;
	}
	
	public static CurrencyAndPair findByPair(String pair) {
		for (CurrencyAndPair value : values()) {
			if (value.getPair().equalsIgnoreCase(pair)) {
				return value;
			}
		}
		return null;
	}
}
