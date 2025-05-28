package com.urlive.domain.user;

import java.util.Arrays;

public enum Country {
    KOREA(0),
    AMERICA(1),
    JAPAN(2),
    CHINA(3),
    ETC(4);

    private final int code;
    private static final String INVALID_COUNTRY = "해당하는 국가가 없습니다.";

    Country(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public Country getCountry(int code) {
        return Arrays.stream(Country.values())
                .filter(c -> c.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_COUNTRY));
    }
}
