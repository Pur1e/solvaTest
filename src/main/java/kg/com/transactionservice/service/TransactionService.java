package kg.com.transactionservice.service;

import kg.com.transactionservice.dto.TransactionDto;

public interface TransactionService {
	void save(TransactionDto transaction);
}
