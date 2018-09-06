package com.prestigegodson.starter.restfulservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("com.prestigegodson.starter.restfulservice.controller"))
          .paths(PathSelectors.any())
          .build()
          .apiInfo(apiInfo())
          .securitySchemes(Arrays.asList(apiKey()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Starter REST API",
                "Starter Restful Service.",
                "API v1",
                "Free",
                "Godson Ositadinma prestigegodson@gmail.com",
                "", "" );
    }

    private ApiKey[] apiKey(){

        return new ApiKey[]{
                new ApiKey("Authorization","Authorization","header"),
                new ApiKey("Refresh-Token","refreshToken","header")
        };
    }


}