package com.urlive.domain.user;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryRepository;
import com.urlive.domain.user.passwordHistory.PasswordHistoryRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    private UrlRepository urlRepository;

    @AfterEach
    void deleteAll() {
        urlRepository.deleteAll();
        userRepository.deleteAll();
        passwordHistoryRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    @DisplayName("User 객체 저장 확인")
    void user_객체_저장() {
        Country country = countryRepository.findByIsoCode("KR").get();
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, country));
        Assertions.assertThat(userRepository.findById(user.getId()).get()).isEqualTo(user);
    }

    @Test
    @Transactional
    @DisplayName("User조회 시 Urls fetch join 확인")
    void user_urls_fetch_join_확인() {
        Country country = countryRepository.findByIsoCode("KR").get();
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, country));
        urlRepository.save(new Url(user, "originalUrl", "shortUrl"));

        User updatedUser = userRepository.findUserWithUrlsById(user.getId()).get();
        Assertions.assertThat(updatedUser.getUrls()).isNotNull();
        Assertions.assertThat(updatedUser.getUrls().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("User조회 시 Countries fetch join으로 가져오는지 확인")
    void user_조회시_countries_확인() {
        Country country = countryRepository.findByIsoCode("KR").get();
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, country));
        urlRepository.save(new Url(user, "originalUrl", "shortUrl"));

        User userWithCountry = userRepository.findUserWithCountry(user.getId()).get();
        Assertions.assertThat(userWithCountry.getCountry()).isNotNull();
    }

    @Test
    @DisplayName("phoneNumber로 Countries fetch join으로 가져오는지 확인")
    void phoneNumber로_User조회시_countries_확인() {
        Country country = countryRepository.findByIsoCode("KR").get();
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, country));
        urlRepository.save(new Url(user, "originalUrl", "shortUrl"));

        User userWithCountry = userRepository.findUserByPhoneNumber("01012345678").get();
        Assertions.assertThat(userWithCountry.getCountry()).isNotNull();
    }
}
