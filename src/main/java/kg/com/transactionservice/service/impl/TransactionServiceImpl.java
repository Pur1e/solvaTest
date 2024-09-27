package kg.com.transactionservice.service.impl;

import kg.com.transactionservice.dto.TransactionDto;
import kg.com.transactionservice.model.Transaction;
import kg.com.transactionservice.repository.TransactionRepository;
import kg.com.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
	
	private final TransactionRepository transactionRepository;
	
	@Override
	public void save(TransactionDto t) {
		Boolean limitExceeded = checkLimitExceeded(t.getAccountFrom());
		
		Transaction transaction = Transaction.builder()
				.accountFrom(t.getAccountFrom())
				.accountTo(t.getAccountTo())
				.currencyShortname(t.getCurrencyShortname())
				.amount(t.getAmount())
				.datetime(OffsetDateTime.now())
				.expenseCategory(t.getExpenseCategory())
				.limitExceeded(limitExceeded)
				.build();
		transactionRepository.save(transaction);
	}
	
	//todo 4. Флаг превышения лимита / finish later
	private Boolean checkLimitExceeded(Long accountFrom) {
		return true;
	}
}
