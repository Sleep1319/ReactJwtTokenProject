package com.apiboad6.reactjwttokenproject.handler;

import com.apiboad6.reactjwttokenproject.exception.MemberEmailAlreadyExistsException;
import com.apiboad6.reactjwttokenproject.exception.MemberNicknameAlreadyExistsException;
import com.apiboad6.reactjwttokenproject.exception.NotFoundRoleIdException;
import com.apiboad6.reactjwttokenproject.exception.SignInFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailExistsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "중복된 이메일 입니다")
        );
    }

    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    public ResponseEntity<?> handleNicknameExistsException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "중복된 닉네임입니다.")
        );
    }

    @ExceptionHandler(NotFoundRoleIdException.class)
    public ResponseEntity<?> handleNotFoundRoleIdException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "저장된 등급 정보가 없습니다.")
        );
    }

    @ExceptionHandler(SignInFailureException.class)
    public ResponseEntity<?> handleSignInFailureException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "이메일 또는 비밀번호가 다릅니다.")
            );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "요청 처리 중 오류 발생")
        );
    }
}
