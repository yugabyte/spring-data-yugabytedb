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

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.convert.SqlGeneratorSource;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import com.yugabyte.data.jdbc.core.YugabyteDbYsqlJdbcTemplate;
import com.yugabyte.data.jdbc.core.convert.YugabyteDbDataAccessStrategy;
import com.yugabyte.data.jdbc.core.convert.YugabyteDbDefaultDataAccessStrategy;

/*
 * Beans registration for Spring Data YugabyteDB YSQL template and repository support.
 * 
 * @author Nikhil Chandrappa
 * @since 2.3.0
 */
@Configuration(proxyBeanMethods = false)
public class AbstractYugabyteDbJdbcConfiguration {
	
	@Bean
	public YugabyteDbYsqlJdbcTemplate yugabyteDBYsqlJdbcTemplate(ApplicationContext applicationContext,
			JdbcMappingContext mappingContext, JdbcConverter converter, YugabyteDbDataAccessStrategy dataAccessStrategy) {
		return new YugabyteDbYsqlJdbcTemplate(applicationContext, mappingContext, converter, dataAccessStrategy);
	}
	
	@Bean
	public DataAccessStrategy dataAccessStrategyBean(NamedParameterJdbcOperations operations, JdbcConverter jdbcConverter,
			JdbcMappingContext context, Dialect dialect) {
		return new YugabyteDbDefaultDataAccessStrategy(new SqlGeneratorSource(context, jdbcConverter, dialect), context,
				jdbcConverter, operations);
	}

}
