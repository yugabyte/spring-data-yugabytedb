package com.yugabyte.data.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.yugabyte.data.jdbc.core.YsqlTemplate;
import com.yugabyte.data.jdbc.core.convert.YsqlDataAccessStrategy;
import com.yugabyte.data.jdbc.repository.config.AbstractYugabyteJdbcConfiguration;
import com.yugabyte.data.relational.core.dialect.YugabyteDialect;

public class AbstractYugabyteConfigurationIntegrationTests {

	AnnotationConfigApplicationContext testApplicationContext;

	@Before
	public void setup() {
		testApplicationContext = new AnnotationConfigApplicationContext(SampleYugabyteJdbcConfiguration.class);
	}

	@Test
	public void configureYugabyteRelatedBeans() {

		assertThat(testApplicationContext.getBean("jdbcDialect", Dialect.class)).isInstanceOf(YugabyteDialect.class);
		assertThat(testApplicationContext.getBean("ysqlTemplate", YsqlTemplate.class)).isNotNull();
		assertThat(testApplicationContext.getBean("ysqlDataAccessStrategyBean", YsqlDataAccessStrategy.class)).isNotNull();
	}

	@After
	public void close() {
		testApplicationContext.close();
	}

	static class SampleYugabyteJdbcConfiguration extends AbstractYugabyteJdbcConfiguration {
		
		@Bean
		public NamedParameterJdbcOperations jdbcOperations() {

			JdbcOperations jdbcOperations = mock(JdbcOperations.class);
			return new NamedParameterJdbcTemplate(jdbcOperations);
		}
		
		@Bean
		public DataSource dataSource() {

			DataSource dataSource = mock(DataSource.class);
			return dataSource;
		}

		@Override
		@Bean
		public Dialect jdbcDialect(NamedParameterJdbcOperations operations) {
			return YugabyteDialect.INSTANCE;
		}
	}

}
