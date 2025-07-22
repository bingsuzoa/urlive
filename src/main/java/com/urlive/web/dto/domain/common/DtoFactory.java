package com.urlive.web.dto.domain.common;


import com.urlive.domain.url.Url;
import com.urlive.domain.user.User;
import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;
import com.urlive.web.dto.domain.user.UserResponse;
import com.urlive.web.dto.domain.user.countryDto.CountryDto;
import com.urlive.web.dto.domain.userUrl.UserUrlResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DtoFactory {

    public static UserResponse createUserResponseDto(User user, Country country) {
        Long id = user.getId();
        String name = user.getName();
        int age = user.getAge();
        Gender gender = user.getGender();
        CountryDto countryDto = new CountryDto(country.getId(), country.getIsoCode(), country.getName());
        return new UserResponse(id, name, age, gender, countryDto);
    }

    public static UserResponse createUserResponseDto(User user) {
        Long id = user.getId();
        String name = user.getName();
        int age = user.getAge();
        Gender gender = user.getGender();
        Country country = user.getCountry();
        CountryDto countryDto = new CountryDto(country.getId(), country.getIsoCode(), country.getName());
        return new UserResponse(id, name, age, gender, countryDto);
    }

    public static List<UserUrlResponse> getBoardDto(Set<Url> urls) {
        List<UserUrlResponse> userUrlsResponse = new ArrayList<>();

        for (Url url : urls) {
            Long id = url.getId();
            String originalUrl = url.getOriginalUrl();
            String shortUrl = url.getShortUrl();
            String title = url.getTitle();
            LocalDateTime createdAt = url.getCreatedAt();
            Long viewCount = url.getViewCount();

            userUrlsResponse.add(new UserUrlResponse(id, originalUrl, shortUrl, title, createdAt, viewCount));
        }
        return userUrlsResponse;
    }

    public static UserUrlResponse getUserUrlDto(Url url) {
        Long id = url.getId();
        String originalUrl = url.getOriginalUrl();
        String shortUrl = url.getShortUrl();
        String title = url.getTitle();
        LocalDateTime createdAt = url.getCreatedAt();
        Long viewCount = url.getViewCount();
        return new UserUrlResponse(id, originalUrl, shortUrl, title, createdAt, viewCount);
    }

    public static CountryDto getCountryDto(Country country) {
        return new CountryDto(country.getId(), country.getIsoCode(), country.getName());
    }

    public static List<CountryDto> getCountries(List<Country> countries) {
        List<CountryDto> countryDtos = new ArrayList<>();

        for (Country country : countries) {
            countryDtos.add(new CountryDto(country.getId(), country.getIsoCode(), country.getName()));
        }
        return countryDtos;
    }
}
