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
@Table(name = "transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "account_from", nullable = false)
	private Long accountFrom;
	
	@NotNull
	@Column(name = "account_to", nullable = false)
	private Long accountTo;
	
	@Size(max = 3)
	@NotNull
	@Column(name = "currency_shortname", nullable = false, length = 3)
	private String currencyShortname;
	
	@NotNull
	@Column(name = "amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal amount;
	
	@Size(max = 50)
	@NotNull
	@Column(name = "expense_category", nullable = false, length = 50)
	private String expenseCategory;
	
	@NotNull
	@Column(name = "datetime", nullable = false)
	private OffsetDateTime datetime;
	
	@NotNull
	@ColumnDefault("false")
	@Column(name = "limit_exceeded", nullable = false)
	private Boolean limitExceeded = false;
}