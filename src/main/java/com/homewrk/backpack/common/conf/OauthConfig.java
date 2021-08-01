package com.homewrk.backpack.common.conf;


import com.homewrk.backpack.api.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;

/**
 * Oauth 토큰 관련 설정처리
 */
@Configuration
@EnableAuthorizationServer
public class OauthConfig extends AuthorizationServerConfigurerAdapter {


    // Oauth 클라이언트 아이디
    @Value("${backpack.oauth.client}")
    private String oauthClient;
    // Oauth 클라이언트 아이디
    @Value("${backpack.oauth.secret}")
    private String oauthSecret;
    // Oauth acess token 유효시간
    @Value("${backpack.oauth.access-expire}")
    private int accessExpire;
    // Oauth refresh token 유효시간
    @Value("${backpack.oauth.refresh-expire}")
    private int refreshExpire;

    @Resource
    private MemberService memberService;

    @Resource
    private TokenStore tokenStore;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private PasswordEncoder encoder;

    @Resource
    private ApprovalStore approvalStore;

    @Resource
    private JwtAccessTokenConverter jwtAccessTokenConverter;

//    @Resource
//    private AuthorizationCodeServices authorizationCodeServices;


    /**
     * token 설정
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(oauthClient)
                .secret(encoder.encode(oauthSecret))
                .authorizedGrantTypes("authorization_code", "implicit", "password", "client_credentials", "refresh_token")
                .scopes("read","write","trust")
                .redirectUris("/oauth/callback")
                .accessTokenValiditySeconds(accessExpire)
                .refreshTokenValiditySeconds(refreshExpire)


        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        endpoints.userDetailsService(memberService)
                 .tokenStore(tokenStore)
                 .authenticationManager(authenticationManager)
                 .approvalStore(approvalStore)
                 .accessTokenConverter(jwtAccessTokenConverter)
                 //.authorizationCodeServices(authorizationCodeServices)



        ;

    }
}
