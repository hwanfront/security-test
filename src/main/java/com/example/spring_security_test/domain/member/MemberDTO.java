package com.example.spring_security_test.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotEmpty(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 2, max = 20, message = "아이디는 2자 이상 20자 이하로 입력해주세요.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String passwordConfirm;

    // 비밀번호와 비밀번호 확인이 일치하는지 검증
    public boolean isPasswordMatching() {
        return password != null && password.equals(passwordConfirm);
    }
}
