package com.yugabyte.spring;

import com.yugabyte.ysql.YBClusterAwareDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public abstract class AbstractYugabyteConfiguration {

    @Value("${spring.yugabyte.initialHost:localhost}")
    private String initialHost;

    @Value("${spring.yugabyte.port:5433}")
    private int port;

    @Value("${spring.yugabyte.database:yugabyte}")
    private String database;

    @Value("${spring.yugabyte.user:yugabyte}")
    private String user;

    @Value("${spring.yugabyte.password:yugabyte}")
    private String password;

    @Value("${spring.yugabyte.maxPoolSizePerNode:8}")
    private int maxPoolSizePerNode;

    @Value("${spring.yugabyte.autoCommit:true}")
    private boolean autoCommit;

    @Value("${spring.yugabyte.connectionTimeoutMs:10000}")
    private int connectionTimeoutMs;

    @Value("${spring.yugabyte.generate-ddl:false}")
    private boolean generateDdl;

    @Value("${spring.yugabyte.packages-to-scan}")
    private String packagesToScan;

    @Bean
    public DataSource dataSource() {
        YBClusterAwareDataSource ds = new YBClusterAwareDataSource();
        ds.setInitialHost(initialHost);
        ds.setPort(port);
        ds.setDatabase(database);
        ds.setUser(user);
        ds.setPassword(password);
        ds.setMaxPoolSizePerNode(maxPoolSizePerNode);
        ds.setAutoCommit(autoCommit);
        ds.setConnectionTimeoutMs(connectionTimeoutMs); // 10 seconds.

        return ds;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(generateDdl);
        adapter.setDatabase(Database.POSTGRESQL);

        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan(packagesToScan);
        em.setPersistenceProvider(new HibernatePersistenceProvider());
        em.setDataSource(dataSource());
        em.setJpaVendorAdapter(jpaVendorAdapter());

        return em;
    }
}