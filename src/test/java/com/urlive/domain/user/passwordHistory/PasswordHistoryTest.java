package com.urlive.domain.user.passwordHistory;


import com.urlive.domain.user.User;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PasswordHistoryTest {

    @Test
    @DisplayName("객체 생성 테스트")
    void 생성자_확인() {
        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국"));
        String password = "test123";
        PasswordHistory passwordHistory = new PasswordHistory(user, password);
        Assertions.assertThat(passwordHistory.getPassword()).isEqualTo(password);
    }
}
