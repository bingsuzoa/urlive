package com.urlive.domain.user.option.country;

import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitCountryRunner implements CommandLineRunner {

    public InitCountryRunner(CountryFetcher countryFetcher,
                             CountryRepository countryRepository
    ) {
        this.countryFetcher = countryFetcher;
        this.countryRepository = countryRepository;
    }

    private final CountryFetcher countryFetcher;
    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {
        if(countryRepository.count() == 0) {
            initCountries();
        }
    }

    private void initCountries() {
        List<CountryResponse> countryResponses = countryFetcher.fetchCountries();
        List<Country> countries = countryResponses.stream()
                .map(c -> new Country(c.getIsoCode(), c.getName().getCommonName()))
                .toList();
        countryRepository.saveAll(countries);
    }

    @Async
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
