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
package com.yugabyte.data.jdbc.core.convert;

import java.sql.SQLException;

import org.springframework.data.jdbc.core.convert.DataAccessStrategy;

import com.yugabyte.data.jdbc.core.QueryOptions;

public interface YsqlDataAccessStrategy extends DataAccessStrategy {
	
	
	/**
	 * Counts the number of objects of a given type.
	 * 
	 * Method uses below transaction isolation level for count(*) queries, 
	 * Transaction BEGIN ISOLATION LEVEL SERIALIZABLE READ ONLY DEFERRABLE 
	 * 
	 * @param domainType the type of the aggregates to be counted.
	 * @return the number of instances stored in the YugabyteDB cluster. Guaranteed to be not {@code null}.
	 */
	long count(Class<?> domainType, QueryOptions queryOptions) throws SQLException;
	
}