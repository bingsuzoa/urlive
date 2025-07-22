package com.urlive.domain.url;

import com.urlive.config.AsyncSyncTestConfig;
import com.urlive.config.TestRedisConfig;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;


    private static final String originalUrl = "http://test.com";
    private static final String shortUrl = "testShortUrl";

    Url setUp() {
        Country country = countryRepository.save(new Country("KOREA", "korea"));
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, country));
        return urlRepository.save(new Url(user, originalUrl, shortUrl));
    }

    @AfterEach
    void delete() {
        urlRepository.deleteAll();
        userRepository.deleteAll();
        countryRepository.deleteAll();
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
