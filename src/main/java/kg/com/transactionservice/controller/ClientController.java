package kg.com.transactionservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kg.com.transactionservice.dto.limit.SetLimitRequest;
import kg.com.transactionservice.dto.transaction.SetTransactionRequest;
import kg.com.transactionservice.dto.transaction.TransactionReportDto;
import kg.com.transactionservice.service.LimitService;
import kg.com.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {
	
	private final TransactionService transactionService;
	private final LimitService limitService;
	
	@PostMapping("/transactions")
	public ResponseEntity<?> addTransaction(@RequestBody @Valid SetTransactionRequest transaction) {
		transactionService.save(transaction);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/limits")
	public ResponseEntity<?> addLimit(@RequestBody @Valid SetLimitRequest limit) {
		limitService.save(limit);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/transactions/limit-exceeded")
	public ResponseEntity<List<TransactionReportDto>>
	getLimitExceedTransactions(@RequestParam
	                           @Size(min = 10, max = 10)
	                           @Pattern(regexp = "\\d+", message = "Account can only contain digits")
	                           String account) {
		List<TransactionReportDto> report = transactionService.getLimitExceededTransaction(account);
		return ResponseEntity.ok(report);
	}
}
