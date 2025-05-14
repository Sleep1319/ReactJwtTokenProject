package com.apiboad6.reactjwttokenproject.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialSignUpRequest {
    private String email;
    private String username;
    private String nickname;
    private String provider;
    private String providerId;
}
