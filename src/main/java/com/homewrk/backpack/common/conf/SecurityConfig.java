package com.homewrk.backpack.common.conf;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 시큐리티 관련 bean 설정
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.homewrk.backpack.*"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${backpack.oauth.signing-key}")
    private String signingKey;

    @Resource
    private DataSource database;

    /**
     * 비밀번호 암호화 모듈 정의
     * @return
     */
    @Bean
    public PasswordEncoder encoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * JWT 토큰
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }



    /**
     * tokenstore 빈생성
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        JwtTokenStore tokenStore   = new JwtTokenStore(accessTokenConverter() );
        tokenStore.setApprovalStore(approvalStore());
        return tokenStore;
    }

   // @Bean protected AuthorizationCodeServices authorizationCodeServices() { return new JdbcAuthorizationCodeServices(database); }


//    @Bean
//    @Primary
//    public AuthorizationServerTokenServices tokenServices() {
//        return new DefaultTokenServices(tokenStore());
//    }
//
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(database);
    }


    /**
     * AuthenticationManager 빈으로 등록
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // swagger 관련 리소스 시큐리티 필터 제거
        web.ignoring().antMatchers(
                "/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**").and();

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.   csrf().disable()
                .anonymous().disable()
                .authorizeRequests()
                .antMatchers("/api-docs/**").permitAll()
                .and()
                .formLogin()
                .and()
                .httpBasic()
        ;
    }

}
