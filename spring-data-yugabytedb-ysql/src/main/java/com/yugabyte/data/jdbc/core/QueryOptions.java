/*
 * Copyright (c) Yugabyte, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except 
 * in compliance with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License 
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express 
 * or implied.  See the License for the specific language governing permissions and limitations 
 * under the License.
*/
package com.yugabyte.data.jdbc.core;

import org.springframework.lang.Nullable;

/**
 * Specifies QueryOptions specified at Statement level for each query performed on YugabyteDB.
 *
 * @author Nikhil Chandrappa
 */
public class QueryOptions {

	private static final QueryOptions EMPTY = QueryOptions.builder().build();
	
	private final @Nullable Boolean deferrable;
	private final @Nullable Boolean multiRowInsert;
	private final @Nullable Boolean followerRead;
	
	protected QueryOptions (@Nullable Boolean isolationLevel, 
			@Nullable Boolean multiRowInsert, @Nullable Boolean follwerRead) {
		this.deferrable = isolationLevel;
		this.multiRowInsert = multiRowInsert;
		this.followerRead = follwerRead;	
	}

	public static QueryOptionsBuilder builder() {
		return new QueryOptionsBuilder();
	}

	public static QueryOptions empty() {
		return EMPTY;
	}
	
	public Boolean isDeferrable() {
		return this.deferrable;
	}
	
	public Boolean getMutliRowInsert() {
		return this.multiRowInsert;
	}
	
	public Boolean getFollowerRead() {
		return this.followerRead;
	}

	public static class QueryOptionsBuilder {
		
		protected @Nullable Boolean deferrable;
		protected @Nullable Boolean multiRowInsert;
		protected @Nullable Boolean followerRead;

		QueryOptionsBuilder() {
		}

		QueryOptionsBuilder(QueryOptions queryOptions) {
			this.deferrable = queryOptions.deferrable;
			this.multiRowInsert = queryOptions.multiRowInsert;
			this.followerRead = queryOptions.followerRead;
		}
		
		public QueryOptionsBuilder deferrable(Boolean deferrable) {
			
			this.deferrable = deferrable;
			
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
			return new QueryOptions(this.deferrable, this.multiRowInsert, this.followerRead);
		}
	}
}