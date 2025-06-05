package com.urlive.domain.user.passwordHistory;

import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PasswordHistoryTest {

    @Test
    @DisplayName("객체 생성 테스트")
    void 생성자_확인() {
        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, Country.CHINA);
        String password = "test123";
        PasswordHistory passwordHistory = new PasswordHistory(user, password);
        Assertions.assertThat(passwordHistory.getPassword()).isEqualTo(password);
    }
}
