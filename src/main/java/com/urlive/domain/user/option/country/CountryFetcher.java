package com.urlive.domain.user.option.country;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class CountryFetcher {

    private static final String URL = "https://restcountries.com/v3.1/all?fields=name,cca2";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<CountryResponse> fetchCountries() {
        CountryResponse[] responses = restTemplate.getForObject(URL, CountryResponse[].class);
        return List.of(responses);
    }
}
