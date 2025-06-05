package com.urlive.domain.url;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UrlTest {

    private static final String originalUrl = "http://test.com";
    private static final String shortUrl = "testShortUrl";

    @Test
    @DisplayName("Url 객체 생성자 정상 작동 테스트")
    void 생성자_정상_작동_확인() {
        Url url = new Url(originalUrl, shortUrl);

        Assertions.assertThat(url.getOriginalUrl()).isEqualTo(originalUrl);
        Assertions.assertThat(url.getShortUrl()).isEqualTo(shortUrl);
        Assertions.assertThat(url.getViewCount()).isEqualTo(0L);
        Assertions.assertThat(url.getUsers()).isEmpty();
        Assertions.assertThat(url.getViews()).isEmpty();
    }


}
