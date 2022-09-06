package com.example.mungta.filter;

import com.example.mungta.authentication.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class QuestionAuthFilter extends AbstractGatewayFilterFactory<QuestionAuthFilter.Config> {

    @Autowired
    private JwtProvider jwtProvider;

    public QuestionAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest(); //Pre Filter

            // Request Header 에 token 이 존재하지 않을 때
            if(!request.getHeaders().containsKey("Authorization")){
                return handleUnAuthorized(exchange); // 401 Error
            }

            // Request Header 에서 token 문자열 받아오기
            List<String> token = request.getHeaders().get("Authorization");
            String tokenString = Objects.requireNonNull(token).get(0);

            // 토큰 검증
            if(jwtProvider.isTokenExpired(tokenString)) {
                return handleUnAuthorized(exchange); // 토큰이 만료되었을 때
            }

            return chain.filter(exchange); // 토큰이 유효할 때
        });
    }

    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
    public static class Config {

    }
}
