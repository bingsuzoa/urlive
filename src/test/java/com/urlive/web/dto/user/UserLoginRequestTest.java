package com.urlive.web.dto.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import org.assertj.core.api.Assertions;
import jakarta.validation.constraints.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public class UserLoginRequestTest {

    Validator setUp() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    /// /////////해피 테스트
    @Test
    @DisplayName("정상 로그인 확인")
    void 정상_로그인_확인() {
        UserLoginRequest request = new UserLoginRequest("01022223333", "user1111");

        Assertions.assertThat(request.password()).isEqualTo("user1111");
        Assertions.assertThat(request.phoneNumber()).isEqualTo("01022223333");
    }

    /// /////////에러 테스트
    @Test
    @DisplayName("로그인 에러 확인 : 휴대폰 형식이 다를 경우 예외 발생")
    void 로그인_에러() {
        Validator validator = setUp();
        UserLoginRequest request = new UserLoginRequest("010-2222-3333", "user1111");
        Set<ConstraintViolation<UserLoginRequest>> violations = validator.validate(request);

        boolean isViolation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof Pattern);

        assertTrue(isViolation);
    }
}
