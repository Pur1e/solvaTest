package kg.com.transactionservice.service.impl;

import kg.com.transactionservice.dto.transaction.SetTransactionRequest;
import kg.com.transactionservice.dto.transaction.TransactionReportDto;
import kg.com.transactionservice.enums.Category;
import kg.com.transactionservice.model.Limit;
import kg.com.transactionservice.model.Transaction;
import kg.com.transactionservice.repository.TransactionRepository;
import kg.com.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
	
	private final TransactionRepository transactionRepository;
	private final CurrencyRatesServiceImpl currencyRatesService;
	private final LimitServiceImpl limitService;
	
	@Override
	public void save(SetTransactionRequest t) {
		if (Arrays.stream(Category.values())
				.noneMatch(e -> e.name().equalsIgnoreCase(t.getExpenseCategory()))) {
			log.error("Invalid expense category: {}", t.getExpenseCategory());
			throw new IllegalArgumentException("Invalid expense category: " + t.getExpenseCategory());
		}
		
		Transaction transaction = Transaction.builder()
				.accountFrom(t.getAccountFrom())
				.accountTo(t.getAccountTo())
				.currencyShortname(t.getCurrencyShortname())
				.amount(t.getAmount())
				.datetime(OffsetDateTime.now(ZoneOffset.ofHours(6)))
				.expenseCategory(t.getExpenseCategory())
				.build();
		
		Boolean limitExceeded = checkLimitExceeded(transaction);
		transaction.setLimitExceeded(limitExceeded);
		
		log.info("Saving transaction: {}", transaction);
		transactionRepository.save(transaction);
	}
	
	@Override
	public List<TransactionReportDto> getLimitExceededTransaction(String userId) {
		List<Transaction> exceededTransactions = transactionRepository.findByAccountFromAndLimitExceededTrue(userId);
		log.info("Exceeded transactions: {}", exceededTransactions);
		return exceededTransactions.stream()
				.map(this :: mapTransactionReportDto)
				.toList();
	}
	
	private TransactionReportDto mapTransactionReportDto(Transaction t) {
		Limit limit = limitService.getLimitByUserAndCategory(t.getAccountFrom(), t.getExpenseCategory())
				.stream()
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Limit not found for the specified category."));
		
		BigDecimal convertedAmount = convertToUSD(t.getCurrencyShortname(), t.getAmount());
		
		return TransactionReportDto.builder()
				.transactionId(t.getId())
				.transactionAmount(convertedAmount)
				.dateTime(t.getDatetime())
				.limitSum(limit.getLimitSum())
				.currency(limit.getLimitCurrencyShortname())
				.limitExceeded(t.getLimitExceeded())
				.build();
	}
	
	private Boolean checkLimitExceeded(Transaction t) {
		log.info("Checking limit exceeded transaction: {}", t);
		BigDecimal transactionAmountInUsd = convertToUSD(t.getCurrencyShortname(), t.getAmount());
		
		List<Limit> limits = limitService.getLimitByUserAndCategory(t.getAccountFrom(), t.getExpenseCategory());
		log.info("Limits found: {}", limits);
		Limit applicableLimit = null;
		
		for (Limit limit : limits) {
			if (limit.getLimitDatetime().isBefore(t.getDatetime())) {
				applicableLimit = limit;
			}
		}
		log.info("Applicable limit found: {}", applicableLimit);
		if (applicableLimit == null) {
			throw new IllegalStateException("No applicable limit found for this transaction");
		}
		
		applicableLimit.setRemainingAmount(applicableLimit.getRemainingAmount().subtract(transactionAmountInUsd));
		limitService.updateRemainingAmount(applicableLimit);

		return transactionAmountInUsd.compareTo(applicableLimit.getRemainingAmount()) > 0;
	}
	
	private BigDecimal convertToUSD(String currencyCode, BigDecimal amount) {
		log.info("Converting amount {} to {}", amount, currencyCode);
		BigDecimal exchangeRate = currencyRatesService.getActualCurrencyRate(currencyCode).getRate();
		return amount.divide(exchangeRate, RoundingMode.HALF_UP);
	}
}
