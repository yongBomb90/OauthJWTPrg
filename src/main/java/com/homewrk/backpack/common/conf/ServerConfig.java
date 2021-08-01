package com.homewrk.backpack.common.conf;


import com.homewrk.backpack.common.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 리소스 서버 설정처리
 */
@Configuration
@EnableResourceServer
public class ServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${backpack.api.server-id}")
    private String apiServerId;

    @Resource
    private JwtFilter jwtFilter;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //super.configure(resources);
        resources
                .resourceId(apiServerId)
                .stateless(false)
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //super.configure(http);

        http.
                 anonymous().disable()
                    .authorizeRequests()
                    .antMatchers("/api/*/**")
                    .authenticated()
                .and()
                    .authorizeRequests()
                    .antMatchers("/oauth/**","/api/member/join").permitAll()
                .and()
                    .exceptionHandling()
                    .accessDeniedHandler(new OAuth2AccessDeniedHandler())
                    .and().exceptionHandling()
                .and()
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;


    }
}
