package com.urlive.domain.infrastructure.log;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class DateRangeAggregator {

    public List<Map<String, Object>> groupByInterval(List<Object[]> logs, int days) {
        if (logs == null || logs.isEmpty()) {
            return Collections.emptyList();
        }

        int intervalDays = getIntervalForRange(days);
        logs.sort(Comparator.comparing(o -> (Date) o[0]));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        List<LocalDate> ranges = new ArrayList<>();
        LocalDate d = startDate;
        while (!d.isAfter(endDate)) {
            ranges.add(d);
            d = d.plusDays(intervalDays);
        }

        Map<LocalDate, Map<String, Long>> grouped = new LinkedHashMap<>();
        for (LocalDate range : ranges) {
            grouped.put(range, new HashMap<>());
        }

        Set<String> allCategories = new HashSet<>();

        for (Object[] log : logs) {
            LocalDate date;
            String category;
            long count;

            if (log.length == 2) {
                date = ((java.sql.Date) log[0]).toLocalDate();
                category = "count";
                count = ((Number) log[1]).longValue();
            } else {
                date = ((java.sql.Date) log[0]).toLocalDate();
                category = log[1] == null ? "unknown" : log[1].toString();
                count = ((Number) log[2]).longValue();
            }

            long daysBetween = ChronoUnit.DAYS.between(startDate, date);
            int index = (int) (daysBetween / intervalDays);
            if (index < 0 || index >= ranges.size()) {
                continue;
            }
            LocalDate rangeStart = ranges.get(index);

            Map<String, Long> stats = grouped.get(rangeStart);
            stats.put(category, stats.getOrDefault(category, 0L) + count);
            allCategories.add(category);
        }
        for (Map<String, Long> stats : grouped.values()) {
            for (String cat : allCategories) {
                stats.putIfAbsent(cat, 0L);
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<LocalDate, Map<String, Long>> entry : grouped.entrySet()) {
            Map<String, Object> oneGroup = new HashMap<>();
            oneGroup.put("range", entry.getKey().toString());
            oneGroup.put("stats", entry.getValue());
            result.add(oneGroup);
        }

        return result;
    }

    private int getIntervalForRange(int days) {
        if (days <= 7) return 1;
        if (days <= 30) return 3;
        if (days <= 90) return 7;
        return 30;
    }
}
