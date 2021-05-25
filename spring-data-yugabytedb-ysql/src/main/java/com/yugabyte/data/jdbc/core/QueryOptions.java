package com.yugabyte.data.jdbc.core;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class QueryOptions {

	private static final QueryOptions EMPTY = QueryOptions.builder().build();
	
	private final @Nullable String isolationLevel;
	
	protected QueryOptions (@Nullable String isolationLevel) {
		this.isolationLevel = isolationLevel;
	}

	public static QueryOptionsBuilder builder() {
		return new QueryOptionsBuilder();
	}

	public static QueryOptions empty() {
		return EMPTY;
	}
	
	public String getIsolationLevel() {
		return this.isolationLevel;
	}

	public static class QueryOptionsBuilder {
		
		protected @Nullable String isolationLevel;

		QueryOptionsBuilder() {
		}

		QueryOptionsBuilder(QueryOptions queryOptions) {
			this.isolationLevel = queryOptions.isolationLevel;
		}
		
		public QueryOptionsBuilder isolationLevel(String isolationLevel) {
			
			Assert.notNull(isolationLevel, "Isolation level cannot be null");
			
			this.isolationLevel = isolationLevel;
			
			return this;
		}

		public QueryOptions build() {
			return new QueryOptions(this.isolationLevel);
		}
	}
}
