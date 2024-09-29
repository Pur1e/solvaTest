package kg.com.transactionservice;

import kg.com.transactionservice.dto.transaction.TransactionReportDto;
import kg.com.transactionservice.model.CurrencyRate;
import kg.com.transactionservice.model.Limit;
import kg.com.transactionservice.model.Transaction;
import kg.com.transactionservice.repository.TransactionRepository;
import kg.com.transactionservice.service.impl.CurrencyRatesServiceImpl;
import kg.com.transactionservice.service.impl.LimitServiceImpl;
import kg.com.transactionservice.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TransactionServiceTests {
	
	@InjectMocks
	private TransactionServiceImpl transactionService;
	@Mock
	private TransactionRepository transactionRepository;
	@Mock
	private CurrencyRatesServiceImpl currencyRatesService;
	@Mock
	private LimitServiceImpl limitService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testGetLimitExceededTransaction() {
		
		String userId = "1234567890";
		String category = "product";
		
		Limit limit = Limit.builder()
				.limitSum(new BigDecimal("1000.00"))
				.remainingAmount(new BigDecimal("500.00"))
				.limitCurrencyShortname("USD")
				.category(category)
				.account(userId)
				.build();
		
		
		Transaction transaction = Transaction.builder()
				.accountFrom(userId)
				.amount(new BigDecimal("70000.00")) // Сумма превышающая лимит
				.currencyShortname("RUB")
				.expenseCategory(category)
				.datetime(OffsetDateTime.now())
				.build();
		
		CurrencyRate currencyRate = CurrencyRate.builder()
				.currencyPair("USD/RUB")
				.rate(new BigDecimal(100))
				.build();
		
		when(transactionRepository.findByAccountFromAndLimitExceededTrue(userId))
				.thenReturn(Collections.singletonList(transaction));
		
		when(limitService.getLimitByUserAndCategory(userId, category))
				.thenReturn(Collections.singletonList(limit));
		
		when(currencyRatesService.getActualCurrencyRate(transaction.getCurrencyShortname()))
				.thenReturn(currencyRate);
		
		List<TransactionReportDto> result = transactionService.getLimitExceededTransaction(userId);
		
		assertEquals(1, result.size());
		assertEquals(transaction.getAmount().divide(currencyRate.getRate(), RoundingMode.HALF_UP), (result.get(0).getTransactionAmount()));
		assertEquals(limit.getLimitSum(), result.get(0).getLimitSum());
		assertEquals(limit.getLimitCurrencyShortname(), result.get(0).getCurrency());
	}
	
}
