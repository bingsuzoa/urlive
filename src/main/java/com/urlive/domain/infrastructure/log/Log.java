package com.urlive.domain.infrastructure.log;

import com.urlive.domain.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "log_id"))
public class Log extends BaseEntity {

    protected Log() {

    }

    public Log(String shortUrl, String rawReferer, String referer, String ip, String device) {
        this.shortUrl = shortUrl;
        this.rawReferer = isBlank(rawReferer) ? UNKNOWN_CONNECTION : rawReferer;
        this.referer = isBlank(referer) ? UNKNOWN_CONNECTION : referer;
        this.ip = isBlank(ip) ? UNKNOWN_CONNECTION : ip;
        this.device = isBlank(device) ? UNKNOWN_CONNECTION : device;
    }

    public static String UNKNOWN_CONNECTION = "unknown";

    @Column(nullable = false)
    String shortUrl;

    String rawReferer;

    String referer;

    String ip;

    String device;

    public String getShortUrl() {
        return shortUrl;
    }

    public String getRawReferer() {
        return rawReferer;
    }

    public String getReferer() {
        return referer;
    }

    public String getIp() {
        return ip;
    }

    public String getDevice() {
        return device;
    }

    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}
