package com.netflix.clone.helper.globalexceptionhandler;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String message) {
        super(message);
    }
}
