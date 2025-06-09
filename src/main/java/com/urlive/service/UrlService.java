package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.url.shortUrlGenerator.ShortUrlGenerator;
import com.urlive.web.dto.url.UrlCreateRequest;
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

    private static final String INVALID_ORIGINAL_URL = "유효하지 않은 URL 입니다.";

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
        try {
            String shortUrl = shortUrlGenerator.generateShortUrl();
            urlRepository.save(new Url(originalUrl, shortUrl));
            return urlRepository.findUrlWithUsersByOriginalUrl(originalUrl).get();
        } catch (DataIntegrityViolationException e) {
            return urlRepository.findUrlWithUsersByOriginalUrl(originalUrl).get();
        }
    }

    private String normalize(String rawUrl) {
        try {
            URI uri = new URI(rawUrl);

            String schema = uri.getScheme() != null ? uri.getScheme().toLowerCase() : "https";
            String host = uri.getHost() != null ? uri.getHost().toLowerCase() : "";
            int port = uri.getPort();
            String path = uri.getPath() != null && !uri.getPath().isEmpty() ? uri.getPath() : "/";
            String authority = (port == -1 || port == 80 || port == 443) ? host : host + ":" + port;
            return schema + "://" + authority + path;

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(INVALID_ORIGINAL_URL);
        }
    }
}
