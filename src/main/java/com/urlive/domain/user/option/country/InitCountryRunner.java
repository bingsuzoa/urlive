package com.urlive.domain.user.option.country;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitCountryRunner implements CommandLineRunner {

    public InitCountryRunner(
            CountryFetcher countryFetcher,
            CountryService countryService
    ) {
        this.countryFetcher = countryFetcher;
        this.countryService = countryService;
    }

    private final CountryFetcher countryFetcher;
    private final CountryService countryService;

    @Override
    public void run(String... args) {
        countryService.initCountries();
    }
}
