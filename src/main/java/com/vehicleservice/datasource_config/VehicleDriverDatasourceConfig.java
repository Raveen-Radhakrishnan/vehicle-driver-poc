package com.vehicleservice.datasource_config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.vehicleservice.auditing.AuditorAwareImpl;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(basePackages = "com.vehicleservice.repository.vehicleDriver",
						entityManagerFactoryRef = "vehicleDriverEntityManager",
						transactionManagerRef = "vehicleDriverTransactionManager")
//@EnableEncryptableProperties
//@Order(2)
public class VehicleDriverDatasourceConfig {

	@Autowired
	private Environment env;

	@Bean
	AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}

//	@Primary
//	@Bean
//	@ConfigurationProperties(prefix = "spring.datasource")
//	DataSource vehicleDriverDataSource() {
//		return DataSourceBuilder.create().build();
//	}

	@Bean
	@Primary
	LocalContainerEntityManagerFactoryBean vehicleDriverEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(vehicleDriverDataSource());
		em.setPackagesToScan(new String[] { "com.vehicleservice.entity.vehicleDriver" , "com.vehicleservice.helper" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setShowSql(Boolean.valueOf(env.getProperty("spring.jpa.show-sql")));
		vendorAdapter.setDatabasePlatform(env.getProperty("spring.jpa.database-platform"));
		vendorAdapter.setDatabase(Database.H2);
//		vendorAdapter.setGenerateDdl(true);
		
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
	public DataSource vehicleDriverDataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
//		dataSource.setUrl(env.getProperty("spring.datasource.jdbcUrl"));
//		dataSource.setUsername(env.getProperty("spring.datasource.username"));
//		dataSource.setPassword(env.getProperty("spring.datasource.password"));

		dataSource.setDriverClassName(env.getProperty("custom.vehicle.driverClassName"));
		dataSource.setUrl(env.getProperty("custom.vehicle.jdbcUrl"));
		dataSource.setUsername(env.getProperty("custom.vehicle.username"));
		dataSource.setPassword(env.getProperty("custom.vehicle.password"));

		return dataSource;

	}

	@Primary
	@Bean
	PlatformTransactionManager vehicleDriverTransactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(vehicleDriverEntityManager().getObject());
		return transactionManager;
	}
}
