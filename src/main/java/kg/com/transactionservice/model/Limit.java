package kg.com.transactionservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "limits")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Limit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "limit_sum", nullable = false, precision = 15, scale = 2)
	private BigDecimal limitSum;
	
	@Size(max = 3)
	@NotNull
	@ColumnDefault("USD")
	@Column(name = "limit_currency_shortname", nullable = false, length = 3)
	private String limitCurrencyShortname;
	
	@NotNull
	@Column(name = "limit_datetime", nullable = false)
	private OffsetDateTime limitDatetime;
	
	@Size(max = 50)
	@NotNull
	@Column(name = "category", nullable = false, length = 50)
	private String category;
	
}