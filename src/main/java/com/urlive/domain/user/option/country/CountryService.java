package com.urlive.domain.user.option.country;


import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CountryService {

    public CountryService(
            CountryRepository countryRepository,
            CountryFetcher countryFetcher
    ) {
        this.countryRepository = countryRepository;
        this.countryFetcher = countryFetcher;
    }

    private static final String NOT_EXIST_COUNTRY = "존재하지 않는 국가 코드입니다.";
    private final CountryFetcher countryFetcher;
    private final CountryRepository countryRepository;

    @Transactional(readOnly = true)
    public Country findByIsoCode(String isoCode) {
        return countryRepository.findByIsoCode(isoCode)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_COUNTRY));
    }

    @Async
    public void initCountries() {
        if(countryRepository.count() == 0) {
            List<CountryResponse> countryResponses = countryFetcher.fetchCountries();
            List<Country> countries = countryResponses.stream()
                    .map(c -> new Country(c.getIsoCode(), c.getName().getCommonName()))
                    .toList();
            countryRepository.saveAll(countries);
        }
    }

    @Scheduled(cron = "0 0 3 ? * MON")
    public void updateCountriesWeekly() {
        List<CountryResponse> countryResponses = countryFetcher.fetchCountries();
        List<Country> countries = countryResponses.stream()
                .map(c -> new Country(c.getIsoCode(), c.getName().getCommonName()))
                .toList();
        countryRepository.deleteAll();
        countryRepository.saveAll(countries);
    }
}
