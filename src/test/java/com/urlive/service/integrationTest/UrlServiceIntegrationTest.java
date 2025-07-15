package com.urlive.service.integrationTest;

import com.urlive.config.AsyncSyncTestConfig;
import com.urlive.config.TestRedisConfig;
import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.url.UrlService;
import com.urlive.web.dto.domain.url.UrlCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
public class UrlServiceIntegrationTest {

    @Autowired
    private UrlService urlService;

    @Autowired
    private UrlRepository urlRepository;

    private static final String originalUrl = "http://urlive.com";
    private static final String shortUrl = "KD";

    @Test
    @DisplayName("단축URL 디코딩 테스트")
    void 단축_url_디코딩_테스트() {
        Url url = new Url(originalUrl, shortUrl);
        urlRepository.save(url);
        Assertions.assertThat(urlService.decodeShortUrl(shortUrl)).isEqualTo(originalUrl);
    }

    @Test
    @DisplayName("단축 url 생성 및 저장하는 테스트")
    void 단축_url_생성_테스트() {
        UrlCreateRequest request = new UrlCreateRequest(originalUrl);

        Url url = urlService.findOrCreateShortUrl(request);
        Assertions.assertThat(url).isNotNull();
    }

    /// //에러 테스트
    @Test
    @DisplayName("단축URL 디코딩 테스트 : 조회되는 Url이 없으면 예외")
    void 단축_url_디코딩_예외() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            urlService.decodeShortUrl(shortUrl);
        });
    }
}
