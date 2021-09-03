package br.com.alura.carteira.infra;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxSwaggerConfigurations {

	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any())
          .build()
          .globalRequestParameters(Arrays.asList(
        		  new RequestParameterBuilder()
                  .name("Authorization")
                  .description("Bearer Token")
                  .required(false)
                  .in("header")
                  .build()))
          .apiInfo(apiInfo());
    }

	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "API Carteira de Investimentos", 
	      "Teste", 
	      "Termos de Uso", 
	      "Termos de Servico", 
	      new Contact("Rita Cury", "www.example.com", "backend@rafaela.com.br"), 
	      "License of API", "API license URL", Collections.emptyList());
	}

}
