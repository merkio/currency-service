package com.company.currency.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {

        List<ResponseMessage> list = new ArrayList<>();

        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("Currency API")
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.company.currency"))
            .paths(PathSelectors.ant("/**"))
            .build()
            .apiInfo(apiInfo())
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            .globalResponseMessage(RequestMethod.GET, list)
            .globalResponseMessage(RequestMethod.POST, list);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Currency rate service")
            .description("Currency rate Service API")
            .license("Open Source")
            .build();
    }
}
