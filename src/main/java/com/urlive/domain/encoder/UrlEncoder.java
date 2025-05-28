package com.urlive.domain.encoder;

import org.springframework.stereotype.Component;

@Component
public interface UrlEncoder {
    String encode(long id);

    long decode(String shortUrl);

}
