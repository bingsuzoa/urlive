package com.urlive.web.dto.log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LogDtoFactory {

    public static List<TrafficByDateRange> getTrafficByDateRange(List<Object[]> logs) {
        List<TrafficByDateRange> trafficsByDateRange = new ArrayList<>();

        for (Object[] log : logs) {
            LocalDate date = (LocalDate) log[0];
            int count = (int) log[1];
            trafficsByDateRange.add(new TrafficByDateRange(date, count));
        }
        return trafficsByDateRange;
    }
}
