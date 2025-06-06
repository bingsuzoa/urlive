package com.urlive.web.dto.user;

import com.urlive.domain.user.option.Gender;
import com.urlive.domain.user.option.country.Country;

public record UserResponse(
        Long id,
        String name,
        int age,
        Gender gender,
        Country country
) {
}
