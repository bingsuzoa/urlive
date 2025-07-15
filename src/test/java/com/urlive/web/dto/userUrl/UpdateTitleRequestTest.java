package com.urlive.web.dto.userUrl;

import com.urlive.web.dto.domain.userUrl.UpdateTitleRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Size;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UpdateTitleRequestTest {


    Validator setUp() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }


    /// //////////해피 테스트
    @Test
    @DisplayName("타이틀 변경하기")
    void 타이틀_변경() {
        UpdateTitleRequest updateTitleRequest = new UpdateTitleRequest("title");

        Assertions.assertThat(updateTitleRequest.newTitle()).isEqualTo("title");
    }


    /// //////////예외 테스트
    @Test
    @DisplayName("타이틀 변경 시 입력이 없다면 예외 발생")
    void 타이틀_변경시_입력없으면_예외() {
        Validator validator = setUp();
        UpdateTitleRequest updateTitleRequest = new UpdateTitleRequest("");

        Set<ConstraintViolation<UpdateTitleRequest>> violations = validator.validate(updateTitleRequest);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("50자 이상이면 예외 발생")
    void 글자수_50_이상_예외_발생() {
        Validator validator = setUp();
        String longTitle = "a".repeat(51);
        UpdateTitleRequest updateTitleRequest = new UpdateTitleRequest(longTitle);

        Set<ConstraintViolation<UpdateTitleRequest>> violations = validator.validate(updateTitleRequest);
        assertFalse(violations.isEmpty());
        boolean hasSizeViolation = violations.stream()
                .anyMatch(v -> v.getConstraintDescriptor().getAnnotation() instanceof Size);

        assertTrue(hasSizeViolation);
    }


}
