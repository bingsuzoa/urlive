package com.urlive.domain.infrastructure;

import java.time.LocalDate;
import java.util.Map;

public class GroupedVisitStats {
    private LocalDate date;
    private Map<String, Long> stats;

    public LocalDate getDate() {
        return date;
    }

    public Map<String, Long> getStats() {
        return stats;
    }
}
