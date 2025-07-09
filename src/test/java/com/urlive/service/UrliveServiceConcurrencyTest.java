package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.url.shortUrlGenerator.ShortUrlGenerator;
import com.urlive.web.dto.url.UrlCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class UrliveServiceConcurrencyTest {

    @Autowired
    private UrlService urlService;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ViewService viewService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    Url setUp() {
       return urlService.findOrCreateShortUrl(new UrlCreateRequest("http://test.com"));
    }

    @AfterEach
    void delete() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
        urlRepository.deleteAll();
    }

    @Test
    @DisplayName("1개의_스레드로_viewCount증가_확인")
    void 한_개의_스레드로_viewCount증가_확인() {
        Url url = setUp();
        Long urlId = url.getId();
        String shortUrl = url.getShortUrl();
        urlService.decodeShortUrl(shortUrl);
        String redisKey = "viewCount:" + url.getId();
        String viewCount = redisTemplate.opsForValue().get(redisKey);
        Assertions.assertThat(viewCount).isEqualTo("1");

    }

    @Test
    @DisplayName("여러 스레드가 동시에 decodeUrl()요청 시에도 viewCount가 정확히 카운트되는지 확인")
    void 카운트_증가_확인() throws InterruptedException {
        Url url = setUp();
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    urlService.decodeShortUrl(url.getShortUrl());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        String redisKey = "viewCount:" + url.getId();
        String viewCount = redisTemplate.opsForValue().get(redisKey);
        Assertions.assertThat(viewCount).isEqualTo("100");
    }

    @Test
    @DisplayName("Redis에 있는 데이터가 DB에 정확히 주입되는지 확인")
    void DB_FLUSH_확인() throws InterruptedException {
        Url url = setUp();
        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    urlService.decodeShortUrl(url.getShortUrl());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        String redisKey = "viewCount:" + url.getId();
        String viewCount = redisTemplate.opsForValue().get(redisKey);
        Assertions.assertThat(viewCount).isEqualTo("30");

        viewService.flushViewCountToDB();
        Url updatedUrl = urlRepository.findById(url.getId()).get();
        Assertions.assertThat(updatedUrl.getViewCount()).isEqualTo(30);
    }
}
