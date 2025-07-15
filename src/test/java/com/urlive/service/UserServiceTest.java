package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.user.UserService;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryService;
import com.urlive.domain.user.passwordHistory.PasswordService;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.domain.userUrl.UserUrlRepository;
import com.urlive.web.dto.domain.user.PasswordChangeRequest;
import com.urlive.web.dto.domain.user.UserCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserUrlRepository userUrlRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("User객체 저장 테스트")
    void 객체_저장() {
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("test", "01012345678", "test123", 20250604, 1, "KR");
        String encodedPassword = "encodedPassword";
        User user = new User("test", "01012345678", encodedPassword, 20250604, Gender.WOMEN, new Country("KR", "대한민국"));
        Country country = new Country("KR", "korea");
        when(passwordService.encode(any())).thenReturn(encodedPassword);
        when(userRepository.save(any())).thenReturn(user);
        when(countryService.findByIsoCode(any())).thenReturn(country);

        Assertions.assertThat(userService.saveUser(userCreateRequest)).isNotNull();
        Assertions.assertThat(userService.saveUser(userCreateRequest).gender()).isEqualTo(Gender.WOMEN);
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void 비밀번호_변경() {
        User user = new User("test", "01012345678", "test123", 20250604, Gender.WOMEN, new Country("KR", "대한민국"));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        String encodedPassword = "encodedPassword";
        when(passwordService.changePassword(any(), any())).thenReturn(encodedPassword);

        PasswordChangeRequest request = new PasswordChangeRequest("test123");

        Assertions.assertThat(userService.changePassword(1L, request)).isNotNull();
        Assertions.assertThat(userService.changePassword(1L, request).gender()).isEqualTo(Gender.WOMEN);
    }

    @Test
    @DisplayName("사용자가 가지고 있는 URL 목록 가져오는 테스트")
    void url_목록_가져오기() {
        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국"));
        Url url1 = new Url("http://test1.com", "test1ShortUrl");
        UserUrl userUrl1 = new UserUrl(user, url1);
        Url url2 = new Url("http://test2.com", "test2ShortUrl");
        UserUrl userUrl2 = new UserUrl(user, url1);
        Url url3 = new Url("http://test3.com", "test3ShortUrl");
        UserUrl userUrl3 = new UserUrl(user, url1);

        List<UserUrl> userUrls = List.of(userUrl1, userUrl2, userUrl3);
        when(userUrlRepository.findUserUrls(anyLong())).thenReturn(userUrls);
        Assertions.assertThat(userService.getUserUrlResponses(1L).size()).isEqualTo(3);
    }
}
