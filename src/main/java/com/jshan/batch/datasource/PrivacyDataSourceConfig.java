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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "privacyEntityManagerFactory",
    transactionManagerRef = "privacyTransactionManager",
    basePackages = {"com.jshan.batch.repository.privacy"})
public class PrivacyDataSourceConfig {

    private final JpaProperties jpaProperties;

    @Bean("privacyDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.privacy")
    public DataSource privacyDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("privacyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean privacyEntityManagerFactory(@Qualifier("privacyDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.cheilemp.batch.entity.privacy");
        entityManagerFactoryBean.setPersistenceUnitName("privacy");

        Properties properties = new Properties();
        properties.putAll(jpaProperties.getProperties());

        entityManagerFactoryBean.setJpaProperties(properties);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return entityManagerFactoryBean;
    }

    @Bean("privacyTransactionManager")
    public PlatformTransactionManager privacyTransactionManager(@Qualifier("privacyDataSource") DataSource dataSource) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setEntityManagerFactory(privacyEntityManagerFactory(dataSource).getObject());
        return transactionManager;
    }
}
