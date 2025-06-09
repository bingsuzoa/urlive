package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ViewService {

    public ViewService(StringRedisTemplate redisTemplate,
                       UrlRepository urlRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.urlRepository = urlRepository;
    }

    public static final String VIEW_COUNT_PREFIX = "viewCount:";
    private final StringRedisTemplate redisTemplate;
    private final UrlRepository urlRepository;

    public void incrementViewCount(Long urlId) {
        String key = VIEW_COUNT_PREFIX + urlId;
        redisTemplate.opsForValue().increment(key);
    }

    public Long getViewCount(Long urlId) {
        String key = VIEW_COUNT_PREFIX + urlId;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.valueOf(value) : 0L;
    }

    @Transactional
    public void flushViewCountToDB() {
        Set<String> keys = redisTemplate.keys(VIEW_COUNT_PREFIX + "*");

        if(keys == null || keys.isEmpty()) {
            return;
        }

        for(String key : keys) {
            String urlIdStr = key.replace(VIEW_COUNT_PREFIX, "");
            Long urlID = Long.valueOf(urlIdStr);

            String value = redisTemplate.opsForValue().get(key);
            if(value == null) {
                continue;
            }
            Long viewCount = Long.valueOf(value);

            Url url = urlRepository.findById(urlID)
                    .orElseThrow(() -> new IllegalArgumentException(Url.NOT_EXIST_SHORT_URL));

            url.updateViewCount(viewCount);
        }
    }
}
