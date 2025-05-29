package com.urlive.controller.dto.user;

import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;

public record UserResponse(
        Long id,
        String name,
        int age,
        Gender gender,
        Country country
) {
}
