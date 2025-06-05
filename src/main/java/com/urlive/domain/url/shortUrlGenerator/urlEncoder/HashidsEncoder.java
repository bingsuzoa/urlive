package com.urlive.domain.url.shortUrlGenerator.urlEncoder;


import com.urlive.domain.url.Url;
import org.hashids.Hashids;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//@Primary
@Component
public class HashidsEncoder implements UrlEncoder {

    public HashidsEncoder() {
        this.hashids = new Hashids("this is my salt", 8);
    }

    private final Hashids hashids;

    @Override
    public String encode(long id) {
        return hashids.encode(id);
    }

    @Override
    public long decode(String shortUrl) {
        long[] decoded = hashids.decode(shortUrl);
        if (decoded.length == 0) {
            throw new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL);
        }
        return decoded[0];
    }

}
