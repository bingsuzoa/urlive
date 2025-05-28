package com.urlive.controller.dto.userUrl;

import java.time.LocalDateTime;

public record UserUrlResponseDto(
        String name,
        String originalUrl,
        String shortUrl,
        String title,
        LocalDateTime createdAt,
        Long viewCount
) {
}
