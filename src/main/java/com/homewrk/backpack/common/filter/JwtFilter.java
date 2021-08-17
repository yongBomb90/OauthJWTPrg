package com.homewrk.backpack.common.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;


@Component
public class JwtFilter extends OncePerRequestFilter {



    RedisTemplate redisTemplate;

    @Autowired
    public JwtFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setDefaultSerializer(new StringRedisSerializer());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 로그아웃 블랙리스트 검사처리
        String token = request.getHeader("Authorization");

        if ( token != null && redisTemplate.opsForValue().get(token) != null ) {
            SecurityContextHolder.getContext().setAuthentication(null);
            request.getSession().removeAttribute("Authorization");
        }
        filterChain.doFilter(request, response);
    }

    public static void main(String[] args) {
        Stream<String> strStream = Stream.of(new String[]{"1","2","3"});
        Stream<Integer> intStream =   strStream.distinct().map(o1 -> Integer.parseInt(o1));
        System.out.println(intStream.peek(System.out::println).count());
    }
}
