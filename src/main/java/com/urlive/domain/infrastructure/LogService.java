package com.urlive.domain.infrastructure;


import com.urlive.web.dto.log.LogDtoFactory;
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

@Service
public class LogService {

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    private static String[] domains = {"naver", "kakao", "google", "instagram", "youtube"};
    private static String[] devices = {"iPhone", "Android", "Windows"};
    private static final Parser parser = new Parser();

    private final LogRepository logRepository;


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

    public List<TrafficByDateRange> getTrafficsByDateRange(LocalDateTime start,
                                                           LocalDateTime end, String shortUrl) {
        return LogDtoFactory.getTrafficByDateRange(logRepository.findLogsByDateRange(shortUrl, start, end));
    }

    public List<TrafficByReferer> getTrafficsByReferer(LocalDateTime start,
                                                       LocalDateTime end, String shortUrl) {
        return LogDtoFactory.getTrafficsByReferer(logRepository.findLogsByReferer(shortUrl, start, end));
    }

    public List<TrafficByDevice> getTrafficsByDevice(LocalDateTime start,
                                                     LocalDateTime end, String shortUrl) {
        return LogDtoFactory.getTrafficsByDevice(logRepository.findLogsByDevice(shortUrl, start, end));
    }

}
