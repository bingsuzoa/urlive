package com.urlive.web.dto.log;

import java.time.LocalDate;

public record TrafficByReferer(LocalDate date,
                               String referer,
                               long count
) {
}
