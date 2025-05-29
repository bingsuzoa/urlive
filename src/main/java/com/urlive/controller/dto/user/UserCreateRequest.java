package com.urlive.controller.dto.user;

import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "이름 작성은 필수입니다.")
        @Size(max = 10)
        String name,

        @NotBlank(message = "휴대폰 번호 작성은 필수입니다.")
        @Size(max = 11, message = "휴대폰 번호는 '01012345678'과 같은 형식으로 작성해주세요.")
        String phoneNumber,

        @NotBlank(message = "비밀번호 작성은 필수입니다.")
        @Size(max = 15, message = "15자 내외로 작성해주세요.")
        String password,

        @NotBlank(message = "나이 작성은 필수입니다. ")
        @Size(min = 4, max = 4, message = "'2025'와 같은 형태로 작성해주세요.")
        int age,

        @NotBlank(message = "성별 체크란이 비어있습니다.")
        int gender,

        @NotBlank(message = "국가 체크란이 비어있습니다.")
        int country
) {

    public User toEntity() {
        return new User(name, phoneNumber, password, age, Gender.getGender(gender), Country.getCountry(country));
    }
}
