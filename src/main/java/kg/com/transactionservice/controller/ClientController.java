package kg.com.transactionservice.controller;

import jakarta.validation.Valid;
import kg.com.transactionservice.dto.TransactionDto;
import kg.com.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {
	private final TransactionService transactionService;
	
	@PostMapping("/transaction")
	public ResponseEntity<?> transaction(@RequestBody @Valid TransactionDto transaction) {
		transactionService.save(transaction);
		return ResponseEntity.ok().build();
	}
}
