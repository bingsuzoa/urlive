package com.urlive.domain.user.option.country;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@EnableRetry
public class CountryFetcherTest {

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private InitCountryRunner initCountryRunner;

    @Autowired
    private CountryFetcher countryFetcher;

    @Test
    @DisplayName("예외 발생하거나, 응답 느려질 경우 재시도 하는지 확인")
    void 오류_발생시_재시도_확인() {
        when(restTemplate.getForObject(
                eq(CountryFetcher.URL),
                eq(CountryResponse[].class)
        )).thenThrow(ResourceAccessException.class);

        List<CountryResponse> responses = countryFetcher.fetchCountries();
        Mockito.verify(restTemplate, times(3))
                .getForObject(
                        eq(CountryFetcher.URL),
                        eq(CountryResponse[].class));

        Assertions.assertThat(responses).isEmpty();
    }
}
