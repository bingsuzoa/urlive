package com.urlive.service;

import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;
import com.urlive.domain.user.passwordHistory.PasswordHistory;
import com.urlive.domain.user.passwordHistory.PasswordHistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PasswordServiceTest {

    @Mock
    private PasswordHistoryRepository passwordHistoryRepository;

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        passwordService = new PasswordService(
                new BCryptPasswordEncoder(),
                passwordHistoryRepository
        );
    }

    /// ////해피 테스트
    @Test
    @DisplayName("비밀번호 해시 정상 작동 확인 테스트")
    void 해시_작동_확인() {
        String rawPassword = "test123";
        Assertions.assertThat(passwordService.encode(rawPassword)).isNotEqualTo(rawPassword);
    }

    @Test
    @DisplayName("로그인 시 비밀번호 매칭하는 테스트")
    void 해시_매칭_확인() {
        String rawPassword = "test123";
        String encodedPassword = passwordService.encode(rawPassword);
        Assertions.assertThat(passwordService.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("최근 변경했던 비밀번호들 중 변경하려는 비밀번호와 일치하는지 확인하는 테스트")
    void 기존_비밀번호_확인() {
        String rawPassword = "test124";
        String existingPassword = passwordService.encode("test123");

        User user = new User("test", "01012345678", existingPassword, 20250604, Gender.WOMEN, Country.AMERICA);
        List<PasswordHistory> histories = List.of(new PasswordHistory(user, existingPassword));
        when(passwordHistoryRepository.findRecentHistories(any(), any())).thenReturn(histories);

        Assertions.assertThat(passwordService.changePassword(1L, rawPassword)).isNotNull();
    }

    /// ////에러 테스트
    @Test
    @DisplayName("로그인 시 비밀번호 매칭 실패 테스트")
    void 해시_매칭_실패() {
        String rawPassword = "test123";
        String encodedPassword = passwordService.encode("notMatchingTest");
        Assertions.assertThat(passwordService.matches(rawPassword, encodedPassword)).isFalse();
    }

    @Test
    @DisplayName("최근 변경했던 비밀번호들 중 변경하려는 비밀번호와 일치할 경우 예외 발생 테스트")
    void 기존_비밀번호_존재() {
        String rawPassword = "test123";
        String existingPassword = passwordService.encode("test123");

        User user = new User("test", "01012345678", existingPassword, 20250604, Gender.WOMEN, Country.AMERICA);
        List<PasswordHistory> histories = List.of(new PasswordHistory(user, existingPassword));
        when(passwordHistoryRepository.findRecentHistories(any(), any())).thenReturn(histories);

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            passwordService.changePassword(1L, rawPassword);
        });
    }
}
