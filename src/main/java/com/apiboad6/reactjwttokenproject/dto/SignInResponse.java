package com.apiboad6.reactjwttokenproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {

    private String accessToken;
    private String refreshToken;

}
