package com.netflix.clone.helper.globalexceptionhandler;

public class AccountDeactivatedException extends RuntimeException {
    public AccountDeactivatedException(String message) {
        super(message);
    }
}
