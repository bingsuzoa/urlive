package com.urlive.domain.user.option.country;

import com.urlive.service.CountryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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
