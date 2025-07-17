package com.urlive.domain.url;

import com.urlive.domain.url.shortUrlGenerator.ShortUrlGenerator;
import com.urlive.domain.view.ViewService;
import com.urlive.web.dto.domain.url.UrlCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class UrlService {

    @Autowired
    public UrlService(
            UrlRepository urlRepository,
            ViewService viewService,
            ShortUrlGenerator shortUrlGenerator
    ) {
        this.urlRepository = urlRepository;
        this.viewService = viewService;
        this.shortUrlGenerator = shortUrlGenerator;
    }

    private static final String INVALID_ORIGINAL_URL = "유효 하지 않은 URL 입니다.";

    private final UrlRepository urlRepository;
    private final ViewService viewService;
    private final ShortUrlGenerator shortUrlGenerator;


    @Transactional
    public String decodeShortUrl(String shortUrl) {
        Optional<Url> optionalUrl = urlRepository.findUrlByShortUrl(shortUrl);
        if (optionalUrl.isEmpty()) {
            throw new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL);
        }
        Url url = optionalUrl.get();
        viewService.incrementViewCount(url.getId());
        return url.getOriginalUrl();
    }


    @Transactional
    public Url findOrCreateShortUrl(UrlCreateRequest urlCreateRequest) {
        String originalUrl = normalize(urlCreateRequest.originalUrl());
        return urlRepository.findUrlWithUsersByOriginalUrl(originalUrl)
                .orElseGet(() -> {
                    String shortUrl = shortUrlGenerator.generateShortUrl();
                    Url url = urlRepository.save(new Url(originalUrl, shortUrl));
                    return url;
                });
    }

    private String normalize(String rawUrl) {
        if (rawUrl == null || rawUrl.trim().isEmpty()) {
            throw new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL);
        }

        String trimmedUrl = rawUrl.trim();

        if (trimmedUrl.startsWith("http://") || trimmedUrl.startsWith("https://")) {
            return trimmedUrl;
        }

        return "https://" + trimmedUrl;
    }
}
