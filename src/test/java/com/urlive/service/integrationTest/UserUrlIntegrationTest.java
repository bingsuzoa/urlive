package com.urlive.service.integrationTest;

import com.urlive.config.AsyncSyncTestConfig;
import com.urlive.config.TestRedisConfig;
import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.url.shortUrlGenerator.ShortUrlGenerator;
import com.urlive.domain.url.shortUrlGenerator.urlEncoder.UrlEncoder;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryRepository;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.domain.userUrl.UserUrlRepository;
import com.urlive.service.CountryService;
import com.urlive.service.UserUrlService;
import com.urlive.web.dto.user.UserCreateRequest;
import com.urlive.web.dto.user.UserResponse;
import com.urlive.web.dto.userUrl.UpdateTitleRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
public class UserUrlIntegrationTest {

    @Autowired
    private UserUrlService userUrlService;

    @Autowired
    private UserUrlRepository userUrlRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ShortUrlGenerator shortUrlGenerator;

    @AfterEach
    void deleteAll() {
        userUrlRepository.deleteAll();
        userRepository.deleteAll();
        urlRepository.deleteAll();
    }

    Url getUrl() {
        String encodedUrl = shortUrlGenerator.generateShortUrl();
        return urlRepository.save(new Url("http://test.com", encodedUrl));
    }
    /// ////////해피 테스트
    @Test
    @DisplayName("userUrl 저장 테스트")
    void 저장() {
        Country country = countryService.findByIsoCode("KR");
        User user = userRepository.save(new User("test", "01012345678", "test123", 20250604, Gender.WOMEN, country));
        Url url = getUrl();
        Assertions.assertThat(userUrlService.saveUserUrl(user, url)).isNotNull();
    }

    @Test
    @DisplayName("userUrl 조회 테스트")
    void userUrl_조회() {
        Country country = countryService.findByIsoCode("KR");
        User user = userRepository.save(new User("test", "01012345678", "test123", 20250604, Gender.WOMEN, country));
        Url url = getUrl();
        UserUrl userUrl = userUrlService.saveUserUrl(user, url);
        Assertions.assertThat(userUrl.getTitle()).isEmpty();;
        Assertions.assertThat(userUrl.getOriginalUrl()).isEqualTo("http://test.com");
    }

    @Test
    @DisplayName("title 변경 테스트")
    void 타이틀_변경() {
        Country country = countryService.findByIsoCode("KR");
        User user = userRepository.save(new User("test", "01012345678", "test123", 20250604, Gender.WOMEN, country));
        Url url = getUrl();
        UserUrl userUrl = userUrlService.saveUserUrl(user, url);
        Assertions.assertThat(userUrlService.updateTitle(userUrl.getId(), new UpdateTitleRequest("newTitle")).title())
                .isEqualTo("newTitle");
    }

    @Test
    @DisplayName("user의 단축Url 삭제 테스트")
    void 단축Url_삭제() {
        Country country = countryService.findByIsoCode("KR");
        User user = userRepository.save(new User("test", "01012345678", "test123", 20250604, Gender.WOMEN, country));
        Url url = getUrl();
        UserUrl userUrl = userUrlService.saveUserUrl(user, url);
        Assertions.assertThat(userUrlService.deleteUserUrl(userUrl.getId())).isNotNull();
    }

    /// ////////예외 테스트
    @Test
    @DisplayName("userUrl 조회 실패 테스트")
    void userUrl_조회_실패() {
        Country country = countryService.findByIsoCode("KR");
        User user = userRepository.save(new User("test123", "01012345678", "test123", 20250604, Gender.WOMEN, country));
        Url url = getUrl();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userUrlService.getUserUrl(user, url);
        });
    }

    @Test
    @DisplayName("user의 단축Url 삭제 실패 테스트")
    void 단축Url_삭제_실패() {
        Country country = countryService.findByIsoCode("KR");
        User user = userRepository.save(new User("test", "01012345678", "test123", 20250604, Gender.WOMEN, country));
        Url url = getUrl();
        UserUrl userUrl = userUrlService.saveUserUrl(user, url);
        userUrlService.deleteUserUrl(userUrl.getId());

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userUrlService.deleteUserUrl(userUrl.getId());
        });
    }
}
