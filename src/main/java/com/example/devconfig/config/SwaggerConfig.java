package com.example.devconfig.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by AMe on 2020-06-22 04:09.
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value("${swagger.enable:#{false}}")
    private boolean enable;

    @Bean
    public Docket customApis() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("GROUP_NAME")
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.regex("/.*"))
            .build()
            .enable(enable);
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("NAME", "URL", "example@gmail.com");
        return new ApiInfoBuilder()
            .title("TITLE")
            .description("DESC")
            .termsOfServiceUrl("SERVICE_URL")
            .contact(contact)
            .version("1.0")
            .build();
    }

}
