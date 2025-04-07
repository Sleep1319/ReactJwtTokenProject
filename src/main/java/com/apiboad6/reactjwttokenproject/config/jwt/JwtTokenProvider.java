package com.apiboad6.reactjwttokenproject.config.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
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
    public String createAccessToken(String email, String username, String nickname, String roleName) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .subject(email)
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

    //Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        String role = getRole(token);

        GrantedAuthority authority = new SimpleGrantedAuthority(role); // 단일 권한 생성
        List<GrantedAuthority> authorities = List.of(authority); // 리스트로 감싸기

        UserDetails userDetails = new User(email, "", authorities);

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }


}
