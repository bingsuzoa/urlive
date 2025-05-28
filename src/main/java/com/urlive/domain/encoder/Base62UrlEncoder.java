package com.urlive.domain.encoder;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class Base62UrlEncoder implements UrlEncoder {
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = 62;

    @Override
    public String encode(long id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            int remainder = (int)(id % BASE);
            sb.append(BASE62.charAt(remainder));
            id /= BASE;
        }
        return sb.reverse().toString();
    }

    @Override
    public long decode(String shortUrl) {
        long id = 0;
        for (int i = 0; i < shortUrl.length(); i++) {
            id = id * BASE + BASE62.indexOf(shortUrl.charAt(i));
        }
        return id;
    }
}
