package kg.com.transactionservice.service.impl;

import jakarta.annotation.PostConstruct;
import kg.com.transactionservice.enums.CurrencyPair;
import kg.com.transactionservice.repository.CurrencyRateRepository;
import kg.com.transactionservice.service.CurrencyRatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRatesServiceImpl implements CurrencyRatesService {
	
	// Best if you save this data in .env file but it`s not that important there and this data open
	// https://twelvedata.com/docs#end-of-day-price
	private static final String url = "https://api.twelvedata.com/eod";
	private static final String api_key = "c8d4fe665a4d478fb6006f48cb8a4435";
	private final CurrencyRateRepository currencyRateRepository;
	private final OkHttpClient client = new OkHttpClient();
	
	//Run after application starts
	@PostConstruct
	private void initializeCurrencyRates() {
		setCurrencyRateFromExternalApi();
	}
	
	// Run every day at 12 PM
	@Scheduled(cron = "0 0 12 * * ?")
	public void setCurrencyRateFromExternalApi() {
		for (CurrencyPair value : CurrencyPair.values()) {
			String json = getCurrencyRateFromExternalApi(value.getSymbol());
			if (json != null) {
				// TODO map/parse to currencyRate and save to DB
				log.info("Successfully fetched currency rate for: {} json: {}", value.getSymbol(), json);
			} else {
				log.error("Failed to fetch currency rate for: {}", value.getSymbol());
			}
		}
	}
	
	private String getCurrencyRateFromExternalApi(String symbol) {
		Request request = new Request.Builder()
				.url(getUrl(symbol))
				.get()
				.addHeader("Accept", MediaType.APPLICATION_JSON.toString())
				.build();
		
		log.info("url: {}", getUrl(symbol));
		
		try (Response response = client.newCall(request).execute()) {
			if (! response.isSuccessful()) {
				throw new IOException("Unexpected response code " + response);
			}
			return response.body().string();
		} catch (IOException e) {
			return null;
		}
	}
	
	private String getUrl(String currencyPair) {
		return String.format("%s?symbol=%s&apikey=%s", url, currencyPair, api_key);
	}
}
