package com.jshan.batch.datasource;

import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "eventEntityManagerFactory",
    transactionManagerRef = "eventTransactionManager",
    basePackages = {"com.jshan.batch.repository.event"})
public class EventDataSourceConfig {

    private final JpaProperties jpaProperties;

    @Primary
    @Bean("eventDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.event")
    public DataSource eventDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean("eventEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean eventEntityManagerFactory(@Qualifier("eventDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.jshan.batch.entity.event");
        entityManagerFactoryBean.setPersistenceUnitName("event");

        Properties properties = new Properties();
        properties.putAll(jpaProperties.getProperties());

        entityManagerFactoryBean.setJpaProperties(properties);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return entityManagerFactoryBean;
    }

    @Primary
    @Bean("eventTransactionManager")
    public PlatformTransactionManager eventTransactionManager(@Qualifier("eventDataSource") DataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setEntityManagerFactory(eventEntityManagerFactory(dataSource).getObject());
        return transactionManager;
    }
}
