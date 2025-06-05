package com.urlive.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    @DisplayName("비밀번호 변경 확인")
    void 비밀번호_변경() {
        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, Country.CHINA);
        String newPassword = "newPassword";
        user.changePassword(newPassword);
        Assertions.assertThat(user.getPassword()).isEqualTo(newPassword);
    }
}
