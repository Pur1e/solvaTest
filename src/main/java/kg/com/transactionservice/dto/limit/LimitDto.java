package kg.com.transactionservice.dto.limit;

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
public class LimitDto {
	private Long id;
	private BigDecimal limitSum;
	private String limitCurrencyShortname;
	private OffsetDateTime limitDatetime;
	private String category;
	private String account;
}
