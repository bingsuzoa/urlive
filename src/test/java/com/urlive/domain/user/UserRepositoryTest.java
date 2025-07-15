package com.urlive.domain.user;

import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryRepository;
import com.urlive.domain.user.passwordHistory.PasswordHistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

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
        countryRepository.deleteAll();
    }

    @Test
    @DisplayName("User 객체 저장 확인")
    void user_객체_저장() {
        countryRepository.save(new Country("KR", "대한민국"));
        Country country = countryRepository.findByIsoCode("KR").get();
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, country));
        Assertions.assertThat(userRepository.findById(user.getId()).get()).isEqualTo(user);
    }

    @Test
    @DisplayName("없는 ID 조회 시 Optional.Empty()반환")
    void 없는_ID_조회() {
        Optional<User> result = userRepository.findById(2L);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("User객체 생성 시 PasswordHistories 저장 확인")
    void 객체_생성시_passwordHistory추가() {
        countryRepository.save(new Country("KR", "대한민국"));
        Country country = countryRepository.findByIsoCode("KR").get();
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, country));

        Assertions.assertThat(user.getPasswordHistories().size()).isEqualTo(1);
    }
}
