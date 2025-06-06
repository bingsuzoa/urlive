package com.urlive.web.dto.url;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlCreateRequestTest {


    Validator setUp() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    /// //////해피 테스트
    @Test
    @DisplayName("originalUrl 전달 객체 생성 통과 테스트")
    void originalUrl_전달_객체_생성() {

        UrlCreateRequest request = new UrlCreateRequest("http://test.com");
        Assertions.assertThat(request.originalUrl()).isEqualTo("http://test.com");
    }

    @Test
    @DisplayName("originalUrl 미작성 시 예외")
    void original_url_미작성시_예외() {
        Validator validator = setUp();

        UrlCreateRequest request = new UrlCreateRequest("");
        Set<ConstraintViolation<UrlCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isViolation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof NotBlank);
        assertTrue(isViolation);
    }

    @ParameterizedTest
    @DisplayName("originalUrl 미작성 시 예외")
    @ValueSource(strings = {"http//test", "//test", "http://test", "test"})
    void 올바른_형태_아닐경우_예외(String value) {
        Validator validator = setUp();

        UrlCreateRequest request = new UrlCreateRequest(value);
        Set<ConstraintViolation<UrlCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean isViolation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof Pattern);
        assertTrue(isViolation);
    }
}
