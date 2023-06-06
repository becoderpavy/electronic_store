package com.electronic.config;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket docket() {

		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		docket.apiInfo(getApiIno());

		docket.securityContexts(Arrays.asList(getSecurityContext()));
		docket.securitySchemes(Arrays.asList(getSchemas()));

		ApiSelectorBuilder select = docket.select();
		select.apis(RequestHandlerSelectors.any());
		select.paths(PathSelectors.any());
		Docket build = select.build();

		return build;
	}

	private ApiKey getSchemas() {
		return new ApiKey("JWT", "Authorization", "header"); 
	}

	private SecurityContext getSecurityContext() {
		SecurityContext context = SecurityContext.builder().securityReferences(getSecurityRefernce()).build();

		return context;
	}

	private List<SecurityReference> getSecurityRefernce() {
		AuthorizationScope[] scopes = { new AuthorizationScope("Global", "Access Everything") };

		return Arrays.asList(new SecurityReference("JWT", scopes));
	}

	private ApiInfo getApiIno() {

		ApiInfo apiInfo = new ApiInfo("Electronic Store Backend APIS", "This is backend Project created by Becoder",
				"1.0.0V", "http://localhost:8080",
				new Contact("Pabitra Das", "http://linkedin.com", "becoder@gmail.com"), "License of APIS",
				"http://linkedin.com", new ArrayDeque<>());

		return apiInfo;
	}

}
