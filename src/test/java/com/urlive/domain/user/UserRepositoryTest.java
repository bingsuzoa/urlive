package com.urlive.domain.user;

import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User 객체 저장 확인")
    void user_객체_저장() {
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국")));

        Assertions.assertThat(userRepository.findById(user.getId()).get()).isEqualTo(user);
    }

    @Test
    @DisplayName("없는 ID 조회 시 Optional.Empty()반환")
    void 없는_ID_조회() {
        User user = new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국"));
        userRepository.save(user);
        userRepository.flush();

        Optional<User> result = userRepository.findById(2L);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("User객체 생성 시 PasswordHistories 저장 확인")
    void 객체_생성시_passwordHistory추가() {
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국")));

        Assertions.assertThat(user.getPasswordHistories().size()).isEqualTo(1);
    }

    /// //예외 테스트
    @Test
    @DisplayName("회원가입 정보에 null값 있을 경우 예외")
    void 회원가입시_null값_예외() {
        User user = new User(null, "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국"));

        org.junit.jupiter.api.Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user);
            userRepository.flush();
        });
    }

    @Test
    @DisplayName("동일한 휴대폰 번호가 저장되어 있을 경우 예외")
    void 존재하는_객체() {
        User user = userRepository.save(new User("test", "01012345678", "1234", 2025, Gender.MEN, new Country("KR", "대한민국")));
        userRepository.flush();

        org.junit.jupiter.api.Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(new User("test2", "01012345678", "123456", 2025, Gender.MEN, new Country("KR", "대한민국")));
            userRepository.flush();
        });
    }

}
