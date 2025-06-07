package com.urlive.service;

import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @Test
    @DisplayName("isoCode로 국가 조회하기")
    void 국가_조회() {
        String isoCode = "KR";
        String name = "Korea";
        Country country = new Country(isoCode, name);
        when(countryRepository.findByIsoCode(any())).thenReturn(Optional.of(country));
        Assertions.assertThat(countryService.findByIsoCode(isoCode).getName()).isEqualTo(name);
    }
}
