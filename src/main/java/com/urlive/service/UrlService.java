package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.url.shortUrlGenerator.ShortUrlGenerator;
import com.urlive.domain.view.View;
import com.urlive.domain.view.ViewRepository;
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
            ViewRepository viewRepository,
            ShortUrlGenerator shortUrlGenerator
    ) {
        this.urlRepository = urlRepository;
        this.viewRepository = viewRepository;
        this.shortUrlGenerator = shortUrlGenerator;
    }

    private static final String INVALID_ORIGINAL_URL = "유효하지 않은 URL 입니다.";

    private final UrlRepository urlRepository;
    private final ViewRepository viewRepository;
    private final ShortUrlGenerator shortUrlGenerator;


    @Transactional
    public String decodeShortUrl(String shortUrl) {
        Optional<Url> optionalUrl = urlRepository.findUrlByShortUrl(shortUrl);
        if (optionalUrl.isEmpty()) {
            throw new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL);
        }
        return recordView(optionalUrl.get()).getOriginalUrl();
    }

    @Transactional
    private Url recordView(Url url) {
        if (urlRepository.increaseViewCount(url.getId()) == 0) {
            throw new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL);
        }
        Url urlWithoutViews = urlRepository.findById(url.getId()).get();
        Url urlWithViews = urlRepository.findUrlWithViews(url.getId()).get();
        urlWithViews.addView(viewRepository.save(new View(urlWithoutViews)));
        return urlWithoutViews;
    }

    @Transactional
    public Url findOrCreateShortUrl(UrlCreateRequest urlCreateRequest) {
        String originalUrl = normalize(urlCreateRequest.originalUrl());
        try {
            String shortUrl = shortUrlGenerator.generateShortUrl();
            return urlRepository.save(new Url(originalUrl, shortUrl));
        } catch (DataIntegrityViolationException e) {
            return urlRepository.findUrlByOriginalUrl(originalUrl).get();
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
