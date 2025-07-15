package com.urlive.web.dto.log;

import java.time.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class LogDtoFactory {

    public static List<TrafficByDateRange> getTrafficByDateRange(List<Object[]> logs) {
        List<TrafficByDateRange> trafficsByDateRange = new ArrayList<>();

        for (Object[] log : logs) {
            Date sqlDate = (Date) log[0];
            LocalDate date = sqlDate.toLocalDate();
            Long count = (long) log[1];
            trafficsByDateRange.add(new TrafficByDateRange(date, count));
        }
        return trafficsByDateRange;
    }

    public static List<TrafficByReferer> getTrafficsByReferer(List<Object[]> logs) {
        List<TrafficByReferer> trafficsByReferer = new ArrayList<>();

        for(Object[] log : logs) {
            Date sqlDate = (Date) log[0];
            LocalDate date = sqlDate.toLocalDate();
            String referer = (String) log[1];
            Long count = (long) log[2];
            trafficsByReferer.add(new TrafficByReferer(date, referer, count));
        }
        return trafficsByReferer;
    }

    public static List<TrafficByDevice> getTrafficsByDevice(List<Object[]> logs) {
        List<TrafficByDevice> trafficByDevices = new ArrayList<>();

        for(Object[] log : logs) {
            Date sqlDate = (Date) log[0];
            LocalDate date = sqlDate.toLocalDate();
            String device = (String) log[1];
            Long count = (long) log[2];
            trafficByDevices.add(new TrafficByDevice(date, device, count));
        }
        return trafficByDevices;
    }
}
