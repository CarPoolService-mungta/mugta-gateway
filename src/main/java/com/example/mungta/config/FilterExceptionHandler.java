package com.example.mungta.config;

import com.google.gson.Gson;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.SerializationUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class FilterExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(
            ServerWebExchange exchange, Throwable ex) {
        ApiStatus apiStatus = ApiStatus.UNEXPECTED_ERROR;
        if (ex.getClass() == ApiException.class) {
            apiStatus = ((ApiException) ex).getApiStatus();
        }

        Gson gson = new Gson();
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(gson.toJson(MessageEntity.of(apiStatus)).getBytes(StandardCharsets.UTF_8));
        exchange.getResponse().setStatusCode(apiStatus.getHttpStatus());
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
