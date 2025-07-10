package com.urlive.web.dto.log;

import java.time.LocalDate;

public record TrafficByDateRange(LocalDate date, int count) {
}
