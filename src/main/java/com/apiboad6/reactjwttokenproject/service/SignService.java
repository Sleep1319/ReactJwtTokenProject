package com.apiboad6.reactjwttokenproject.service;

import com.apiboad6.reactjwttokenproject.domain.member.Roles;
import com.apiboad6.reactjwttokenproject.dto.SignInRequest;
import com.apiboad6.reactjwttokenproject.dto.SignInResponse;
import com.apiboad6.reactjwttokenproject.dto.SignUpRequest;
import com.apiboad6.reactjwttokenproject.exception.MemberEmailAlreadyExistsException;
import com.apiboad6.reactjwttokenproject.exception.MemberNicknameAlreadyExistsException;
import com.apiboad6.reactjwttokenproject.exception.NotFoundRoleIdException;
import com.apiboad6.reactjwttokenproject.exception.SignInFailureException;
import com.apiboad6.reactjwttokenproject.repository.RoleRepository;
import com.apiboad6.reactjwttokenproject.repository.SignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignService {
    private final SignRepository signRepository;
    private final RoleRepository roleRepository;

    public SignInResponse signIn(SignInRequest req) {
        return signRepository.signInQuery(req.getEmail(), req.getPassword())
                .orElseThrow(() -> new SignInFailureException("로그인 요청 결과가 없음"));
    }

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUp(req);
        Roles roles = roleRepository.findById(2).orElseThrow(NotFoundRoleIdException::new);
        signRepository.save(SignUpRequest.toEntity(req, roles));
    }

    private void validateSignUp(SignUpRequest req) {
        if(signRepository.existsByEmail(req.getEmail())) {
            throw new MemberEmailAlreadyExistsException();
        }
        if(signRepository.existsByNickname(req.getNickname())) {
            throw new MemberNicknameAlreadyExistsException();
        }
    }

}
