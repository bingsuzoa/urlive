package com.urlive.domain.user.passwordHistory;


import com.urlive.config.JpaAuditingConfig;
import com.urlive.domain.user.User;
import com.urlive.domain.user.UserRepository;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryRepository;
import com.urlive.domain.userUrl.UserUrlRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.urlive.domain.user.passwordHistory.PasswordService.FIRST_PAGE;
import static com.urlive.domain.user.passwordHistory.PasswordService.PASSWORD_HISTORY_LIMIT;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class)
public class PasswordHistoryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUrlRepository userUrlRepository;

    @AfterEach
    void delete() {
        passwordHistoryRepository.deleteAll();
        userRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    @DisplayName("과거 변경했던 비밀번호 중 가장 최근 비밀번호 2개 조회하기")
    void 과거_비밀번호_조회() throws InterruptedException {
        Country country = countryRepository.saveAndFlush(new Country("KR", "South Korea"));
        User firstUser = new User("test", "01011112222", "test1234",
                20250312, Gender.MEN, country);
        User user = userRepository.saveAndFlush(firstUser);
        Thread.sleep(1000);

        PasswordHistory history1 = new PasswordHistory(user, "password1");
        Thread.sleep(1000);
        passwordHistoryRepository.saveAndFlush(history1);

        PasswordHistory history2 = new PasswordHistory(user, "password2");
        Thread.sleep(1000);
        passwordHistoryRepository.saveAndFlush(history2);

        PasswordHistory history3 = new PasswordHistory(user, "password3");
        Thread.sleep(1000);
        passwordHistoryRepository.saveAndFlush(history3);

        PasswordHistory history4 = new PasswordHistory(user, "password4");
        Thread.sleep(1000);
        passwordHistoryRepository.saveAndFlush(history4);

        User updatedUser = userRepository.findById(user.getId()).get();
        List<PasswordHistory> histories = passwordHistoryRepository.findRecentHistories(updatedUser.getId(), PageRequest.of(FIRST_PAGE, PASSWORD_HISTORY_LIMIT));
        Assertions.assertThat(histories.get(0).getPassword()).isEqualTo("password4");
        Assertions.assertThat(histories.size()).isEqualTo(2);
    }
}
