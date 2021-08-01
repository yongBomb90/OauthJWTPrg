package com.homewrk.backpack.common.conf;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Swagger 설정 관련
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 모든 리소스 API swagger 대상
     * @return
     */
    @Bean
    public Docket api(){


//        Parameter parameterBuilder = new ParameterBuilder()
//            .name(HttpHeaders.AUTHORIZATION)
//                .description("Access Tocken(Bearer +accessToken)")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build();
//        List<Parameter> globalParamters = new ArrayList<>();
//        globalParamters.add(parameterBuilder);

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
                //.globalOperationParameters(globalParamters)
                .groupName("리소스서버")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.homewrk.backpack"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(serverApiInfo)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Arrays.asList(securitySchema()))
                ;

    }

    /**
     * 로그인 API swagger 대상
     * @return
     */
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

        Parameter parameterBuilder = new ParameterBuilder()
                .name(HttpHeaders.AUTHORIZATION)
                .description("Api key(Basic YmFja3BhY2s6VGVzdDEyMzQh)")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        List<Parameter> globalParamters = new ArrayList<>();
        globalParamters.add(parameterBuilder);

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("권한서버(로그인)")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/oauth/token"))
                .build()
                .apiInfo(authApiInfo)
                ;




    }

    private OAuth securitySchema() {
        final List<AuthorizationScope> authorizationScopeList = new ArrayList<>(2);

        authorizationScopeList.add(new AuthorizationScope("read", "read all"));
        authorizationScopeList.add(new AuthorizationScope("write", "access all"));

        final List<GrantType> grantTypes = new ArrayList<>(1);
        // 토큰 end point (http://localhost:3000/oauth/token)
        grantTypes.add(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));

        return new OAuth("oauth2", authorizationScopeList, grantTypes);
    }
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[] {
                new AuthorizationScope("read", "read all"),
                new AuthorizationScope("write", "write all")
        };

        return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
    }
}
