package com.urlive.service;

import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.web.dto.user.UserCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
