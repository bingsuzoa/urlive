package com.urlive.domain.userUrl;


import com.urlive.config.AsyncSyncTestConfig;
import com.urlive.config.TestRedisConfig;
import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
@ActiveProfiles("test")
public class UserUrlRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UserUrlRepository userUrlRepository;

    @Autowired
    private CountryRepository countryRepository;

    @AfterEach
    void deleteAll() {
        userUrlRepository.deleteAll();
        userRepository.deleteAll();
        urlRepository.deleteAll();
    }

    User getUser() {
        countryRepository.save(new Country("KR", "대한민국"));
        Country country = countryRepository.findByIsoCode("KR").get();
        return userRepository.save(new User("test", "01012345678", "user1111",
                20250312, Gender.WOMEN, country));
    }

    @Test
    @DisplayName("userId로 userUrl 목록 등록하기")
    void userUrl_등록하기() {
        User user = getUser();
        Url url = urlRepository.save(new Url("http://original.com", "shortUrl"));
        Assertions.assertThat(userUrlRepository.save(new UserUrl(user, url))).isNotNull();
    }

    @Test
    @DisplayName("user의 url목록 랜더링 위해 fetch join으로 가져옴 : 초기화 true확인")
    void fetch_join_확인() {
        User user = getUser();
        Url url1 = urlRepository.save(new Url("http://original.com", "shortUrl"));
        Url url2 = urlRepository.save(new Url("http://test2.com", "shortUrl2"));
        userUrlRepository.save(new UserUrl(user, url1));
        userUrlRepository.save(new UserUrl(user, url2));

        List<UserUrl> userUrls = userUrlRepository.findUserUrls(user.getId());
        Object notProxy = userUrls.getFirst().getUser();
        Assertions.assertThat(Hibernate.isInitialized(notProxy)).isTrue();
    }
}
