package com.apiboad6.reactjwttokenproject.dto;

import com.apiboad6.reactjwttokenproject.domain.member.Member;
import com.apiboad6.reactjwttokenproject.domain.member.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "이메일 형식을 다릅니다")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호에 공백이 있거나 입력이 안되어있음")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;

    @NotBlank(message = "이름에 공백이 있거나 입력값이 없습니다")
    @Size(min = 2, message = "2글자 이상 적어주십시오")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "사용자 이름은 한글 또는 알파벳만 가능합니다")
    private String username;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, message = "2글자 이상 적어주십시오")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "닉네임은 한글 또는 알파벳만 입력해주세요.")
    private String nickname;

    //패스워드 인코더 활용시 수정 필요
    public static Member toEntity(SignUpRequest req, Roles role) {
        return new Member(req.email, req.password, req.username, req.nickname, role);
    }
}
