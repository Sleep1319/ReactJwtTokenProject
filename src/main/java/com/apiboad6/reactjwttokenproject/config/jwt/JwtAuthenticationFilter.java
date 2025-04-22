package com.apiboad6.reactjwttokenproject.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    // 매 요청시마다 쿠키에서 토큰 꺼내서 인증
    /**
     *
     StringUtils.hasText(token): null/빈문자 체크
     jwtTokenProvider.validateToken(token): 토큰 유효성 검사 (서명, 만료 등)
     getAuthentication(token): 토큰에서 유저 정보를 꺼내서 Authentication 객체 생성
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(req);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        //필터로 넘기기
        //이걸 호출해야 다음 필터 or DispatcherServlet까지 넘어감
        filterChain.doFilter(req, res);
    }

    private String resolveToken(HttpServletRequest req) {
        //헤더 우선 찾기
        String bearerToken = req.getHeader("Authorization");
        //Bearer <토큰> 형식이면 "Bearer " 떼고 순수 토큰만 추출
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        // Authorization 헤더가 없다면 쿠키에서 찾기
        if (req.getCookies() != null) {
            for (var cookie : req.getCookies()) {
                if (cookie.getName().equals("token")) {
                    return cookie.getValue();
                }
            }
        }
        //JWT 인증이 필수적인 API에서는 403 Forbidden 응답을 주도록 변경 가능
        return null;
    }
}
/**
    [요청 들어옴]
      ↓
[JwtAuthenticationFilter 실행]
      ↓
토큰 꺼내기 (Header or Cookie)
      ↓
토큰 유효성 검사
      ↓
유저 정보 추출 (getAuthentication)
      ↓
SecurityContextHolder 에 저장
      ↓
다음 필터로 넘기기
위 필터가 인증을 대신 하기에
컨트롤러에서는 간단히 @AuthenticationPrincipal 이나 SecurityContextHolder로 로그인 유저 정보 가져올 수 있다
*/
