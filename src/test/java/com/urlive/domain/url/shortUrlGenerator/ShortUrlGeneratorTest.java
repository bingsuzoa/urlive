package com.urlive.domain.url.shortUrlGenerator;

import com.urlive.domain.url.shortUrlGenerator.urlEncoder.UrlEncoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShortUrlGeneratorTest {


    @Test
    @DisplayName("단축 URL 생성 테스트")
    void 단축_URL_생성() {
        UrlEncoder mockEncoder = mock(UrlEncoder.class);
        ShortUrlGenerator shortUrlGenerator = new ShortUrlGenerator(mockEncoder);

        when(mockEncoder.encode(anyLong())).thenReturn("short_url");

        Assertions.assertThat(shortUrlGenerator.generateShortUrl()).isEqualTo("short_url");
    }
}
