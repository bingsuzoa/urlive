package com.urlive.web.dto.common;

import com.urlive.domain.url.Url;
import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.userUrl.UserUrl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DtoFactoryTest {

    @Test
    @DisplayName("user객체로부터 userResponse 생성")
    void user_로부터_userResponse() {
        User user = new User("test", "01012345678", "password1111", 20250605, Gender.MEN, Country.CHINA);

        Assertions.assertThat(DtoFactory.createUserResponseDto(user)).isNotEqualTo(user);
        Assertions.assertThat(DtoFactory.createUserResponseDto(user).name()).isEqualTo("test");
    }

    @Test
    @DisplayName("UserUrls로부터 List<UserUrlResponse> 생성")
    void 리스트_생성() {
        User user = new User("test", "01012345678", "password1111", 20250605, Gender.MEN, Country.CHINA);
        UserUrl userUrl1 = new UserUrl(user, new Url("http://test.com", "test1"));
        UserUrl userUrl2 = new UserUrl(user, new Url("http://test2.com", "test2"));

        List<UserUrl> userUrls = List.of(userUrl1, userUrl2);
        Assertions.assertThat(DtoFactory.getBoardDto(userUrls).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("UserUrls로부터 UserUrlResponse생성")
    void UserUrl_로부터_response_생성() {
        User user = new User("test", "01012345678", "password1111", 20250605, Gender.MEN, Country.CHINA);
        UserUrl userUrl1 = new UserUrl(user, new Url("http://test.com", "test1"));

        Assertions.assertThat(DtoFactory.getUserUrlDto(userUrl1)).isNotEqualTo(user);
    }
}
