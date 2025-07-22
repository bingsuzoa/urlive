package com.urlive.service;

import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.UserService;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryService;
import com.urlive.domain.user.passwordHistory.PasswordService;
import com.urlive.web.dto.domain.user.PasswordChangeRequest;
import com.urlive.web.dto.domain.user.UserCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordService passwordService;
    @Mock
    private CountryService countryService;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("User객체 저장 테스트")
    void 객체_저장() throws Exception {
        Country country = new Country("KR", "korea");
        ReflectionTestUtils.setField(country, "id", 1L);
        when(countryService.findByIsoCode(any())).thenReturn(country);

        String encodedPassword = "encodedPassword";
        when(passwordService.encode(any())).thenReturn(encodedPassword);

        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "test", "01012345678", "rawPassword", 20250604, 1, "KR");

        User user = new User("test", "01012345678", encodedPassword, 20250604, Gender.WOMEN, new Country("KR", "대한민국"));
        when(userRepository.save(any())).thenReturn(user);

        Assertions.assertThat(userService.saveUser(userCreateRequest).gender()).isEqualTo(Gender.WOMEN);
    }

    @Test
    @DisplayName("이미 가입된 휴대폰 번호 없으면 false 반환 확인")
    void 동일한_휴대폰번호_없으면_false_확인() {
        String phoneNumber = "01011112222";
        when(userRepository.findUserByPhoneNumber(any())).thenReturn(Optional.empty());
        Assertions.assertThat(userService.isDuplicatePhoneNumber(phoneNumber)).isFalse();
    }

    @Test
    @DisplayName("이미 가입된 휴대폰 번호가 있는지 확인")
    void 동일한_휴대폰번호_가입여부_확인() {
        String phoneNumber = "01011112222";
        User user = new User("test", "01011112222", "encodedPassword", 20250604, Gender.WOMEN, new Country("KR", "대한민국"));
        when(userRepository.findUserByPhoneNumber(any())).thenReturn(Optional.of(user));

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.isDuplicatePhoneNumber(phoneNumber);
        });
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void 비밀번호_변경() {
        Country country = new Country("KR", "korea");
        ReflectionTestUtils.setField(country, "id", 1L);

        User user = new User("test", "01012345678", "test123", 20250604, Gender.WOMEN, country);
        when(userRepository.findUserWithCountry(any())).thenReturn(Optional.of(user));

        String encodedPassword = "encodedPassword";
        when(passwordService.changePassword(any(), any())).thenReturn(encodedPassword);

        PasswordChangeRequest request = new PasswordChangeRequest("test123");

        Assertions.assertThat(userService.changePassword(1L, request)).isNotNull();
        Assertions.assertThat(userService.changePassword(1L, request).gender()).isEqualTo(Gender.WOMEN);
    }
}
