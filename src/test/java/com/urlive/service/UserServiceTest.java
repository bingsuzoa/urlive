package com.urlive.service;

import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.web.dto.user.PasswordChangeRequest;
import com.urlive.web.dto.user.UserCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UrlService urlService;

    @Mock
    private UserUrlService userUrlService;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("User객체 저장 테스트")
    void 객체_저장() {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("test", "01012345678", "test123", 20250604, 1, 1);
        String encodedPassword = "encodedPassword";
        when(passwordService.encode(any())).thenReturn(encodedPassword);
        User user = new User("test", "01012345678", encodedPassword, 20250604, Gender.WOMEN, Country.AMERICA);
        when(userRepository.save(any())).thenReturn(user);

        Assertions.assertThat(userService.saveUser(userCreateRequest)).isNotNull();
        Assertions.assertThat(userService.saveUser(userCreateRequest).country()).isEqualTo(Country.AMERICA);
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void 비밀번호_변경() {
        User user = new User("test", "01012345678", "test123", 20250604, Gender.WOMEN, Country.AMERICA);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        String encodedPassword = "encodedPassword";
        when(passwordService.changePassword(any(), any())).thenReturn(encodedPassword);

        PasswordChangeRequest request = new PasswordChangeRequest("test123");

        Assertions.assertThat(userService.changePassword(1L, request)).isNotNull();
        Assertions.assertThat(userService.changePassword(1L, request).country()).isEqualTo(Country.AMERICA);
    }

    @Test
    @DisplayName("사용자가 가지고 있는 URL 목록 가져오는 테스트")
    void url_목록_가져오기() {

    }
}
