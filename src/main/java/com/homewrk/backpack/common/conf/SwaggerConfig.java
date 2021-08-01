package com.homewrk.backpack.common.conf;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Swagger 설정 관련
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 모든 API swagger 대상
     * @return
     */
//    @Bean
//    public Docket api(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                .pathMapping("/");
//    }

    @Bean
    public Docket api(){


        Parameter parameterBuilder = new ParameterBuilder()
            .name(HttpHeaders.AUTHORIZATION)
                .description("Access Tocken")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        List<Parameter> globalParamters = new ArrayList<>();
        globalParamters.add(parameterBuilder);

        ApiInfo serverApiInfo = new ApiInfo(
                "백패커 과제 리소스 API Docs"
                , "인증 서버(JWT) 및 API서버(JPA) Docs"
                , "1.0.0"
                , "-"
                , new Contact("Contact Me", "-", "docsuli90@gmail.com")
                , "Licenses"
                , "-"
                ,  new ArrayList<>());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(globalParamters)
                .groupName("리소스서버")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.homewrk.backpack"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(serverApiInfo)
                ;

    }

    @Bean
    public Docket apiV2(){

        ApiInfo authApiInfo = new ApiInfo(
                "백패커 과제 인증 API Docs"
                , "인증 서버(JWT) 및 API서버(JPA) Docs"
                , "1.0.0"
                , "-"
                , new Contact("Contact Me", "-", "docsuli90@gmail.com")
                , "Licenses"
                , "-"
                ,  new ArrayList<>());

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("권한서버(로그인)")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/oauth/token"))
                .build()
                .apiInfo(authApiInfo)
                .securitySchemes(Arrays.asList(apiKey2()));

    }

    private ApiKey apiKey2() {
        return new ApiKey("Basic YmFja3BhY2s6VGVzdDEyMzQh", "Authorization", "header");
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer +accessToken", "Authorization", "header");
    }

}
