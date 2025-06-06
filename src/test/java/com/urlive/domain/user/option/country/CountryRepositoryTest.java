package com.urlive.domain.user.option.country;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    @DisplayName("국가 코드로 국가 조회")
    void 조회() {
        Assertions.assertThat(countryRepository.findByIsoCode("KR").get().getName()).isEqualTo("대한민국");
    }

}
