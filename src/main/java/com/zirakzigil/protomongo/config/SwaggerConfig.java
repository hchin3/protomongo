package com.zirakzigil.protomongo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Link http://{host}:{port}/swagger-ui/
 * 
 * @author harry
 */
@Component
public class SwaggerConfig {

	final TypeResolver typeResolver;

	public SwaggerConfig(@Autowired final TypeResolver typeResolver) {
		this.typeResolver = typeResolver;
	}

	@Bean
	public Docket mongoApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Mongo").select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.regex("/mongo/.*")).build();

	}

	@Bean
	public Docket personApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Person").select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.regex("/person/.*")).build();

	}

}
