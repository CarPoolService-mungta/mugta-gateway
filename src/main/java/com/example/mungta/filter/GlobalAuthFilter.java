package com.example.mungta.filter;

import com.example.mungta.authentication.JwtProvider;
import com.example.mungta.config.ApiException;
import com.example.mungta.config.ApiStatus;
import com.example.mungta.config.FilterExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class GlobalAuthFilter extends AbstractGatewayFilterFactory<GlobalAuthFilter.Config> {

    @Autowired
    private JwtProvider jwtProvider;

    public GlobalAuthFilter() {
        super(Config.class);
    }

    @Bean
    public ErrorWebExceptionHandler myExceptionHandler() {
        return new FilterExceptionHandler();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest(); //Pre Filter

            String path = request.getPath().value();
            if(path.contains("/auth/")){
                return chain.filter(exchange);
            }

            // Request Header 에 token 이 존재하지 않을 때
            if(!request.getHeaders().containsKey("Authorization")){
                throw new ApiException(ApiStatus.NO_TOKEN);
            }

            // Request Header 에서 token 문자열 받아오기
            List<String> token = request.getHeaders().get("Authorization");
            String tokenString = Objects.requireNonNull(token).get(0);
            // 토큰 검증
            if(jwtProvider.isTokenExpired(tokenString)) {
                throw new ApiException(ApiStatus.TOKEN_EXPIRED);
            }

            return chain.filter(exchange.mutate().request(
                    request.mutate()
                            .header("userId", jwtProvider.getUserIdFromToken(tokenString))
                            .header("name", jwtProvider.getNameFromToken(tokenString))
                            .header("email", jwtProvider.getEmailFromToken(tokenString))
                            .header("team", jwtProvider.getTeamFromToken(tokenString))
                            .header("userType", jwtProvider.getUserTypeFromToken(tokenString))
                            .header("driverYn", jwtProvider.getDriverYnTypeFromToken(tokenString))
                            .build()
            ).build()); // 토큰이 유효할 때
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
