package com.urlive.domain.url;

import com.urlive.domain.url.shortUrlGenerator.ShortUrlGenerator;
import com.urlive.domain.user.User;
import com.urlive.service.SseService;
import com.urlive.web.dto.domain.common.DtoFactory;
import com.urlive.web.dto.domain.url.UrlCreateRequest;
import com.urlive.web.dto.domain.userUrl.UpdateTitleRequest;
import com.urlive.web.dto.domain.userUrl.UserUrlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UrlService {

    @Autowired
    public UrlService(
            UrlRepository urlRepository,
            ShortUrlGenerator shortUrlGenerator,
            SseService sseService
    ) {
        this.urlRepository = urlRepository;
        this.shortUrlGenerator = shortUrlGenerator;
        this.sseService = sseService;
    }

    private static final String INVALID_ORIGINAL_URL = "유효 하지 않은 URL 입니다.";

    private final UrlRepository urlRepository;
    private final ShortUrlGenerator shortUrlGenerator;
    private final SseService sseService;


    @Transactional
    public String decodeShortUrl(String shortUrl) {
        Optional<Url> optionalUrl = urlRepository.findUrlByShortUrl(shortUrl);
        if (optionalUrl.isEmpty()) {
            throw new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL);
        }
        Url url = optionalUrl.get();
        Long updatedViewCount = incrementViewCount(url);
        sseService.sendViewUpdate(url.getId(), updatedViewCount);
        System.out.println("📕SSE sendViewUpdate called for urlId: " + url.getId() + ", viewCount: " + updatedViewCount);
        return url.getOriginalUrl();
    }

    @Transactional
    private Long incrementViewCount(Url url) {
        url.increaseViewCount();
        return url.getViewCount();
    }

    @Transactional
    public Url CreateShortUrl(User user, UrlCreateRequest urlCreateRequest) {
        String originalUrl = normalize(urlCreateRequest.originalUrl());
        String shortUrl = shortUrlGenerator.generateShortUrl();
        return urlRepository.save(new Url(user, originalUrl, shortUrl));
    }

    @Transactional
    public UserUrlResponse updateTitle(Long id, UpdateTitleRequest updateTitleRequest) {
        Optional<Url> optionalUrl = urlRepository.findById(id);
        if (optionalUrl.isEmpty()) {
            throw new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL);
        }
        Url url = optionalUrl.get();
        return DtoFactory.getUserUrlDto(url.updateTitle(updateTitleRequest.newTitle()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Long getCurrentViewCount(Long userUrlId) {
        return urlRepository.findViewCountById(userUrlId)
                .orElseThrow(() -> new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL));
    }

    @Transactional
    public UserUrlResponse deleteUrl(Long id) {
        Optional<Url> optionalUrl = urlRepository.findById(id);
        if (optionalUrl.isEmpty()) {
            throw new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL);
        }
        Url url = optionalUrl.get();
        urlRepository.delete(url);
        return DtoFactory.getUserUrlDto(url);
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
