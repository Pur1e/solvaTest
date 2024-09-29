package kg.com.transactionservice.dto.limit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetLimitRequest {
	@NotNull(message = "Category cannot be null")
	@Size(max = 50, message = "Category must not exceed 50 characters")
	private String category;
	
	@NotNull
	private BigDecimal limitSum;
	
	@Size(min = 10, max = 10, message = "must consist of exactly 10 digits")
	@NotNull(message = "Account cannot be null")
	@Pattern(regexp = "\\d+", message = "Account can only contain digits")
	private String account;
}
