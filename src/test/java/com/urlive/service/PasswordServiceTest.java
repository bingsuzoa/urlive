package com.urlive.service;

import com.urlive.domain.user.passwordHistory.PasswordHistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


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

    /// ////에러 테스트
    @Test
    @DisplayName("로그인 시 비밀번호 매칭 실패 테스트")
    void 해시_매칭_실패() {
        String rawPassword = "test123";
        String encodedPassword = passwordService.encode("notMatchingTest");
        Assertions.assertThat(passwordService.matches(rawPassword, encodedPassword)).isFalse();
    }
}
