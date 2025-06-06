package com.urlive.web.dto.user;

import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserResponseTest {

    @Test
    @DisplayName("userResponse 생성 테스트")
    void userResponse_생성_테스트() {
        UserResponse response = new UserResponse(
                1L,
                "abc123",
                20250503,
                Gender.WOMEN,
                new Country("KR", "대한민국")
        );

        Assertions.assertThat(response.id()).isEqualTo(1L);
        Assertions.assertThat(response.name()).isEqualTo("abc123");
        Assertions.assertThat(response.age()).isEqualTo(20250503);
        Assertions.assertThat(response.gender()).isEqualTo(Gender.WOMEN);
        Assertions.assertThat(response.country().getIsoCode()).isEqualTo("KR");
    }
}
