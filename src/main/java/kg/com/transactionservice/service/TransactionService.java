package kg.com.transactionservice.service;

import kg.com.transactionservice.dto.transaction.SetTransactionRequest;
import kg.com.transactionservice.dto.transaction.TransactionReportDto;

import java.util.List;

public interface TransactionService {
	
	void save(SetTransactionRequest transaction);
	
	List<TransactionReportDto> getLimitExceededTransaction(String userId);
}
