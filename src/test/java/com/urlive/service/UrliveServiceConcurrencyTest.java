package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.url.shortUrlGenerator.ShortUrlGenerator;
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
@Rollback(value = false)
public class UrliveServiceConcurrencyTest {

    @Autowired
    private UrlService urlService;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ShortUrlGenerator shortUrlGenerator;

    @Autowired
    private ViewService viewService;

    @Container
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void setRedisContainer(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void cleanRedis() {
        // viewCount:* 로 시작하는 모든 키 삭제
        Set<String> keys = redisTemplate.keys("viewCount:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    Url setUp() {
        return urlRepository.save(new Url("http://test.com", "shortUrl"));
    }

    @AfterEach
    void delete() {
        urlRepository.deleteAll();
    }

    @Test
    @DisplayName("1개의_스레드로_viewCount증가_확인")
    void 한_개의_스레드로_viewCount증가_확인() {
        Url url = setUp();
        Long urlId = url.getId();
        urlService.decodeShortUrl("shortUrl");
        Assertions.assertThat(viewService.getViewCount(urlId)).isEqualTo(1);

    }

    @Test
    @DisplayName("여러 스레드가 동시에 decodeUrl()요청 시에도 viewCount가 정확히 카운트되는지 확인")
    void 카운트_증가_확인() throws InterruptedException {
        setUp();
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    urlService.decodeShortUrl("shortUrl");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Url url = urlRepository.findUrlByShortUrl("shortUrl").get();
        Assertions.assertThat(viewService.getViewCount(url.getId())).isEqualTo(threadCount);
    }
}
