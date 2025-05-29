package com.urlive.controller.dto.userUrl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTitleRequest(
        @NotBlank(message = "단축 URL에 대한 title을 작성해주세요.")
        @Size(max = 50)
        String newTitle
) {
}
