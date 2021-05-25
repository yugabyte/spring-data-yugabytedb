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
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.jdbc.repository.config.DialectResolver.DefaultDialectProvider;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.lang.Nullable;

import com.yugabyte.data.relational.core.dialect.YugabyteDbDialect;

/**
 * An SQL dialect for YugabyteDB.
 *
 * @author Nikhil Chandrappa
 * @since 2.3
 */
public class YugabyteDbDialectResolver {

	static public class YugabyteDbDialectProvider extends DefaultDialectProvider {

		private static final Log LOG = LogFactory.getLog(YugabyteDbDialectProvider.class);

		@Override
		public Optional<Dialect> getDialect(JdbcOperations operations) {

			Optional<Dialect> yugabytedbdialect = Optional.ofNullable(
					operations.execute((ConnectionCallback<Dialect>) YugabyteDbDialectProvider::getDialect));

			if (yugabytedbdialect.isPresent()) {
				return yugabytedbdialect;
			}

			return super.getDialect(operations);
		}

		@Nullable
		private static Dialect getDialect(Connection connection) throws SQLException {

			DatabaseMetaData metaData = connection.getMetaData();
			
			// make use of yb_tservers() to determine if its a yb-cluster.
			String name = metaData.getDatabaseProductName().toLowerCase(Locale.ENGLISH);

			if (name.contains("yugabyte")) {
				return YugabyteDbDialect.INSTANCE;
			}

			LOG.info(String.format("Couldn't determine Dialect for \"%s\"", name));
			return null;
		}
	}

}
