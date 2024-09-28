package kg.com.transactionservice.dto.transaction;

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
public class TransactionReportDto {
	private Long transactionId;
	private OffsetDateTime dateTime;
	private BigDecimal transactionAmount;
	private BigDecimal limitSum;
	private String currency;
}
