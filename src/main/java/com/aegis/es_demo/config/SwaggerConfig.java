package com.aegis.es_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * 1、开启swagger
 * 2、在swagger中依然解决跨域的问题、请求头的问题、静态资源拦截问题
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("集成Swagger测试")
                .description("简单的web项目测试集成swagger")
                .version("v1.0.0")
                .build();
    }
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
//              配置指定要扫描的包
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aegis.es_demo.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .enable(true)
                .apiInfo(apiInfo());
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> list=new ArrayList();
        list.add(new ApiKey("Authorization", "Authorization", "header"));
        return list;
    }
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> list=new ArrayList();
        list.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build());
        return list;
    }
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> list = new ArrayList<>();
        list.add(new SecurityReference("Authorization", authorizationScopes));
        return list;
    }
}
