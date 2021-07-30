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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.data.jdbc.core.convert.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.convert.SqlGeneratorSource;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.sql.IdentifierProcessing;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.yugabyte.data.jdbc.core.QueryOptions;

/**
 * The YugabyteDB
 * {@link org.springframework.data.jdbc.core.convert.DataAccessStrategy} is to
 * generate YSQL statements based on the metadata of the entity.
 *
 * @author Nikhil Chandrappa
 */
public class DefaultYsqlDataAccessStrategy extends DefaultDataAccessStrategy implements YsqlDataAccessStrategy {

	private static final String DEFERRABLE_TRANSACTION = "BEGIN ISOLATION LEVEL SERIALIZABLE, READ ONLY, DEFERRABLE";
	private final DataSource dataSource;
	private final RelationalMappingContext context;

	public DefaultYsqlDataAccessStrategy(SqlGeneratorSource sqlGeneratorSource, RelationalMappingContext context,
			JdbcConverter converter, NamedParameterJdbcOperations operations, DataSource dataSource) {
		super(sqlGeneratorSource, context, converter, operations);
		this.context = context;
		this.dataSource = dataSource;
	}

	@Override
	public long count(Class<?> domainType, QueryOptions queryOptions) throws SQLException {

		String tableName = context.getRequiredPersistentEntity(domainType).getTableName()
				.toSql(IdentifierProcessing.ANSI);			
		String sqlString = String.format("SELECT COUNT(*) FROM %s", tableName);

		// Determine transaction in flight before executing the DEFERRABLE transactions. 
		Connection readConnection = DataSourceUtils.getConnection(dataSource);
		Boolean originalAutoCommit = readConnection.getAutoCommit();
		if (queryOptions.isDeferrable() != null && queryOptions.isDeferrable()) {
			if (!TransactionSynchronizationManager.isActualTransactionActive()) {
				readConnection.setAutoCommit(false);
				Statement deferrableStatement = readConnection.createStatement();
				deferrableStatement.executeUpdate(DEFERRABLE_TRANSACTION);
			}
		}

		Statement countStatement = readConnection.createStatement();
		Long result = null;

		try {		
			ResultSet rs = countStatement.executeQuery(sqlString);
			while (rs.next()) {
				result = rs.getLong(1);
			}	
		} finally {
			readConnection.setAutoCommit(originalAutoCommit);
			readConnection.close();
		}

		Assert.notNull(result, "The result of a count query must not be null.");
		return result;
	}

}