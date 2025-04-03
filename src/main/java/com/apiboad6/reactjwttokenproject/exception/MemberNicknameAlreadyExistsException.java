package com.apiboad6.reactjwttokenproject.exception;

public class MemberNicknameAlreadyExistsException extends RuntimeException{

    public MemberNicknameAlreadyExistsException() { super(); }
    public MemberNicknameAlreadyExistsException(String message) {
        super(message);
    }
}
