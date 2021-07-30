package com.yugabyte.data.jdbc.testing;

import static com.yugabyte.data.jdbc.testing.YugabyteDBTestImage.YUGABYTEDB_IMAGE;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.YugabyteDBContainer;

import com.yugabyte.ysql.YBClusterAwareDataSource;

@Configuration
public class YugbayteDataSourceConfiguration {
	
	private static final Logger LOG = LoggerFactory.getLogger(YugbayteDataSourceConfiguration.class);
	private static YugabyteDBContainer YUGABYTE_CONTAINER;
	
	protected DataSource createDataSource() {
		
		if (YUGABYTE_CONTAINER == null) {
			YugabyteDBContainer yugabyteDBContainer = new YugabyteDBContainer(YUGABYTEDB_IMAGE);
			yugabyteDBContainer.start();
			YUGABYTE_CONTAINER = yugabyteDBContainer;
			LOG.info("Initialized YugabyteDB Test Container.");
		}
		
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setUrl(YUGABYTE_CONTAINER.getJdbcUrl());
		dataSource.setUser(YUGABYTE_CONTAINER.getUsername());
		dataSource.setPassword(YUGABYTE_CONTAINER.getPassword());
		LOG.info("Initialized PGSimpleDataSource for YugabyteDB.");
		
		return dataSource;
		
	}
	
	protected DataSource createClusterAwareDataSource() {
		
		if (YUGABYTE_CONTAINER == null) {
			YugabyteDBContainer yugabyteDBContainer = new YugabyteDBContainer(YUGABYTEDB_IMAGE);
			yugabyteDBContainer.start();
			YUGABYTE_CONTAINER = yugabyteDBContainer;
		}
		
		YBClusterAwareDataSource dataSource = new YBClusterAwareDataSource();
		dataSource.setUrl(YUGABYTE_CONTAINER.getJdbcUrl());
		dataSource.setUser(YUGABYTE_CONTAINER.getUsername());
		dataSource.setPassword(YUGABYTE_CONTAINER.getPassword());
		LOG.info("Initialized YBClusterAwareDataSource for YugabyteDB.");
		
		return dataSource;
	}
	
}
