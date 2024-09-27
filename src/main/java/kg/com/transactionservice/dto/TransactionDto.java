package kg.com.transactionservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
	private Long id;
	
	@NotNull(message = "Sender account cannot be null")
	@Size(min = 10, max = 10, message = "Sender account must be exactly 10 digits")
	private Long accountFrom;
	
	@NotNull(message = "Recipient account cannot be null")
	@Size(min = 10, max = 10, message = "Recipient account must be exactly 10 digits")
	private Long accountTo;
	
	@NotNull(message = "Currency short name cannot be null")
	@Size(min = 3, max = 3, message = "Currency short name must consist of exactly three letters")
	private String currencyShortname;
	
	@NotNull(message = "Amount cannot be null")
	private BigDecimal amount;
	
	@NotNull(message = "Expense category cannot be null")
	@Size(max = 50, message = "Expense category must not exceed 50 characters")
	private String expenseCategory;
	
	private OffsetDateTime datetime;
	private Boolean limit_exceeded;
}
