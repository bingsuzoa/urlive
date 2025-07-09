package com.urlive.service.integrationTest;

import com.urlive.config.AsyncSyncTestConfig;
import com.urlive.config.TestRedisConfig;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryRepository;
import com.urlive.domain.user.passwordHistory.PasswordHistory;
import com.urlive.domain.user.passwordHistory.PasswordHistoryRepository;
import com.urlive.service.CountryService;
import com.urlive.service.PasswordService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
public class PasswordServiceIntegrationTest {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    @AfterEach
    void deleteAll() {
        userRepository.deleteAll();
        passwordHistoryRepository.deleteAll();
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
        String encodedPassword = passwordService.encode("test123");
        Country country = countryRepository.findByIsoCode("KR").get();

        User user = userRepository.save(new User("test", "01012345678", encodedPassword, 20250604, Gender.WOMEN, country));
        passwordHistoryRepository.save(new PasswordHistory(user, encodedPassword));
        Assertions.assertThat(passwordService.changePassword(user.getId(), rawPassword)).isNotNull();
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
        Country country = countryRepository.findByIsoCode("KR").get();

        User user = userRepository.save(new User("test", "01012345678", existingPassword, 20250604, Gender.WOMEN, country));
        List<PasswordHistory> histories = List.of(new PasswordHistory(user, existingPassword));
        passwordHistoryRepository.saveAll(histories);

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            passwordService.changePassword(1L, rawPassword);
        });
    }

}
