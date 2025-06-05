package com.urlive.domain.url.shortUrlGenerator.urlEncoder;


import com.urlive.domain.url.Url;
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
            int remainder = (int) (id % BASE);
            sb.append(BASE62.charAt(remainder));
            id /= BASE;
        }
        return sb.reverse().toString();
    }

    @Override
    public long decode(String shortUrl) {
        long id = 0;
        for (int i = 0; i < shortUrl.length(); i++) {
            char c = shortUrl.charAt(i);
            int value;
            if ('0' <= c && c <= '9') {
                value = c - '0';
            } else if ('A' <= c && c <= 'Z') {
                value = c - 'A' + 10;
            } else if ('a' <= c && c <= 'z') {
                value = c - 'a' + 36;
            } else {
                throw new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL);
            }
            id = id * 62 + value;
        }
        return id;
    }
}
