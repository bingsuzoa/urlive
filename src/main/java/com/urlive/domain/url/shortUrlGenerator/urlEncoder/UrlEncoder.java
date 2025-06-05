package com.urlive.domain.url.shortUrlGenerator.urlEncoder;

import org.springframework.stereotype.Component;

@Component
public interface UrlEncoder {
    String encode(long id);

    long decode(String shortUrl);

}
