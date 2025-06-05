package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.url.shortUrlGenerator.urlEncoder.Base62UrlEncoder;
import com.urlive.domain.url.shortUrlGenerator.urlEncoder.UrlEncoder;
import com.urlive.web.dto.url.UrlCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    UrlRepository urlRepository = mock(UrlRepository.class);

    private UrlEncoder urlEncoder = new Base62UrlEncoder();

    UrlService urlService = new UrlService(urlRepository, urlEncoder);

    private static final String originalUrl = "http://urlive.com";
    private static final String shortUrl = "KD";

    @Test
    @DisplayName("단축URL 디코딩 테스트")
    void 단축_url_디코딩_테스트() {
        String originalUrl = "http://urlive.com";
        Url url = new Url(originalUrl);
        when(urlRepository.findUrlByShortUrl(any())).thenReturn(Optional.of(url));
        Assertions.assertThat(urlService.decodeShortUrl(shortUrl)).isEqualTo(originalUrl);
    }

    @Test
    @DisplayName("조회수 증가 확인 테스트")
    void 조회수_증가() {
        String originalUrl = "http://urlive.com";
        Url url = new Url(originalUrl);
        when(urlRepository.increaseViewCount(any())).thenReturn(1);
        when(urlRepository.findUrlByShortUrl(any())).thenReturn(Optional.of(url));
        Assertions.assertThat(urlService.decodeShortUrl(shortUrl)).isNotNull();
    }

    @Test
    @DisplayName("단축 url생성하는 테스트")
    void 단축_url_생성_테스트() {
        UrlCreateRequest request = new UrlCreateRequest(originalUrl);
        Url url = new Url(originalUrl);
        ReflectionTestUtils.setField(url, "id", 1253L);

        when(urlRepository.findUrlByOriginalUrl(any())).thenReturn(Optional.empty());
        when(urlRepository.save(any())).thenReturn(url);

        Assertions.assertThat(urlService.findOrCreateShortUrl(request)).isEqualTo(shortUrl);
    }
}
