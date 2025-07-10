package com.urlive.web.dto.userUrl;

import com.urlive.web.dto.domain.userUrl.UserUrlResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class UserUrlResponseTest {


    @Test
    @DisplayName("UserUrlResponse 생성 테스트")
    void UserUrlResponse_생성_테스트() {
        UserUrlResponse response = new UserUrlResponse(
                1L,
                "https://example.com",
                "abc123",
                "title",
                LocalDateTime.of(2025, 6, 6, 12, 0),
                100L
        );

        Assertions.assertThat(response.originalUrl()).isEqualTo("https://example.com");
        Assertions.assertThat(response.shortUrl()).isEqualTo("abc123");
        Assertions.assertThat(response.title()).isEqualTo("title");
        Assertions.assertThat(response.createdAt()).isEqualTo(LocalDateTime.of(2025, 6, 6, 12, 0));
        Assertions.assertThat(response.viewCount()).isEqualTo(100L);
    }
}
