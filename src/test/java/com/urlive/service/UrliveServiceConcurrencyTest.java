//package com.urlive.service;
//
//import com.urlive.domain.url.Url;
//import com.urlive.domain.url.UrlRepository;
//import com.urlive.domain.url.UrlService;
//import com.urlive.domain.user.User;
//import com.urlive.domain.user.option.Gender;
//import com.urlive.domain.user.option.country.Country;
//import com.urlive.domain.user.option.country.CountryRepository;
//import com.urlive.web.dto.domain.url.UrlCreateRequest;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.test.context.ActiveProfiles;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@ActiveProfiles("test")
//@SpringBootTest
//@Testcontainers
//public class UrliveServiceConcurrencyTest {
//
//    @Autowired
//    private UrlService urlService;
//
//    @Autowired
//    private UrlRepository urlRepository;
//
//    @Autowired
//    private CountryRepository countryRepository;
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//
//    Url setUp() {
//        Country country = countryRepository.findByIsoCode("KR").get();
//        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, country);
//        return urlService.CreateShortUrl(user, new UrlCreateRequest("http://test.com"));
//    }
//
//    @AfterEach
//    void delete() {
//        redisTemplate.getConnectionFactory().getConnection().flushDb();
//        urlRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("1개의_스레드로_viewCount증가_확인")
//    void 한_개의_스레드로_viewCount증가_확인() {
//        Url url = setUp();
//        Long urlId = url.getId();
//        String shortUrl = url.getShortUrl();
//        urlService.decodeShortUrl(shortUrl);
//        String redisKey = "viewCount:" + url.getId();
//        String viewCount = redisTemplate.opsForValue().get(redisKey);
//        Assertions.assertThat(viewCount).isEqualTo("1");
//
//    }
//
//    @Test
//    @DisplayName("여러 스레드가 동시에 decodeUrl()요청 시에도 viewCount가 정확히 카운트되는지 확인")
//    void 카운트_증가_확인() throws InterruptedException {
//        Url url = setUp();
//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        for (int i = 0; i < threadCount; i++) {
//            executorService.submit(() -> {
//                try {
//                    urlService.decodeShortUrl(url.getShortUrl());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//        String redisKey = "viewCount:" + url.getId();
//        String viewCount = redisTemplate.opsForValue().get(redisKey);
//        Assertions.assertThat(viewCount).isEqualTo("100");
//    }
//}
