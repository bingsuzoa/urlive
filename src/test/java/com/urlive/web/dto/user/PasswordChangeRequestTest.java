package com.urlive.web.dto.user;

import com.urlive.web.dto.domain.user.PasswordChangeRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordChangeRequestTest {

    Validator setUp() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("비밀번호 get테스트")
    void 비밀번호_얻기() {
        PasswordChangeRequest request = new PasswordChangeRequest("password123");
        Assertions.assertThat(request.rawNewPassword()).isEqualTo("password123");
    }

    /// ///////예외 테스트

    @Test
    @DisplayName("비밀번호 공백으로 작성 시 예외 발생")
    void 비밀번호_공백() {
        Validator validator = setUp();
        PasswordChangeRequest request = new PasswordChangeRequest("");
        Set<ConstraintViolation<PasswordChangeRequest>> violations = validator.validate(request);

        boolean isViolation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof NotBlank);

        assertTrue(isViolation);
    }

    @ParameterizedTest
    @DisplayName("영문 + 숫자 조합이 아닌 경우 예외 발생")
    @ValueSource(strings = {"onlyEnglish", "12345678"})
    void 영문_숫자조합(String value) {
        Validator validator = setUp();

        PasswordChangeRequest request = new PasswordChangeRequest(value);
        Set<ConstraintViolation<PasswordChangeRequest>> violations = validator.validate(request);

        boolean isViolation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof Pattern);
        assertTrue(isViolation);
    }

    @ParameterizedTest
    @DisplayName("글자 수 예외 테스트")
    @ValueSource(strings = {"test1", "test112345test123456"})
    void 글자수_제한(String value) {
        Validator validator = setUp();

        PasswordChangeRequest request = new PasswordChangeRequest(value);
        Set<ConstraintViolation<PasswordChangeRequest>> violations = validator.validate(request);

        boolean isViolation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof Size);
        assertTrue(isViolation);
    }
}
