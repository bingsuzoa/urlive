package com.urlive.domain.url;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    private static final String originalUrl = "http://test.com";
    private static final String shortUrl = "testShortUrl";

    void setUp() {
        urlRepository.save(new Url(originalUrl, shortUrl));
    }

    @AfterEach
    void delete() {
        urlRepository.deleteAll();
    }

    @Test
    @DisplayName("originalUrl로 Url 가져오기")
    void originalUrl로_Url_가져오기() {
        setUp();
        Url url = urlRepository.findUrlByOriginalUrl(originalUrl).get();
        Assertions.assertThat(url.getShortUrl()).isEqualTo(shortUrl);
    }

    @Test
    @DisplayName("shortUrl로 Url 가져오기")
    void shortUrl로_Url_가져오기() {
        setUp();
        Url url = urlRepository.findUrlByShortUrl(shortUrl).get();
        Assertions.assertThat(url.getOriginalUrl()).isEqualTo(originalUrl);
    }

    @Test
    @DisplayName("단축 URL 디코딩 시도 시 viewCount + 1 증가하기")
    void viewCount_증가() {
        setUp();
        Url url = urlRepository.findUrlByShortUrl(shortUrl).get();
        long id = url.getId();
        long existingViewCount = url.getViewCount();

        urlRepository.increaseViewCount(id);
        long updatedViewCount = urlRepository.findById(id).get().getViewCount();
        Assertions.assertThat(existingViewCount + 1).isEqualTo(updatedViewCount);
    }
}
