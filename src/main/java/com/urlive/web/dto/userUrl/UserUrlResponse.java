package com.urlive.web.dto.userUrl;

import java.time.LocalDateTime;

public record UserUrlResponse(
        Long id,
        String originalUrl,
        String shortUrl,
        String title,
        LocalDateTime createdAt,
        Long viewCount
) {
}
