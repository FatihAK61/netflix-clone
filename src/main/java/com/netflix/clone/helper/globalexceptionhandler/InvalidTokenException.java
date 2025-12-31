package com.netflix.clone.helper.globalexceptionhandler;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
