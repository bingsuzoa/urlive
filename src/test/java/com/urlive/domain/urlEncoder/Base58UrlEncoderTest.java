package com.urlive.domain.urlEncoder;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class Base58UrlEncoderTest {

    @Test
    @DisplayName("Original Url 인코딩 테스트")
    void 인코딩() {
        Long id = 5621L;
        String encodingResult = "2fv";

        Base58UrlEncoder encoder = new Base58UrlEncoder();
        Assertions.assertThat(encoder.encode(id)).isEqualTo(encodingResult);

    }

    @Test
    @DisplayName("shortUrl 디코딩 테스트")
    void 디코딩() {
        Long id = 5621L;
        String shortUrl = "2fv";

        Base58UrlEncoder encoder = new Base58UrlEncoder();
        Assertions.assertThat(encoder.decode(shortUrl)).isEqualTo(id);

    }
}
