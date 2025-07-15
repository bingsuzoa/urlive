package com.urlive.domain.infrastructure;

import java.time.LocalDate;

public class SimpleVisitStats {
    private LocalDate date;
    private Long count;

    public LocalDate getDate() {
        return date;
    }

    public Long getCount() {
        return count;
    }
}
