package com.urlive.service.integrationTest;


import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.service.ViewService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
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
public class ViewServiceIntegrationTest {

    @Autowired
    private ViewService viewService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UrlRepository urlRepository;

    @Container
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void setRedisContainer(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @BeforeEach
    void cleanRedis() {
        Set<String> keys = redisTemplate.keys(ViewService.VIEW_COUNT_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @AfterEach
    void delete() {
        urlRepository.deleteAll();
    }

    Url getUrl() {
        return urlRepository.save(new Url("http://test.com", "shortUrl"));
    }

    @Test
    @DisplayName("여러 스레드가 동시 요청 시에도 viewCount 제대로 카운트 되는지 확인")
    void viewCount_확인() throws InterruptedException {
        Url url = getUrl();
        Long urlId = url.getId();

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    viewService.incrementViewCount(urlId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Url updatedUrl = urlRepository.findById(urlId).get();
        Assertions.assertThat(viewService.getViewCount(updatedUrl.getId())).isEqualTo(100);
    }

    @Test
    @DisplayName("메모리에 저장된 viewCount db저장 확인 테스트")
    void viewCount_db_저장() throws InterruptedException {
        Url url = getUrl();
        Long urlId = url.getId();

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    viewService.incrementViewCount(urlId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        viewService.flushViewCountToDB();
        Url updatedUrl = urlRepository.findById(urlId).get();
        Assertions.assertThat(updatedUrl.getViewCount()).isEqualTo(100);
    }
}
