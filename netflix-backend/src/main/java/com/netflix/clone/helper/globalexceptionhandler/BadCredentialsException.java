package com.netflix.clone.helper.globalexceptionhandler;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
