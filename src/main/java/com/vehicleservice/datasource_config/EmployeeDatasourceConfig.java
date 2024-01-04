package com.vehicleservice.datasource_config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.vehicleservice.auditing.AuditorAwareImpl;

@Configuration
//@EnableJpaAuditing(auditorAwareRef = "employeeAuditorAware")
@EnableJpaRepositories(basePackages = "com.vehicleservice.repository.employee",
						entityManagerFactoryRef = "employeeEntityManager",
						transactionManagerRef = "employeeTransactionManager")
//@EnableEncryptableProperties
//@Order(3)
public class EmployeeDatasourceConfig {

	@Autowired
	private Environment env;

//	@Bean(name = "employeeAuditorAware")
//	AuditorAware<String> employeeAuditorAware() {
//		return new AuditorAwareImpl();
//	}

//	@Bean
//	@ConfigurationProperties(prefix = "spring.second-datasource")
//	DataSource employeeDataSource() {
//		return DataSourceBuilder.create().build();
//	}

	

	@Bean
	LocalContainerEntityManagerFactoryBean employeeEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(employeeDataSource());
		em.setPackagesToScan(new String[] { "com.vehicleservice.entity.employee" });

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
	
	@Bean
	DataSource employeeDataSource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName(env.getProperty("spring.second-datasource.driver-class-name"));
//		dataSource.setUrl(env.getProperty("spring.second-datasource.jdbcUrl"));
//		dataSource.setUsername(env.getProperty("spring.second-datasource.username"));
//		dataSource.setPassword(env.getProperty("spring.second-datasource.password"));
		
		dataSource.setDriverClassName(env.getProperty("custom.employee.driverClassName"));
		dataSource.setUrl(env.getProperty("custom.employee.jdbcUrl"));
		dataSource.setUsername(env.getProperty("custom.employee.username"));
		dataSource.setPassword(env.getProperty("custom.employee.password"));

		return dataSource;
		
	}

	@Bean
	PlatformTransactionManager employeeTransactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(employeeEntityManager().getObject());
		return transactionManager;
	}
}
