package com.urlive.service;


import com.urlive.domain.user.option.country.Country;
import com.urlive.domain.user.option.country.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountryService {

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    private static final String NOT_EXIST_COUNTRY = "존재하지 않는 국가 코드입니다.";
    private final CountryRepository countryRepository;

    @Transactional(readOnly = true)
    public Country findByIsoCode(String isoCode) {
        return countryRepository.findByIsoCode(isoCode)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_COUNTRY));
    }
}
