package com.urlive.web.dto.user.country;

public record CountryDto(
        Long id,
        String isoCode,
        String name
) {
}