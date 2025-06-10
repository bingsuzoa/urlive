package com.urlive.domain.user.option.country;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class CountryFetcher {

    public CountryFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static final String URL = "https://restcountries.com/v3.1/all?fields=name,cca2";
    private static final String FAIL_GET_COUNTRY_MESSAGE = "국가 정보 API 재시도 실패 : {}";
    private static final Logger log = LoggerFactory.getLogger(CountryFetcher.class);
    private RestTemplate restTemplate;

    @Retryable(
            value = {ResourceAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public List<CountryResponse> fetchCountries() {
        CountryResponse[] responses = restTemplate.getForObject(URL, CountryResponse[].class);
        return List.of(responses);
    }

    @Recover
    public List<CountryResponse> recover(ResourceAccessException e) {
        log.error(FAIL_GET_COUNTRY_MESSAGE, e.getMessage());
        return Collections.emptyList();
    }
}
