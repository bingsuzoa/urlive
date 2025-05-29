package com.urlive.domain.url;

import com.urlive.controller.dto.url.UrlCreateRequest;
import com.urlive.domain.encoder.UrlEncoder;
import org.springframework.beans.factory.annotation.Autowired;
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
            UrlEncoder urlEncoder
    ) {
        this.urlRepository = urlRepository;
        this.urlEncoder = urlEncoder;
    }

    private static final String INVALID_ORIGINAL_URL = "유효하지 않은 URL 입니다.";

    private final UrlRepository urlRepository;
    private final UrlEncoder urlEncoder;

    @Transactional
    public Url getShortUrl(UrlCreateRequest urlCreateRequest) {
        String originalUrl = normalize(urlCreateRequest.rawUrl());
        Optional<Url> optionalUrl = urlRepository.findUrlByOriginalUrl(originalUrl);
        if(optionalUrl.isEmpty()) {
            Url url = urlRepository.save(new Url(originalUrl));
            String shortUrl = urlEncoder.encode(url.getId());
            url.createShortKey(shortUrl);
            return url;
        }
        return optionalUrl.get();
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
