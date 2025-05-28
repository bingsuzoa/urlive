package com.urlive.controller.dto.user;

import com.urlive.domain.user.Country;
import com.urlive.domain.user.Gender;
import com.urlive.domain.user.User;

public record UserCreateRequestDto(
        String name,
        String phoneNumber,
        String password,
        int age,
        int gender,
        int country
) {

    public User toEntity() {
        return new User(name, phoneNumber, password, age, Gender.getGender(gender), Country.getCountry(country));
    }
}
