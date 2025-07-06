package com.urlive.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(
        @NotBlank(message = "휴대폰 번호 작성은 필수입니다.")
        @Size(max = 11, message = "휴대폰 번호는 '01012345678'과 같은 형식으로 작성해주세요.")
        String phoneNumbr,

        @NotBlank(message = "비밀번호 작성은 필수입니다.")
        @Size(min = 8, max = 15, message = "15자 내외로 작성해주세요.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$", message = "영문자와 숫자를 포함해야 합니다.")
        String password
) {
}
