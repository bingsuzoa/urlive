package com.urlive.web.dto.domain.user;

import com.urlive.domain.user.option.Gender;
import com.urlive.web.dto.domain.user.countryDto.CountryDto;

public record UserResponse(
        Long id,
        String name,
        int age,
        Gender gender,
        CountryDto countryDto
) {
}
