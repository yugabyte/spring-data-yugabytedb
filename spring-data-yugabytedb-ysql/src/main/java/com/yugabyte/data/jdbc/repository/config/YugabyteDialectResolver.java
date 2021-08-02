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
package com.yugabyte.data.jdbc.repository.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.jdbc.repository.config.DialectResolver.DefaultDialectProvider;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import com.yugabyte.data.relational.core.dialect.YugabyteDialect;

/**
 * An SQL dialect for YugabyteDB.
 *
 * @author Nikhil Chandrappa
 * @since 2.3
 */
public class YugabyteDialectResolver {

	static public class YugabyteDialectProvider extends DefaultDialectProvider {

		private static final Log LOG = LogFactory.getLog(YugabyteDialectProvider.class);
		private static final String YUGABYTE_SERVERS_QUERY = "select * from yb_servers()";

		@Override
		public Optional<Dialect> getDialect(JdbcOperations operations) {

			Optional<Dialect> yugabyteDialect = Optional.ofNullable(
					operations.execute((ConnectionCallback<Dialect>) YugabyteDialectProvider::getDialect));

			if (yugabyteDialect.isPresent()) {
				return yugabyteDialect;
			}

			return super.getDialect(operations);
		}

		@Nullable
		private static Dialect getDialect(Connection connection) throws SQLException {
			
			Statement ybStatement = connection.createStatement();
			Dialect dialect = null;
			
			LOG.debug("Executing query against YB_SERVERS() system function.");
			ResultSet rs = ybStatement.executeQuery(YUGABYTE_SERVERS_QUERY);
			if (!rs.next()) {
				LOG.debug("Query for YB_SERVERS() system function returned null. Falling back on DefaultDialectProvider.");
				return null;
			}
			
			// Determine client application is connecting to a YugabyteDB cluster.
			String hostName = rs.getString("host");
			if (StringUtils.hasText(hostName)) {
				dialect = YugabyteDialect.INSTANCE;
			}

			LOG.debug("Using YugabyteDB YSQL Dialect.");
			return dialect;
		}
	}

}