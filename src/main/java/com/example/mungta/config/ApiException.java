package com.example.mungta.config;

import lombok.Getter;

public class ApiException extends RuntimeException {
    @Getter
    private ApiStatus apiStatus;

    public ApiException(ApiStatus apiStatus) {
        super(apiStatus.getMessage());
        this.apiStatus = apiStatus;
    }

    public ApiException(String apiStatus) {
        super(apiStatus);
    }
}
