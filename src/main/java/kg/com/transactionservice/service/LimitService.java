package kg.com.transactionservice.service;

import kg.com.transactionservice.dto.limit.SetLimitRequest;

public interface LimitService {
	void save(SetLimitRequest limit);
}
