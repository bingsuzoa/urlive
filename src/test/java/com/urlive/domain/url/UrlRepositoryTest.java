package com.urlive.domain.url;

import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.option.country.CountryRepository;
import com.urlive.domain.userUrl.UserUrlRepository;
import com.urlive.domain.view.ViewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUrlRepository userUrlRepository;

    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private CountryRepository countryRepository;


    private static final String originalUrl = "http://test.com";
    private static final String shortUrl = "testShortUrl";


    Url setUp() {
        return urlRepository.save(new Url(originalUrl, shortUrl));
    }

    @AfterEach
    void delete() {
        userRepository.deleteAll();
        countryRepository.deleteAll();
        urlRepository.deleteAll();
        userUrlRepository.deleteAll();
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

}
