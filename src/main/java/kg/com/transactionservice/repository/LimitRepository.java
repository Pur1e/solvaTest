package kg.com.transactionservice.repository;

import kg.com.transactionservice.model.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
	
	@Query(value = """
			SELECT * FROM limits l
			WHERE l.account =:account
			AND l.category =:category
			ORDER BY l.limit_datetime ASC
			""", nativeQuery = true)
	List<Limit> findByLimitAccountAndCategory(@Param("account") String account, @Param("category") String category);
}
