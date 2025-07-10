package com.urlive.web.dto.domain.user.country;

public record CountryDto(
        Long id,
        String isoCode,
        String name
) {
}