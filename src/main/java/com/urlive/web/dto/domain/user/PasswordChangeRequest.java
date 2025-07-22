package com.urlive.web.dto.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordChangeRequest(

        @NotBlank(message = "비밀번호 작성은 필수입니다.")
        String rawNewPassword
) {
}
