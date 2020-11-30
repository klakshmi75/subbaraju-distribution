package com.lakshmi.poc.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@Slf4j
@Getter
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {
    @Value("${swagger.info.build.version}")
    private String version;

    /**
     * Generates UI Configuration for the swagger page.
     * @return UiConfiguration object
     */
    @Bean
    public UiConfiguration configureUI() {
        return UiConfigurationBuilder.builder()
                .displayRequestDuration(true)
                .build();
    }

    @Bean
    public Docket produceApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.lakshmi.poc.controller"))
                .build();
    }
}