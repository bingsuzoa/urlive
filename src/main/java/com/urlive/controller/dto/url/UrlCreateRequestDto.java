package com.urlive.controller.dto.url;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UrlCreateRequestDto(
        @NotBlank(message = "originalUrl은 필수입니다.")
        @Pattern(
                regexp = "^(https?://)[\\w\\-]+(\\.[\\w\\-]+)+[/#?]?.*$",
                message = "올바른 URL 형식이 아닙니다."
        )
        String rawUrl
) {
}
