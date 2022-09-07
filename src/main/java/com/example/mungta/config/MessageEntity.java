package com.example.mungta.config;

import lombok.*;

import java.io.Serializable;

@Getter
public class MessageEntity<T> implements Serializable {
    private Integer status;
    private String message;

    private MessageEntity(ApiStatus apiStatus) {
        this.status = apiStatus.getCode();
        this.message = apiStatus.getMessage();
    }

    public static MessageEntity of(ApiStatus apiStatus){
        return new MessageEntity(apiStatus);
    }
}
