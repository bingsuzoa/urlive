package com.urlive.service;

import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.userUrl.UserUrl;
import com.urlive.domain.userUrl.UserUrlRepository;
import com.urlive.web.dto.userUrl.UpdateTitleRequest;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUrlServiceTest {

    @Mock
    private UserUrlRepository userUrlRepository;

    @InjectMocks
    private UserUrlService userUrlService;

    User getUser() {
        Country country = new Country("KR", "South Korea");
        User user = new User("test", "01012345678", "test123", 20250604, Gender.WOMEN, country);
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    Url getUrl() {
        Url url = new Url("http://test.com", "shortUrl");
        ReflectionTestUtils.setField(url, "id", 1L);
        return url;
    }

    @Test
    @DisplayName("userUrl 객체 저장 테스트")
    void 저장() {
        User user = getUser();
        Url url = getUrl();
        UserUrl userUrl = new UserUrl(getUser(), getUrl());
        when(userUrlRepository.save(any())).thenReturn(userUrl);

        Assertions.assertThat(userUrlService.saveUserUrl(user, url).getOriginalUrl()).isEqualTo(userUrl.getOriginalUrl());
    }

    @Test
    @DisplayName("userUrl 객체 조회 테스트")
    void 조회() {
        User user = getUser();
        Url url = getUrl();
        UserUrl userUrl = new UserUrl(getUser(), getUrl());
        when(userUrlRepository.findUserUrlByUserAndUrl(any(), any())).thenReturn(Optional.of(userUrl));

        Assertions.assertThat(userUrlService.getUserUrl(user, url)).isNotNull();
    }

    @Test
    @DisplayName("userUrl title 변경")
    void title_변경() {
        User user = getUser();
        Url url = getUrl();
        UserUrl userUrl = new UserUrl(getUser(), getUrl());
        when(userUrlRepository.findById(any())).thenReturn(Optional.of(userUrl));

        Assertions.assertThat(userUrlService.updateTitle(1L, new UpdateTitleRequest("newTitle"))).isNotNull();
    }

    @Test
    @DisplayName("user의 url 삭제 테스트")
    void url_삭제_테스트() {
        User user = getUser();
        Url url = getUrl();
        UserUrl userUrl = new UserUrl(getUser(), getUrl());
        when(userUrlRepository.findById(any())).thenReturn(Optional.of(userUrl));
        doNothing().when(userUrlRepository).delete(any());

        Assertions.assertThat(userUrlService.deleteUserUrl(1L)).isNotNull();
    }
}
