package com.urlive.service.integrationTest;

import com.urlive.config.AsyncSyncTestConfig;
import com.urlive.config.TestRedisConfig;
import com.urlive.domain.user.option.country.CountryRepository;
import com.urlive.domain.user.option.country.CountryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
public class CountryServiceIntegrationTest {

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;


    @Test
    @DisplayName("isoCode로 국가 조회하기")
    void 국가_조회() {
        Assertions.assertThat(countryService.findByIsoCode("KR").getName()).isEqualTo("South Korea");
    }

    @Test
    @DisplayName("없는 코드로 조회 시도 시 예외 발생")
    void 없는_코드_예외_발생() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            countryService.findByIsoCode("NOTEXIST");
        });
    }
}
