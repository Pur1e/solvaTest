package kg.com.transactionservice.service.impl;

import kg.com.transactionservice.dto.TransactionDto;
import kg.com.transactionservice.enums.Category;
import kg.com.transactionservice.model.CurrencyRate;
import kg.com.transactionservice.model.Limit;
import kg.com.transactionservice.model.Transaction;
import kg.com.transactionservice.repository.TransactionRepository;
import kg.com.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
	
	private final TransactionRepository transactionRepository;
	private final CurrencyRatesServiceImpl currencyRatesService;
	private final LimitServiceImpl limitService;
	
	@Override
	public void save(TransactionDto t) {
		if (Arrays.stream(Category.values())
				.noneMatch(e -> e.name().equalsIgnoreCase(t.getExpenseCategory()))) {
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
		
		transactionRepository.save(transaction);
	}
	
	private Boolean checkLimitExceeded(Transaction t) {
		CurrencyRate rate = currencyRatesService.getActualCurrencyRate(t.getCurrencyShortname());
		
		BigDecimal transactionAmountInUsd = t.getAmount().divide(rate.getRate(), RoundingMode.HALF_UP);
		
		List<Limit> limits = limitService.getLimitByUserAndCategory(t.getAccountFrom(), t.getExpenseCategory());
		
		Limit applicableLimit = null;
		
		for (Limit limit : limits) {
			if (limit.getLimitDatetime().isBefore(t.getDatetime())) {
				applicableLimit = limit;
			}
		}
		
		if (applicableLimit == null) {
			throw new IllegalStateException("No applicable limit found for this transaction");
		}
		
		applicableLimit.setRemainingAmount(applicableLimit.getRemainingAmount().subtract(transactionAmountInUsd));
		limitService.updateRemainingAmount(applicableLimit);
		
		return transactionAmountInUsd.compareTo(applicableLimit.getRemainingAmount()) > 0;
	}
}
