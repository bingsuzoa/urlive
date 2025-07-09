package com.urlive.web.dto.user;

import com.urlive.domain.user.User;
import com.urlive.domain.user.option.Gender;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserCreateRequestTest {


    Validator setUp() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    /// ////////해피 테스트
    @Test
    @DisplayName("정상 user 생성 테스트")
    void 정상_user_생성() {
        User user = new User("test", "01012345678", "password1111", 20250605, Gender.MEN, new com.urlive.domain.user.option.country.Country("KR", "대한민국"));

        Assertions.assertThat(user.getName()).isEqualTo("test");
        Assertions.assertThat(user.getPhoneNumber()).isEqualTo("01012345678");
        Assertions.assertThat(user.getPassword()).isEqualTo("password1111");
        Assertions.assertThat(user.getAge()).isEqualTo(20250605);
        Assertions.assertThat(user.getGender()).isEqualTo(Gender.MEN);
        Assertions.assertThat(user.getCountry().getName()).isEqualTo("대한민국");
    }

    /// ////////예외 테스트
    @Test
    @DisplayName("이름 공백일 경우 예외")
    void 이름_공백() {
        Validator validator = setUp();
        UserCreateRequest request = new UserCreateRequest("", "01012345678", "password1111", 20250605, 1, "KR");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isValidation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof NotBlank);
        assertTrue(isValidation);
    }

    @Test
    @DisplayName("이름 10글자 이상 예외")
    void 이름_글자수_초과() {
        Validator validator = setUp();
        UserCreateRequest request = new UserCreateRequest("alkjflkajflk", "01012345678", "password1111", 20250605, 1, "KR");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isValidation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof Size);
        assertTrue(isValidation);
    }

    @Test
    @DisplayName("휴대폰 번호 정확히 입력안할 경우 예외")
    void 휴대폰번호_예외() {
        Validator validator = setUp();
        UserCreateRequest request = new UserCreateRequest("test123", "010-1234-5678", "password1111", 20250605, 1, "KR");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isValidation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof Size);
        assertTrue(isValidation);
    }

    @Test
    @DisplayName("비밀번호 미작성 시 예외")
    void 비밀번호_미작성() {
        Validator validator = setUp();
        UserCreateRequest request = new UserCreateRequest("test123", "010-1234-5678", "", 20250605, 1, "KR");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isValidation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof NotBlank);
        assertTrue(isValidation);
    }

    @ParameterizedTest
    @DisplayName("8-15자 범위 초과 시 예외")
    @ValueSource(strings = {"test1", "test123457878787878"})
    void 비밀번호_글자_범위_초과(String value) {
        Validator validator = setUp();
        UserCreateRequest request = new UserCreateRequest("test123", "010-1234-5678", value, 20250605, 1, "KR");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isValidation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof Size);
        assertTrue(isValidation);
    }

    @ParameterizedTest
    @DisplayName("영문만 있거나 숫자만 있으면 예외")
    @ValueSource(strings = {"testOnlyEng", "123456789"})
    void 비밀번호_제약_조건(String value) {
        Validator validator = setUp();
        UserCreateRequest request = new UserCreateRequest("test123", "010-1234-5678", value, 20250605, 1, "KR");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isValidation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof Pattern);
        assertTrue(isValidation);
    }

    @Test
    @DisplayName("생년월일 null일 경우 예외")
    void 생년월일_null() {
        Validator validator = setUp();
        UserCreateRequest request = new UserCreateRequest("test123", "010-1234-5678", "test123", null, 1, "KR");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isValidation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof NotNull);
        assertTrue(isValidation);
    }

    @Test
    @DisplayName("성별 null일 경우 예외")
    void 성별_null() {
        Validator validator = setUp();
        UserCreateRequest request = new UserCreateRequest("test123", "010-1234-5678", "test123", 20250603, null, "KR");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isValidation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof NotNull);
        assertTrue(isValidation);
    }

    @Test
    @DisplayName("국가 null일 경우 예외")
    void 국가_null() {
        Validator validator = setUp();
        UserCreateRequest request = new UserCreateRequest("test123", "010-1234-5678", "test123", 20250603, 1, "");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isValidation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof NotBlank);
        assertTrue(isValidation);
    }
}
