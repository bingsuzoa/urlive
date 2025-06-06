package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.url.shortUrlGenerator.ShortUrlGenerator;
import com.urlive.web.dto.url.UrlCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    UrlRepository urlRepository = mock(UrlRepository.class);
    ShortUrlGenerator shortUrlGenerator = mock(ShortUrlGenerator.class);

    UrlService urlService = new UrlService(urlRepository, shortUrlGenerator);

    private static final String originalUrl = "http://urlive.com";
    private static final String shortUrl = "KD";

    @Test
    @DisplayName("단축URL 디코딩 테스트")
    void 단축_url_디코딩_테스트() {
        Url url = new Url(originalUrl, shortUrl);
        when(urlRepository.findUrlByShortUrl(any())).thenReturn(Optional.of(url));
        Assertions.assertThat(urlService.decodeShortUrl(shortUrl)).isEqualTo(originalUrl);
    }

    @Test
    @DisplayName("단축 url 생성 및 저장하는 테스트")
    void 단축_url_생성_테스트() {
        UrlCreateRequest request = new UrlCreateRequest(originalUrl);
        Url url = new Url(originalUrl, shortUrl);
        ReflectionTestUtils.setField(url, "id", 1253L);

        when(urlRepository.findUrlByOriginalUrl(any())).thenReturn(Optional.empty());
        when(urlRepository.save(any())).thenReturn(url);

        Assertions.assertThat(urlService.findOrCreateShortUrl(request)).isEqualTo(url);
    }

    @Test
    @DisplayName("이미 저장된 단축 url이 있을 경우 예외 발생")
    void 저장된_단축_url_반환() {
        UrlCreateRequest request = new UrlCreateRequest(originalUrl);
        when(urlRepository.save(any())).thenThrow(new DataIntegrityViolationException("중복 에러"));

        Url existingUrl = new Url(originalUrl, shortUrl);

        when(urlRepository.findUrlByOriginalUrl(any())).thenReturn(Optional.of(existingUrl));

        Assertions.assertThat(urlService.findOrCreateShortUrl(request)).isEqualTo(existingUrl);
    }

    /// //에러 테스트
    @Test
    @DisplayName("단축URL 디코딩 테스트 : 조회되는 Url이 없으면 예외")
    void 단축_url_디코딩_예외() {
        when(urlRepository.findUrlByShortUrl(any())).thenReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            urlService.decodeShortUrl(shortUrl);
        });
    }
}
