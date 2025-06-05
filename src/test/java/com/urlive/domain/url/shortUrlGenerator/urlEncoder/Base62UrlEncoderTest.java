package com.urlive.domain.url.shortUrlGenerator.urlEncoder;

import com.urlive.domain.url.shortUrlGenerator.urlEncoder.Base62UrlEncoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Base62UrlEncoderTest {

    @Test
    @DisplayName("Original Url 인코딩 테스트")
    void 인코딩() {
        Long id = 5621L;
        String encodingResult = "1Sf";

        Base62UrlEncoder encoder = new Base62UrlEncoder();
        Assertions.assertThat(encoder.encode(id)).isEqualTo(encodingResult);

    }

    @Test
    @DisplayName("shortUrl 디코딩 테스트")
    void 디코딩() {
        Long id = 5621L;
        String shortUrl = "1Sf";

        Base62UrlEncoder encoder = new Base62UrlEncoder();
        Assertions.assertThat(encoder.decode(shortUrl)).isEqualTo(id);

    }
}
