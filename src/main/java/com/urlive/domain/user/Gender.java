package com.urlive.domain.user;

import java.util.Arrays;

public enum Gender {
    MEN(0),
    WOMEN(1);

    private final int code;
    private static final String INVALID_GENDER = "해당하는 성별이 없습니다.";

    Gender(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Gender getGender(int code) {
        return Arrays.stream(Gender.values())
                .filter(g -> g.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_GENDER));
    }
}
