package com.apiboad6.reactjwttokenproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {

    private int id;
    private String email;
    private String username;
    private String nickname;
    private String roleName;
}
