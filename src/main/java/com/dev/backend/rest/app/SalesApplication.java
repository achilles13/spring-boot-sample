package com.dev.backend.rest.app;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Spring Boot Application class that configures the REST endpoints and initializes the context.
 * @author creddy
 *
 */
@ComponentScan(basePackages = { "com.ck.data"})
@EnableAutoConfiguration(exclude = { SolrAutoConfiguration.class })
@Configuration
@PropertySource(ignoreResourceNotFound = true, value = { "classpath:/application.properties" })
public class SalesApplication extends SpringBootServletInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(SalesApplication.class);

	public static void main(String[] args) {
	    ApplicationContext ctx = SpringApplication.run(SalesApplication.class, args);

	    LOGGER.debug("List of Beans loaded by Spring Boot");

	    String[] beanNames = ctx.getBeanDefinitionNames();
	    Arrays.sort(beanNames);
	    for (String beanName : beanNames) {
	      LOGGER.debug("Loaded bean " + beanName);
	    }
	  }

	  @Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	    return application.sources(SalesAppConfig.class);
	  }

}
