package com.urlive.domain.url.shortUrlGenerator.urlEncoder;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HashidsEncoderTest {

    @Test
    @DisplayName("Original Url 인코딩 테스트")
    void 인코딩() {
        Long id = 5621L;
        String result = "EAoE7BGL";

        HashidsEncoder encoder = new HashidsEncoder();
        Assertions.assertThat(encoder.encode(id)).isEqualTo(result);
    }

    @Test
    @DisplayName("shortUrl 디코딩 테스트")
    void 디코딩() {
        Long id = 5621L;
        String shortUrl = "EAoE7BGL";

        HashidsEncoder encoder = new HashidsEncoder();
        Assertions.assertThat(encoder.decode(shortUrl)).isEqualTo(id);
    }
}
