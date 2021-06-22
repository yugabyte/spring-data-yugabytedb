package com.yugabyte.data.jdbc.core;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class QueryOptions {

	private static final QueryOptions EMPTY = QueryOptions.builder().build();
	
	private final @Nullable String isolationLevel;
	private final @Nullable Boolean multiRowInsert;
	private final @Nullable Boolean followerRead;
	
	protected QueryOptions (@Nullable String isolationLevel, 
			@Nullable Boolean multiRowInsert, @Nullable Boolean follwerRead) {
		this.isolationLevel = isolationLevel;
		this.multiRowInsert = multiRowInsert;
		this.followerRead = follwerRead;	
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
	
	public Boolean getMutliRowInsert() {
		return this.multiRowInsert;
	}
	
	public Boolean getFollowerRead() {
		return this.followerRead;
	}

	public static class QueryOptionsBuilder {
		
		protected @Nullable String isolationLevel;
		protected @Nullable Boolean multiRowInsert;
		protected @Nullable Boolean followerRead;

		QueryOptionsBuilder() {
		}

		QueryOptionsBuilder(QueryOptions queryOptions) {
			this.isolationLevel = queryOptions.isolationLevel;
			this.multiRowInsert = queryOptions.multiRowInsert;
			this.followerRead = queryOptions.followerRead;
		}
		
		public QueryOptionsBuilder isolationLevel(String isolationLevel) {
			
			Assert.notNull(isolationLevel, "Isolation level cannot be null");
			
			this.isolationLevel = isolationLevel;
			
			return this;
		}
		
		public QueryOptionsBuilder multiRowInsert(Boolean multiRowInsert) {
			
			this.multiRowInsert = multiRowInsert;
			
			return this;
		}
		
		public QueryOptionsBuilder followerRead(Boolean followerRead) {
			
			this.followerRead = followerRead;
			
			return this;
		}

		public QueryOptions build() {
			return new QueryOptions(this.isolationLevel, this.multiRowInsert, this.followerRead);
		}
	}
}
