package com.amakedon.om;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.amakedon.om.data.repository.jpa"})
@EnableElasticsearchRepositories(basePackages = {"com.amakedon.om.data.repository.es"})
//@EnableElasticsearchRepositories(includeFilters = @ComponentScan.Filter(
//		type = FilterType.ASSIGNABLE_TYPE, value = ElasticsearchRepository.class))
//com.amakedon.om.data.repository.es
public class OrderManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementApplication.class, args);
	}

}
