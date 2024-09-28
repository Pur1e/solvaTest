package kg.com.transactionservice.service.impl;

import kg.com.transactionservice.dto.limit.SetLimitRequest;
import kg.com.transactionservice.enums.Category;
import kg.com.transactionservice.model.Limit;
import kg.com.transactionservice.repository.LimitRepository;
import kg.com.transactionservice.service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LimitServiceImpl implements LimitService {
	
	private final LimitRepository limitRepository;
	
	@Override
	public void save(SetLimitRequest l) {
		if (Arrays.stream(Category.values())
				.noneMatch(e -> e.name().equalsIgnoreCase(l.getCategory()))) {
			throw new IllegalArgumentException("Invalid category: " + l.getCategory());
		}
		
		Limit limit = Limit.builder()
				.limitAccount(l.getLimitAccount())
				.limitSum(l.getLimitSum())
				.category(l.getCategory())
				.limitDatetime(OffsetDateTime.now())
				.limitCurrencyShortname("USD")
				.remainingAmount(l.getLimitSum())
				.build();
		limitRepository.save(limit);
	}
	
	protected List<Limit> getLimitByUserAndCategory(Long account, String category) {
		return limitRepository.findByLimitAccountAndCategory(account, category);
	}
	
	protected void updateRemainingAmount(Limit limit) {
		limitRepository.save(limit);
	}
	
	@Scheduled(cron = "0 0 0 1 * ?")
	private void resetMonthlyLimits() {
		limitRepository.findAll().forEach(limit -> {
			limit.setRemainingAmount(limit.getLimitSum());
			limitRepository.save(limit);
		});
	}
}
