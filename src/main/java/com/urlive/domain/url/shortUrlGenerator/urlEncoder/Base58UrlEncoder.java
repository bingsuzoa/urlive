package com.urlive.domain.url.shortUrlGenerator.urlEncoder;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Primary
@Component
public class Base58UrlEncoder implements UrlEncoder {
    private static final String BASE58 = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final int BASE = 58;
    private static final Map<Character, Integer> charToIndexMap = new HashMap<>();

    public Base58UrlEncoder() {
        init();
    }

    @Override
    public String encode(long id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            int remainder = (int) (id % BASE);
            sb.append(BASE58.charAt(remainder));
            id /= BASE;
        }
        return sb.reverse().toString();
    }

    @Override
    public long decode(String shortUrl) {
        long id = 0;
        for (int i = 0; i < shortUrl.length(); i++) {
            id = id * BASE + charToIndexMap.get(shortUrl.charAt(i));
        }
        return id;
    }


    private void init() {
        for (int i = 0; i < BASE58.length(); i++) {
            charToIndexMap.put(BASE58.charAt(i), i);
        }
    }

}
