package com.apiboad6.reactjwttokenproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class SignInQueryResult {
    private int id;
    private String email;
    private String username;
    private String nickname;
    private String password;
    private String roleName;
}
