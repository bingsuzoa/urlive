package com.urlive.web.dto.user;

import com.urlive.domain.user.User;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "이름 작성은 필수입니다.")
        @Size(max = 10)
        String name,

        @NotBlank(message = "휴대폰 번호 작성은 필수입니다.")
        @Size(max = 11, message = "휴대폰 번호는 '01012345678'과 같은 형식으로 작성해주세요.")
        String phoneNumber,

        @NotBlank(message = "비밀번호 작성은 필수입니다.")
        @Size(min = 8, max = 15, message = "15자 내외로 작성해주세요.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$", message = "영문자와 숫자를 포함해야 합니다.")
        String password,

        @NotNull(message = "생년월일 체크란이 비어있습니다.")
        Integer age,

        @NotNull(message = "성별 체크란이 비어있습니다.")
        Integer gender,

        @NotBlank(message = "국가 체크란이 비어있습니다.")
        String isoCode
) {

    public User toEntityWithEncodedPassword(String encodedPassword, Country country) {
        return new User(name, phoneNumber, encodedPassword, age, Gender.getGender(gender), country);
    }
}
