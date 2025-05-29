package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.urlEncoder.Base62UrlEncoder;
import com.urlive.domain.urlEncoder.UrlEncoder;
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

    @Test
    @DisplayName("인코딩 확인하는 테스트")
    void 인코딩_확인() {
        String originalUrl = "http://urlive.com";
        UrlCreateRequest request = new UrlCreateRequest(originalUrl);
        Url url = new Url(originalUrl);
        ReflectionTestUtils.setField(url, "id", 1253L);

        when(urlRepository.findUrlByOriginalUrl(any())).thenReturn(Optional.empty());
        when(urlRepository.save(any())).thenReturn(url);

        Url result = urlService.getShortUrl(request);

        Assertions.assertThat(result.getShortUrl()).isEqualTo("KD");

    }
}
