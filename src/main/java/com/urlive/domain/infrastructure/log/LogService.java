package com.urlive.domain.infrastructure.log;


import com.urlive.web.dto.log.TrafficByDateRange;
import com.urlive.web.dto.log.TrafficByDevice;
import com.urlive.web.dto.log.TrafficByReferer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua_parser.Client;
import ua_parser.Parser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class LogService {

    public LogService(
            LogRepository logRepository,
            DateRangeAggregator dateRangeAggregator) {
        this.logRepository = logRepository;
        this.dateRangeAggregator = dateRangeAggregator;
    }

    private static String[] domains = {"naver", "kakao", "google", "instagram", "youtube"};
    private static String[] devices = {"iPhone", "Android", "Windows"};
    private static final Parser parser = new Parser();

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
            if (rawReferer.equals(domain)) {
                return domain;
            }
        }
        return Log.UNKNOWN_CONNECTION;
    }

    private String getDevice(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return Log.UNKNOWN_CONNECTION;
        }
        Client client = parser.parse(userAgent);
        String os = client.os.family;

        for (String device : devices) {
            if (os.equalsIgnoreCase(device)) {
                return device;
            }
        }
        return Log.UNKNOWN_CONNECTION;
    }

    public List<Map<String, Object>> getTrafficsByDateRange(
            int days, LocalDateTime start, LocalDateTime end, String shortUrl) {
        return dateRangeAggregator.groupByInterval(logRepository.findLogsByDateRange(shortUrl, start, end), days);
    }

    public List<Map<String, Object>> getTrafficsByReferer(
            int days, LocalDateTime start, LocalDateTime end, String shortUrl
    ) {
        return dateRangeAggregator.groupByInterval(logRepository.findLogsByReferer(shortUrl, start, end), days);
    }

    public List<Map<String, Object>> getTrafficsByDevice(
            int days, LocalDateTime start, LocalDateTime end, String shortUrl
    ) {
        return dateRangeAggregator.groupByInterval(logRepository.findLogsByDevice(shortUrl, start, end), days);
    }

}
