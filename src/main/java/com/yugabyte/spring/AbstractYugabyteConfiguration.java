package com.yugabyte.spring;

import com.yugabyte.ysql.YBClusterAwareDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
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

    public abstract String packagesToScan();


    public boolean generateDdl() {
        return false;
    }

    @Bean
    public DataSource dataSource() {
        return new YBClusterAwareDataSource();
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(generateDdl());
        adapter.setDatabase(Database.POSTGRESQL);

        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan(packagesToScan());
        em.setPersistenceProvider(new HibernatePersistenceProvider());
        em.setDataSource(dataSource());
        em.setJpaVendorAdapter(jpaVendorAdapter());

        return em;
    }
}