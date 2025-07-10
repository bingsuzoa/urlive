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

    public static List<TrafficByReferer> getTrafficsByReferer(List<Object[]> logs) {
        List<TrafficByReferer> trafficsByReferer = new ArrayList<>();

        for(Object[] log : logs) {
            LocalDate date = (LocalDate) log[0];
            String referer = (String) log[1];
            int count = (int) log[2];
            trafficsByReferer.add(new TrafficByReferer(date, referer, count));
        }
        return trafficsByReferer;
    }
}
