package com.apiboad6.reactjwttokenproject.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInQueryResult {
    private int id;
    private String email;
    private String username;
    private String nickname;
    private String password;
    private String roleName;
}
