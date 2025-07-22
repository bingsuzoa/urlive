package com.urlive.domain.url;

import com.urlive.domain.user.User;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UrlTest {

    private static final String originalUrl = "http://test.com";
    private static final String shortUrl = "testShortUrl";

    @Test
    @DisplayName("Url 객체 생성자 정상 작동 테스트")
    void 생성자_정상_작동_확인() {
        Country country = new Country("KR", "korea");
        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, country);
        Url url = new Url(user, originalUrl, shortUrl);

        Assertions.assertThat(url.getOriginalUrl()).isEqualTo(originalUrl);
        Assertions.assertThat(url.getShortUrl()).isEqualTo(shortUrl);
        Assertions.assertThat(url.getViewCount()).isEqualTo(0L);
    }


}
