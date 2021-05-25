package com.yugabyte.data.jdbc.core.convert;

import org.springframework.data.jdbc.core.convert.DataAccessStrategy;

import com.yugabyte.data.jdbc.core.QueryOptions;

public interface YugabyteDbDataAccessStrategy extends DataAccessStrategy {
	
	long count(Class<?> domainType, QueryOptions queryOptions);

}
