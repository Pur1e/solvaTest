package kg.com.transactionservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "currency_rates")
public class CurrencyRate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Size(max = 7)
	@NotNull
	@Column(name = "currency_pair", nullable = false, length = 7)
	private String currencyPair;
	
	@NotNull
	@Column(name = "rate", nullable = false, precision = 15, scale = 2)
	private BigDecimal rate;
	
	@NotNull
	@Column(name = "rate_date", nullable = false)
	private OffsetDateTime rateDate;
	
	@ColumnDefault("now()")
	@Column(name = "created_at")
	private Instant createdAt;
	
}