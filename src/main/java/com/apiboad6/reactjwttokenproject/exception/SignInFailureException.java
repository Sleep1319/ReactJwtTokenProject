package com.apiboad6.reactjwttokenproject.exception;

public class SignInFailureException extends RuntimeException {
    public SignInFailureException(String message) {
        super(message);
    }
}
