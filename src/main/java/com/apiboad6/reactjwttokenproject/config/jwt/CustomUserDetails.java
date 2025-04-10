package com.apiboad6.reactjwttokenproject.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final int id;
    private final String email;
    private final String username;
    private final String nickname;
    private final String roleName;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return ""; // 패스워드는 인증에 안 씀
    }

    @Override
    public String getUsername() {
        return email; // 기본은 이메일로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
