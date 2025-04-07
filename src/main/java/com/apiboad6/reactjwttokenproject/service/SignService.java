package com.apiboad6.reactjwttokenproject.service;

import com.apiboad6.reactjwttokenproject.config.jwt.JwtTokenProvider;
import com.apiboad6.reactjwttokenproject.domain.member.Roles;
import com.apiboad6.reactjwttokenproject.dto.SignInQueryResult;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignService {
    private final SignRepository signRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public SignInResponse signIn(SignInRequest req) {

        SignInQueryResult queryResult = signRepository.findUserWithPasswordAndRole(req.getEmail())
                .orElseThrow(() -> new SignInFailureException("없는 이메일, 회원정보"));

        //디비 직접 입력한 비밀번호 변환용 (테스트용)
        if(!queryResult.getPassword().startsWith("$2a$")) {
            queryResult.setPassword(passwordEncoder.encode(queryResult.getPassword()));
        }

        // 🔹 비밀번호 검증
        validateSignInPassword(req.getPassword(), queryResult.getPassword());

        // 🔹 JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(queryResult.getEmail(), queryResult.getUsername(), queryResult.getNickname(), queryResult.getRoleName());
        String refreshToken = jwtTokenProvider.createRefreshToken(queryResult.getEmail());

        // 🔹 응답 DTO 생성
        return new SignInResponse(
                accessToken,
                refreshToken
        );
    }

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUp(req);
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        Roles roles = roleRepository.findById(2).orElseThrow(NotFoundRoleIdException::new);
        signRepository.save(SignUpRequest.toEntity(req, roles));
    }

    //비밀번호 검증
    private void validateSignInPassword(String reqPassword, String password) {
        if(!passwordEncoder.matches(reqPassword, password)) {
            throw new SignInFailureException("비밀번호 틀림");
        }
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
