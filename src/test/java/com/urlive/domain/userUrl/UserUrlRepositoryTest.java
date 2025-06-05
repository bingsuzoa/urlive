package com.urlive.domain.userUrl;


import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserUrlRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UserUrlRepository userUrlRepository;

    @Autowired
    private EntityManager em;

    Long setUp() {
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, Country.CHINA));

        Url url1 = new Url("http://test1.com", "test1ShortUrl");
        urlRepository.save(url1);
        userUrlRepository.save(new UserUrl(user, url1));
        Url url2 = new Url("http://test2.com", "test2ShortUrl");
        urlRepository.save(url2);
        userUrlRepository.save(new UserUrl(user, url2));
        Url url3 = new Url("http://test3.com", "test3ShortUrl");
        urlRepository.save(url3);
        userUrlRepository.save(new UserUrl(user, url3));
        return user.getId();
    }

    @Test
    @DisplayName("userId로 userUrl 목록 가져오기")
    void userUrl조회() {
        Long userId = setUp();
        List<UserUrl> userUrls = userUrlRepository.findUserUrls(userId);

        Assertions.assertThat(userUrls.size()).isEqualTo(3);
        Assertions.assertThat(userUrls.getFirst().getOriginalUrl()).isNotNull();
    }

    @Test
    @DisplayName("userId로 userUrl에서 USER 초기화 안한 상태 확인하기")
    void user_지연로딩_확인() {
        Long userId = setUp();
        em.flush();
        em.clear();

        List<UserUrl> userUrls = userUrlRepository.findUserUrls(userId);

        Object userProxy = userUrls.getFirst().getUser();
        Assertions.assertThat(Hibernate.isInitialized(userProxy)).isFalse();
    }
}
