package com.homewrk.backpack.api.login.controller;


import io.swagger.annotations.*;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;


/**
 * 리소스 서버 로그인 controller
 */
@Controller
@RequestMapping("/api")
public class LoginController {


    @Autowired
    RedisTemplate redisTemplate;

    @Value("${backpack.oauth.access-expire}")
    private int accessExpire;

    /**
     * redis 를 통한 로그아웃 구현
     * @param request
     * @return
     */

    @ApiOperation(
            value = "로그아웃"
            , notes = "JWT 토큰 블랙리스트"
            , httpMethod = "GET"
    )
    @ApiResponses({
            @ApiResponse(code = 204 , message = "로그아웃 성공" )
            , @ApiResponse(code = 500 , message = "서버에러" )
    })
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        redisTemplate.opsForValue().get(token);
        redisTemplate.opsForValue().set(
                token
                , "1"
                , accessExpire
                , TimeUnit.SECONDS
        );

        return new ResponseEntity<String>("", HttpStatus.NO_CONTENT);
    }


}
