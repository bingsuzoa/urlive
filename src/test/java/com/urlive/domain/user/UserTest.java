package com.urlive.domain.user;

import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    @DisplayName("비밀번호 변경 확인")
    void 비밀번호_변경() {
        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국"));
        String newPassword = "newPassword";
        user.changePassword(newPassword);
        Assertions.assertThat(user.getPassword()).isEqualTo(newPassword);
    }
}
