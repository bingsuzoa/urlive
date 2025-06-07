package com.urlive.service.integrationTest;

import com.urlive.domain.url.Url;
import com.urlive.domain.url.UrlRepository;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.domain.userUrl.UserUrlRepository;
import com.urlive.service.PasswordService;
import com.urlive.service.UserService;
import com.urlive.web.dto.user.PasswordChangeRequest;
import com.urlive.web.dto.user.UserCreateRequest;
import com.urlive.web.dto.user.UserResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUrlRepository userUrlRepository;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("User객체 저장 테스트")
    void 객체_저장() {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("test", "01012345678", "test123", 20250604, 1, "KR");

        UserResponse response = userService.saveUser(userCreateRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.country().getName()).isEqualTo("South Korea");
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void 비밀번호_변경() {
        String existingPassword = "test123";
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("test", "01012345678", existingPassword, 20250604, 1, "KR");
        UserResponse response = userService.saveUser(userCreateRequest);
        Long userId = response.id();

        Assertions.assertThat(userService.changePassword(userId, new PasswordChangeRequest("test124"))).isNotNull();
    }

    @Test
    @DisplayName("사용자가 가지고 있는 URL 목록 가져오는 테스트")
    void url_목록_가져오기() {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("test", "01012345678", "test123", 20250604, 1, "KR");
        UserResponse response = userService.saveUser(userCreateRequest);
        User user = userRepository.findUserWithUrlsById(response.id()).get();

        Url url1 = new Url("http://test1.com", "test1ShortUrl");
        urlRepository.save(url1);
        Url url1WithUsers = urlRepository.findUrlWithUsersByOriginalUrl(url1.getOriginalUrl()).get();
        UserUrl userUrl1 = new UserUrl(user, url1WithUsers);

        Url url2 = new Url("http://test2.com", "test2ShortUrl");
        urlRepository.save(url2);
        Url url2WithUsers = urlRepository.findUrlWithUsersByOriginalUrl(url2.getOriginalUrl()).get();
        UserUrl userUrl2 = new UserUrl(user, url2WithUsers);

        Url url3 = new Url("http://test3.com", "test3ShortUrl");
        urlRepository.save(url3);
        Url url3WithUsers = urlRepository.findUrlWithUsersByOriginalUrl(url3.getOriginalUrl()).get();
        UserUrl userUrl3 = new UserUrl(user, url3WithUsers);

        List<UserUrl> userUrls = List.of(userUrl1, userUrl2, userUrl3);
        userUrlRepository.saveAll(userUrls);

        Assertions.assertThat(userService.getUserUrlResponses(user.getId()).size()).isEqualTo(3);
    }
}
