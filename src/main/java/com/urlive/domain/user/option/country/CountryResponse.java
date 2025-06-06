package com.urlive.domain.user.option.country;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryResponse {

    @JsonProperty("cca2")
    private String isoCode;

    @JsonProperty("name")
    private Name name;

    public static class Name {
        @JsonProperty("common")
        private String commonName;

        public String getCommonName() {
            return commonName;
        }
    }

    public String getIsoCode() {
        return isoCode;
    }

    public Name getName() {
        return name;
    }
}
