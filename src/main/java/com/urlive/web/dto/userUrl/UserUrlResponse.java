package com.urlive.web.dto.userUrl;

import java.time.LocalDateTime;

public record UserUrlResponse(
        String originalUrl,
        String shortUrl,
        String title,
        LocalDateTime createdAt,
        Long viewCount
) {
}
