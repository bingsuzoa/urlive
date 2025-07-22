package com.urlive.domain.infrastructure.log;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DateRangeAggregatorTest {

    static DateRangeAggregator aggregator = new DateRangeAggregator();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    static LocalDate standardDay = LocalDate.of(2025, 6, 14);

    List<Object[]> logs = new ArrayList<>();

    @BeforeEach
    void init() throws ParseException {
        String str1 = "2025-06-14 12:00";
        Object[] log1 = {new java.sql.Date(sdf.parse(str1).getTime()), "instagram", 2};
        logs.add(log1);

        String str2 = "2025-06-15 11:00";
        Object[] log2 = {new java.sql.Date(sdf.parse(str2).getTime()), "instagram", 1};
        logs.add(log2);

        String str3 = "2025-06-16 17:00";
        Object[] log3 = {new java.sql.Date(sdf.parse(str3).getTime()), "kakao", 1};
        logs.add(log3);

        String str4 = "2025-07-20 17:00";
        Object[] log4 = {new java.sql.Date(sdf.parse(str4).getTime()), "instagram", 2};
        logs.add(log4);
    }

    @Test
    @DisplayName("days 30으로 조회할 경우 3일 간격으로 그룹핑")
    void days_30_인경우() {
        List<Map<String, Object>> result = aggregator.groupByInterval(logs, 30, standardDay);

        for (Map<String, Object> r : result) {
            String date = (String) r.get("range");
            Map<String, Long> stats = (Map<String, Long>) r.get("stats");
            if (date.equals("2025-06-14")) {
                Assertions.assertThat(stats.get("instagram")).isEqualTo(3L);
            }
        }
    }

    @Test
    @DisplayName("days 90으로 조회할 경우 7일 간격으로 그룹핑")
    void days_90_인경우() {
        List<Map<String, Object>> result = aggregator.groupByInterval(logs, 90, standardDay);

        for (Map<String, Object> r : result) {
            String date = (String) r.get("range");
            Map<String, Long> stats = (Map<String, Long>) r.get("stats");
            if (date.equals("2025-06-14")) {
                Assertions.assertThat(stats.get("instagram")).isEqualTo(5L);
            }
        }
    }

    @Test
    @DisplayName("days7으로 조회할 경우 1일 간격으로 그룹핑")
    void days_7_인경우() {
        List<Map<String, Object>> result = aggregator.groupByInterval(logs, 7, standardDay);

        for (Map<String, Object> r : result) {
            String date = (String) r.get("range");
            Map<String, Long> stats = (Map<String, Long>) r.get("stats");
            if (date.equals("2025-06-14")) {
                Assertions.assertThat(stats.get("instagram")).isEqualTo(2L);
            }
        }
    }

    @Test
    @DisplayName("days0으로 조회할 경우 시간 간격으로 그룹핑")
    void days_0_인경우() throws ParseException {
        List<Object[]> logs = new ArrayList<>();
        Object[] log4 = {"16:00:00", "naver", 3};
        logs.add(log4);
        Object[] log5 = {"17:00:00", "instagram", 2};
        logs.add(log5);

        List<Map<String, Object>> result = aggregator.groupByPerTime(logs);
        Assertions.assertThat(result.size()).isEqualTo(8);

        for (Map<String, Object> r : result) {
            String date = (String) r.get("range");
            Map<String, Long> stats = (Map<String, Long>) r.get("stats");
            if (date.equals("17:00:00")) {
                Assertions.assertThat(stats.get("instagram")).isEqualTo(2L);
            }
            if (date.equals("16:00:00")) {
                Assertions.assertThat(stats.get("instagram")).isEqualTo(3L);
            }
        }
    }
}
