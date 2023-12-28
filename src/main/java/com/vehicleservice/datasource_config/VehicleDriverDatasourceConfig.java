package com.vehicleservice.datasource_config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.vehicleservice.auditing.AuditorAwareImpl;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(basePackages = "com.vehicleservice.repository.vehicleDriver",
						entityManagerFactoryRef = "vehicleDriverEntityManager",
						transactionManagerRef = "vehicleDriverTransactionManager")
public class VehicleDriverDatasourceConfig {

	@Autowired
	private Environment env;

	@Bean
	AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	DataSource vehicleDriverDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Primary
	LocalContainerEntityManagerFactoryBean vehicleDriverEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(vehicleDriverDataSource());
		em.setPackagesToScan(new String[] { "com.vehicleservice.entity.vehicleDriver" , "com.vehicleservice.helper" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setShowSql(Boolean.valueOf(env.getProperty("spring.jpa.show-sql")));
		vendorAdapter.setDatabasePlatform(env.getProperty("spring.jpa.database-platform"));
		
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//		properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
//		properties.put("hibernate.show_sql",true);
		em.setJpaPropertyMap(properties);

		return em;
	}

	@Primary
	@Bean
	PlatformTransactionManager vehicleDriverTransactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(vehicleDriverEntityManager().getObject());
		return transactionManager;
	}
}
