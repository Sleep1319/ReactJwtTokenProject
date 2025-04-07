package com.apiboad6.reactjwttokenproject.controller.sign;

import com.apiboad6.reactjwttokenproject.dto.SignInRequest;
import com.apiboad6.reactjwttokenproject.dto.SignInResponse;
import com.apiboad6.reactjwttokenproject.dto.SignUpRequest;
import com.apiboad6.reactjwttokenproject.service.SignService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @PostMapping("/api/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest req, HttpSession session) {
        SignInResponse res = signService.signIn(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/api/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest req) {
        signService.signUp(req);
        return ResponseEntity.ok().build();
    }
}
