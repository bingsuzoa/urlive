package com.urlive.web.dto.log;

import java.time.LocalDate;

public record TrafficByDevice(LocalDate date,
                              String device,
                              long count) {
}
