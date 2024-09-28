package kg.com.transactionservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import kg.com.transactionservice.enums.CurrencyAndPair;
import kg.com.transactionservice.model.CurrencyRate;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
	private final ObjectMapper mapper;
	
	//Run after application starts
	@PostConstruct
	private void initializeCurrencyRates() {
		setCurrencyRateFromExternalApi();
		log.info("CurrencyRatesServiceImpl initialized");
	}
	
	// Run every day at 12:00 AM
	@Scheduled(cron = "0 0 0 * * ?")
	public void setCurrencyRateFromExternalApi() {
		for (CurrencyAndPair pair : CurrencyAndPair.values()) {
			String json = getCurrencyRateFromExternalApi(pair.getPair());
			if (json == null) {
				log.error("Failed to fetch currency rate for: {}", pair.getPair());
				return;
			}
			try {
				JsonNode node = mapper.readTree(json);
				BigDecimal rate = new BigDecimal(node.path("close").asText());
				OffsetDateTime rateDate = Instant.ofEpochSecond(
						node.path("timestamp").asLong()).atOffset(ZoneOffset.ofHours(6));
				
				CurrencyRate currencyRate = CurrencyRate.builder()
						.rate(rate)
						.rateDate(rateDate).currencyPair(pair.getPair()).createdAt(LocalDateTime.now())
						.build();
				
				currencyRateRepository.save(currencyRate);
				log.info("Successfully saved currency rate for: {}", pair.getPair());
			} catch (Exception e) {
				log.error("Error processing currency rate for {}: {}", pair.getPair(), e.getMessage());
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
	
	protected CurrencyRate getActualCurrencyRate(String currencyShortname) {
		String pair = CurrencyAndPair.findByCurrency(currencyShortname).getPair();
		
		if (pair == null) {
			log.error("Invalid currency: {}", currencyShortname);
			throw new IllegalArgumentException("Invalid currency: " + currencyShortname);
		}
		
		return currencyRateRepository.findByCurrencyPair(pair).orElse(null);
	}
}
