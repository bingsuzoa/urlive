package com.urlive.domain.url.shortUrlGenerator;


import com.urlive.domain.url.shortUrlGenerator.urlEncoder.UrlEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShortUrlGenerator {

    private ShortUrlGenerator(UrlEncoder urlEncoder) {
        this.urlEncoder = urlEncoder;
    }

    private final UrlEncoder urlEncoder;

    public String generateShortUrl() {
        UUID uuid = UUID.randomUUID();
        long mostSigBits = Math.abs(uuid.getMostSignificantBits());
        return urlEncoder.encode(mostSigBits);
    }
}
