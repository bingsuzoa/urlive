package com.urlive.web.dto.common;

import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.web.dto.domain.common.DtoFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DtoFactoryTest {

    @Test
    @DisplayName("user객체로부터 userResponse 생성")
    void user_로부터_userResponse() {
        User user = new User("test", "01012345678", "password1111", 20250605, Gender.MEN, new Country("KR", "대한민국"));

        Assertions.assertThat(DtoFactory.createUserResponseDto(user)).isNotEqualTo(user);
        Assertions.assertThat(DtoFactory.createUserResponseDto(user).name()).isEqualTo("test");
    }

//    @Test
//    @DisplayName("UserUrls로부터 List<UserUrlResponse> 생성")
//    void 리스트_생성() {
//        User user = new User("test", "01012345678", "password1111", 20250605, Gender.MEN, new Country("KR", "대한민국"));
//        Url url1 = new Url(user, "http://test.com", "test1");
//        Url url2 = new Url(user, "http://test2.com", "test2");
//
//        List<Url> userUrls = List.of(url1, url2);
//        Assertions.assertThat(DtoFactory.getBoardDto(userUrls).size()).isEqualTo(2);
//    }

    @Test
    @DisplayName("UserUrls로부터 UserUrlResponse생성")
    void UserUrl_로부터_response_생성() {
        User user = new User("test", "01012345678", "password1111", 20250605, Gender.MEN, new Country("KR", "대한민국"));
        Url url1 = new Url(user, "http://test.com", "test1");

        Assertions.assertThat(DtoFactory.getUserUrlDto(url1)).isNotEqualTo(user);
    }
}
