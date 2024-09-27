package kg.com.transactionservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
	
	PRODUCT("product"),
	SERVICE("service");
	
	private final String category;
}
