package com.urlive.service.integrationTest;

import com.urlive.config.AsyncSyncTestConfig;
import com.urlive.config.TestRedisConfig;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.userUrl.UserUrlRepository;
import com.urlive.domain.user.passwordHistory.PasswordService;
import com.urlive.domain.url.UrlService;
import com.urlive.domain.user.UserService;
import com.urlive.web.dto.domain.user.PasswordChangeRequest;
import com.urlive.web.dto.domain.user.UserCreateRequest;
import com.urlive.web.dto.domain.user.UserResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUrlRepository userUrlRepository;

    @Autowired
    private UrlService urlService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserService userService;

    @AfterEach
    void deleteAll() {
        userUrlRepository.deleteAll();
        userRepository.deleteAll();
    }

    UserResponse getUser() {
        UserCreateRequest request = new UserCreateRequest("test", "01012345678", "user1111",
                20250312, 1, "KR");
        return userService.saveUser(request);
    }

    @Test
    @DisplayName("User객체 저장 테스트")
    void 객체_저장() {
        UserCreateRequest userCreateRequest = new UserCreateRequest("test", "01012345678", "user1111",
                20250312, 1, "KR");

        UserResponse response = userService.saveUser(userCreateRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.countryDto().name()).isEqualTo("South Korea");
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void 비밀번호_변경() {
        UserResponse user = getUser();
        Assertions.assertThat(userService.changePassword(user.id(), new PasswordChangeRequest("test124"))).isNotNull();
    }
}
