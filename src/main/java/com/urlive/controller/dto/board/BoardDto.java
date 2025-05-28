package com.urlive.controller.dto.board;

import java.time.LocalDateTime;

public record BoardDto(
        String name,
        String shortUrl,
        String title,
        LocalDateTime createdAt,
        Long viewCount
) {
}
