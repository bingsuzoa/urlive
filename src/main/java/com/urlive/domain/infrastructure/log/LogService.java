package com.urlive.domain.infrastructure.log;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua_parser.Parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class LogService {

    private static final Logger log = LoggerFactory.getLogger(LogService.class);

    public LogService(
            LogRepository logRepository,
            DateRangeAggregator dateRangeAggregator) {
        this.logRepository = logRepository;
        this.dateRangeAggregator = dateRangeAggregator;
    }

    private static String[] domains = {"naver", "kakao", "google", "instagram", "youtube"};
    private static String[] devices = {"iPhone", "Android", "Windows"};
    private static final Parser parser = new Parser();
    private static LocalDate today = LocalDate.now();

    private final LogRepository logRepository;
    private final DateRangeAggregator dateRangeAggregator;


    @Transactional
    public Log createLog(HttpServletRequest request, String shortUrl) {
        String rawReferer = request.getHeader("Referer");
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String device = getDevice(userAgent);
        String referer = getParsedReferer(rawReferer);
        return logRepository.save(new Log(shortUrl, rawReferer, referer, ip, device));
    }

    private String getParsedReferer(String rawReferer) {
        if (rawReferer == null || rawReferer.isEmpty()) {
            return Log.UNKNOWN_CONNECTION;
        }
        for (String domain : domains) {
            if (rawReferer.contains(domain)) {
                return domain;
            }
        }
        return Log.UNKNOWN_CONNECTION;
    }

    private String getDevice(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return Log.UNKNOWN_CONNECTION;
        }

        userAgent = userAgent.toLowerCase();

        for (String device : devices) {
            if (userAgent.contains("iphone")) {
                return device;
            }
            if (userAgent.contains("android")) {
                return device;
            }
            if (userAgent.contains("mac")) {
                return device;
            }
        }
        return Log.UNKNOWN_CONNECTION;
    }

    public List<Map<String, Object>> getTrafficsByDateRange(
            int days, LocalDateTime start, LocalDateTime end, String shortUrl) {

        if (days == 0 || days == 1) {
            return getTrafficsByDatePerHour(days, shortUrl);
        }
        return dateRangeAggregator.groupByInterval(logRepository.findLogsByDateRange(shortUrl, start, end), days, today);
    }

    public List<Map<String, Object>> getTrafficsByReferer(
            int days, LocalDateTime start, LocalDateTime end, String shortUrl
    ) {
        if (days == 0 || days == 1) {
            return getTrafficsByRefererPerHour(days, shortUrl);
        }
        return dateRangeAggregator.groupByInterval(logRepository.findLogsByReferer(shortUrl, start, end), days, today);
    }

    public List<Map<String, Object>> getTrafficsByDevice(
            int days, LocalDateTime start, LocalDateTime end, String shortUrl
    ) {
        if (days == 0 || days == 1) {
            return getTrafficsByDevicePerHour(days, shortUrl);
        }
        return dateRangeAggregator.groupByInterval(logRepository.findLogsByDevice(shortUrl, start, end), days, today);
    }

    private List<Map<String, Object>> getTrafficsByDatePerHour(int days, String shortUrl) {
        String[] range = getDateRangeByDays(days);
        String start = range[0];
        String end = range[1];
        return dateRangeAggregator.groupByPerTime(logRepository.findLogsPerTime(shortUrl, start, end));
    }

    private List<Map<String, Object>> getTrafficsByRefererPerHour(int days, String shortUrl) {
        String[] range = getDateRangeByDays(days);
        String start = range[0];
        String end = range[1];
        return dateRangeAggregator.groupByPerTime(logRepository.findLogsByRefererPerTime(shortUrl, start, end));
    }

    private List<Map<String, Object>> getTrafficsByDevicePerHour(int days, String shortUrl) {
        String[] range = getDateRangeByDays(days);
        String start = range[0];
        String end = range[1];
        return dateRangeAggregator.groupByPerTime(logRepository.findLogsByDevicePerTime(shortUrl, start, end));
    }

    private String[] getDateRangeByDays(int days) {
        DateTimeFormatter startFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00");
        DateTimeFormatter endFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59");

        if (days == 0) {
            LocalDateTime now = LocalDateTime.now();
            String start = startFormatter.format(now);
            String end = endFormatter.format(now);
            return new String[]{start, end};
        }
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        String start = startFormatter.format(yesterday);
        String end = endFormatter.format(yesterday);
        return new String[]{start, end};
    }

}
