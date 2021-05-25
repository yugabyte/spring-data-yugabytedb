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

import org.springframework.data.jdbc.core.convert.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.convert.SqlGeneratorSource;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.sql.IdentifierProcessing;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.util.Assert;

import com.yugabyte.data.jdbc.core.QueryOptions;
import com.yugabyte.data.jdbc.core.TransactionMode;

/**
 * The YugabyteDB
 * {@link org.springframework.data.jdbc.core.convert.DataAccessStrategy} is to
 * generate YSQL statements based on the metadata of the entity.
 *
 * @author Nikhil Chandrappa
 */
public class YugabyteDbDefaultDataAccessStrategy extends DefaultDataAccessStrategy
		implements YugabyteDbDataAccessStrategy {

	private final NamedParameterJdbcOperations operations;
	private final RelationalMappingContext context;

	public YugabyteDbDefaultDataAccessStrategy(SqlGeneratorSource sqlGeneratorSource, RelationalMappingContext context,
			JdbcConverter converter, NamedParameterJdbcOperations operations) {
		super(sqlGeneratorSource, context, converter, operations);
		this.operations = operations;
		this.context = context;
	}

	@Override
	public long count(Class<?> domainType, QueryOptions queryOptions) {

		String tableName = context.getRequiredPersistentEntity(domainType).getTableName()
				.toSql(IdentifierProcessing.NONE);
		
		String sqlString = String.format("SELECT COUNT(*) FROM %s", tableName);;
		
		if (queryOptions.getIsolationLevel().equalsIgnoreCase(TransactionMode.DEFRRABLE.toString())) {
				// need to find a way to specify BEGIN ISOLATION LEVEL SERIALIZABLE, READ ONLY, DEFERRABLE
				// scan_conn.setAutoCommit(false);

            // Additionally, for long running scans, with concurrent writes, set READ ONLY, DEFERRABLE to
            // avoid read-restarts.
            // Statement stmt = scan_conn.createStatement();
            // stmt.executeUpdate("BEGIN ISOLATION LEVEL SERIALIZABLE, READ ONLY, DEFERRABLE");
		} 	
		
		Long result = operations.getJdbcOperations().queryForObject(sqlString, Long.class);

		Assert.notNull(result, "The result of a count query must not be null.");

		return result;
	}

}
