package com.urlive.domain.userUrl;

import com.urlive.domain.url.Url;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.user.option.country.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserUrlTest {

    @Test
    @DisplayName("UserUrl 객체 생성시 처음 title은 Empty상태")
    void title_초기_상태_확인() {
        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국"));
        Url url = new Url("http://test.com", "testShortUrl");
        UserUrl userUrl = new UserUrl(user, url);
        Assertions.assertThat(userUrl.getTitle()).isEmpty();
    }

    @Test
    @DisplayName("UserUrl title변경")
    void title_변경() {
        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국"));
        Url url = new Url("http://test.com", "testShortUrl");
        UserUrl userUrl = new UserUrl(user, url);

        String newTitle = "newTitle";
        userUrl.updateTitle(newTitle);
        Assertions.assertThat(userUrl.getTitle()).isEqualTo(newTitle);
    }
}
