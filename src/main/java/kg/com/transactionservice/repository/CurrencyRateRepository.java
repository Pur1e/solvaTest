package kg.com.transactionservice.repository;

import kg.com.transactionservice.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
	
	@Query(value = """
			SELECT r.* FROM currency_rates AS r
			        WHERE r.currency_pair = :currencyShortname
			ORDER BY r.created_at DESC
			       	 LIMIT 1
			""", nativeQuery = true)
	Optional<CurrencyRate> findByCurrencyPair(@Param("currencyShortname") String currencyShortname);
	
}
