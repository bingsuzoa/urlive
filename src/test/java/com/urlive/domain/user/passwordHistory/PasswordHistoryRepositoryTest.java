package com.urlive.domain.user.passwordHistory;


import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.urlive.service.PasswordService.FIRST_PAGE;
import static com.urlive.service.PasswordService.PASSWORD_HISTORY_LIMIT;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PasswordHistoryRepositoryTest {

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    User setUp() {
        return userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국")));
    }

    @Test
    @DisplayName("과거 변경했던 비밀번호 중 가장 최근 비밀번호 2개 조회하기")
    void 과거_비밀번호_조회() throws Exception {
        User user = setUp();
        Long userId = user.getId();

        PasswordHistory history1 = new PasswordHistory(user, "password1");
        passwordHistoryRepository.save(history1);
        Thread.sleep(1000);

        PasswordHistory history2 = new PasswordHistory(user, "password2");
        passwordHistoryRepository.save(history2);
        Thread.sleep(1000);

        PasswordHistory history3 = new PasswordHistory(user, "password3");
        passwordHistoryRepository.save(history3);
        Thread.sleep(1000);

        PasswordHistory history4 = new PasswordHistory(user, "password4");
        passwordHistoryRepository.save(history4);
        passwordHistoryRepository.flush();

        List<PasswordHistory> histories = passwordHistoryRepository.findRecentHistories(userId, PageRequest.of(FIRST_PAGE, PASSWORD_HISTORY_LIMIT));
        Assertions.assertThat(histories.get(0).getPassword()).isEqualTo("password4");
        Assertions.assertThat(histories.size()).isEqualTo(2);
    }

    /// ///예외 테스트
    @Test
    @DisplayName("비밀번호 60자 넘으면 에외 발생")
    void 비밀번호_60자확인() {
        User user = setUp();
        String password = "fdfljqlrjifjdlsnfjrhiqjfldnfdnfejqrafjlajflakjflakjflkajflkajlfkajlfkjalkfjlakfjljhoiqjfdfdnjkdhilfieolkrlkfnj";
        passwordHistoryRepository.save(new PasswordHistory(user, password));
        org.junit.jupiter.api.Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            passwordHistoryRepository.flush();
        });
    }
}
