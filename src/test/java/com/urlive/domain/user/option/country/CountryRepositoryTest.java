package com.urlive.domain.user.option.country;

import com.urlive.config.AsyncSyncTestConfig;
import com.urlive.config.TestRedisConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    @DisplayName("국가 코드로 국가 조회")
    void 조회() {
        Assertions.assertThat(countryRepository.findByIsoCode("KR").get().getName()).isEqualTo("South Korea");
    }

}
