package com.apiboad6.reactjwttokenproject.config.jwt;

import com.apiboad6.reactjwttokenproject.dto.sign.UserInfoResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/*
 * 토큰을 쿠키에 담아 프론트에 전달하기 위한 방식
 * HTTPONLY를 이용하여 유저 정보를 서버 세션에서 관리
 * 이 프로젝트 이전에 해보던 쿠키 방식과 토큰방식으로 결합
 * 서버가가 꺼지거나 로그아웃이 되어서 세션이 사라지면 클라이언트에서도 로그아웃이 되게 하기 위함
 * 평시에는 서버에서 클라이언트에서 요청시 쿠키 안에 담긴 토큰 값으로 세션에 있는 값과 비교 후 값 전달
 * */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidity;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

//    private Key key;

    //키 고정
//    @PostConstruct
//    protected void init() {
//        byte[] keyBytes = Base64.getDecoder().decode(secretKey); // Base64 디코딩
//        key = Keys.hmacShaKeyFor(keyBytes);
//    }

    //키 생성을 메서드로 분리
    private SecretKey getSignInKey() { // 🔹 메서드로 분리
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    //토큰 생성
    public String createAccessToken(String email, int id, String username, String nickname, String roleName) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .subject(email)
                .claim("id", id)
                .claim("username", username)
                .claim("nickname", nickname)
                .claim("roleName", roleName)
                .issuedAt(now)
                .expiration(validity)
                .signWith(getSignInKey())
                .compact();
    }

    //리프레시 토큰 새성
    public String createRefreshToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(validity)
                .signWith(getSignInKey())
                .compact();
    }

    //소셜용 임시토큰
//    public String createTempToken(String email) {
//        return Jwts.builder()
//                .subject(email)
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
//                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS256)
//                .compact();
//    }


    //인증을 위한 고유 유저 정보
    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    //권한 제어를 위해
    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roleName", String.class);
    }

    //유저 정보 꺼내오기
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UserInfoResponse getUserInfoFromToken(String token) {
        Claims claims = getClaims(token);

        int id = claims.get("id", Integer.class);
        String email = claims.getSubject();
        String username = claims.get("username", String.class);
        String nickname = claims.get("nickname", String.class);
        String role = claims.get("roleName", String.class);

        return new UserInfoResponse(id, email, username, nickname, role);
    }

    //유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //Authentication 객체 생성(인증 객체 생성)
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        int id = claims.get("id", Integer.class);
        String email = claims.getSubject();
        String username = claims.get("username", String.class);
        String nickname = claims.get("nickname", String.class);
        String role = claims.get("roleName", String.class);

        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        List<GrantedAuthority> authorities = List.of(authority);

        CustomUserDetails userDetails = new CustomUserDetails(id, email, username, nickname, role, authorities);

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public ResponseCookie createHttpOnlyCookie(String token) {
        return ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // 배포 시 true + https
                .path("/")
                .maxAge(60 * 30) // 30분
                .sameSite("Strict")
                .build();
    }

    //로그아웃
    public ResponseCookie deleteCookie() {
        return ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // 배포 시 true + https
                .path("/")
                .maxAge(0) // 즉시 만료
                .sameSite("Strict")
                .build();
    }

}
/**
Authentication
Spring Security에서 인증된 사용자 정보를 담는 중앙 객체
이 객체는 로그인된 유저의 정보, 권한, 인증 상태 등을 가지고 있음.
언제든지 현재 로그인된 유저 정보를 확인하려면 이걸 꺼내서 쓰면 됨.
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

사용자 로그인 → JWT 토큰 검증 → 유저 정보 조회 → Authentication 객체 생성 → SecurityContext에 저장

사용자 로그인: 사용자가 로그인하면 (혹은 JWT 토큰을 가지고 요청하면),
Jwt 토큰 검증: JwtAuthenticationFilter 같은 인증 필터가 토큰을 검증하고,
유저 정보 조회: 토큰 안의 정보를 바탕으로 DB에서 유저 정보(UserDetails)를 가져와,
객체 생성: Authentication 구현체 (UsernamePasswordAuthenticationToken)을 생성해서,
저장: 이걸 SecurityContextHolder에 저장함.
SecurityContextHolder.getContext().setAuthentication(authentication);
이렇게 저장된 인증 정보는 모든 요청에서 공유

커스텀 유저 디테일이 가느한 이유
return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
구문으로 principal로 들어감
 */

/**
getAuthentication(token)
    ↓
1. getEmailFromToken(token)
   → 토큰의 Payload(Body)에서 subject(이메일) 꺼냄
    ↓
2. userDetailsService.loadUserByUsername(email)
   → 이메일 기준으로 DB에서 유저 정보 꺼냄
    ↓
3. new UsernamePasswordAuthenticationToken(...)
   → 인증된 Authentication 객체 생성 (Spring Security에서 로그인 성공 처리한 상태)

   [JWT 필터] → 토큰 꺼냄
          → 유효성 확인
          → JwtTokenProvider.getAuthentication(token) 호출
             → getEmailFromToken(token)
             → loadUserByUsername(email)
             → UsernamePasswordAuthenticationToken 반환
          → SecurityContextHolder 에 등록
   */
