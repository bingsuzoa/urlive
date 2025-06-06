package com.urlive.domain.url;

import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.domain.userUrl.UserUrlRepository;
import com.urlive.domain.view.View;
import com.urlive.domain.view.ViewRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
    private EntityManager em;

    private static final String originalUrl = "http://test.com";
    private static final String shortUrl = "testShortUrl";

    Url setUp() {
        return urlRepository.save(new Url(originalUrl, shortUrl));
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
    @DisplayName("연관관계 확인 Url - User")
    void 연관관계_Url_User() {
        User user1 = new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국"));
        userRepository.save(user1);
        User user2 = new User("test1", "01012345679", "5678", 2025, Gender.MEN, new Country("KR", "대한민국"));
        userRepository.save(user2);

        Url url1 = new Url("http://test1.com", "testShort1");
        Url testUrl = urlRepository.save(url1);

        UserUrl user1Url1 = new UserUrl(user1, url1);
        userUrlRepository.save(user1Url1);
        UserUrl user2Url1 = new UserUrl(user2, url1);
        userUrlRepository.save(user2Url1);

        Assertions.assertThat(testUrl.getUsers().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("연관관계 확인 Url - View")
    void 연관관계_Url_view() {
        Url url = setUp();

        View view1 = new View(url);
        viewRepository.save(view1);

        View view2 = new View(url);
        viewRepository.save(view2);

        Assertions.assertThat(url.getViews().size()).isEqualTo(2);
    }

    /// ////예외 테스트
    @Test
    @DisplayName("이미 저장된 originalUrl 저장 시도 시 예외 발생")
    void 중복_URL_저장시_예외() {
        setUp();
        urlRepository.flush();

        org.junit.jupiter.api.Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            urlRepository.saveAndFlush(new Url(originalUrl, "shortUrl"));
        });
        em.clear();
    }
}
