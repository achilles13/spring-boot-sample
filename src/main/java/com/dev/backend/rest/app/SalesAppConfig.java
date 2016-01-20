package com.dev.backend.rest.app;

import java.io.IOException;

import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

import com.dev.backend.persistence.dao.CustomerDAO;
import com.dev.backend.persistence.dao.ProductDAO;
import com.dev.backend.persistence.dao.SalesOrderDAO;
import com.dev.backend.persistence.management.DBManagerUtil;
import com.dev.backend.rest.controller.SalesAppHandler;

/**
 * Spring configuration class that defines the singleton classes for this application.
 * @author creddy
 *
 */
@ComponentScan(basePackages = { "com.dev.backend"})
@EnableAutoConfiguration(exclude = { SolrAutoConfiguration.class })
@Configuration
@PropertySource(ignoreResourceNotFound = true, value = { "classpath:/application.properties" })
public class SalesAppConfig {
	
	@Bean
	@Lazy
	public SessionFactory sessionFactory() throws IOException {
		return DBManagerUtil.getSessionFactory();
	}
	
	@Bean
	public CustomerDAO customerDao() throws IOException {
		return new CustomerDAO();
	}
	
	@Bean
	public ProductDAO productDao() throws IOException {
		return new ProductDAO();
	}
	
	@Bean
	public SalesOrderDAO salesOrderDao() throws IOException {
		return new SalesOrderDAO();
	}
	
	@Bean
	public SalesAppHandler salesAppHandler() throws IOException {
		return new SalesAppHandler(customerDao(), productDao(), salesOrderDao(), sessionFactory());
	}

}
