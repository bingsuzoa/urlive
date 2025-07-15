package com.urlive.domain.infrastructure.log;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @Mock
    private DateRangeAggregator dateRangeAggregator;

    @InjectMocks
    private LogService logService;

    private static final String RAW_REFERER = "http://naver.com";
    private static final String IP = "123.123.123.123";
    private static final String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 15_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148";
    private static final String SHORTURL = "abc123";

    @Test
    @DisplayName("로그 생성")
    void 로그_생성() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Referer")).thenReturn(RAW_REFERER);
        when(request.getRemoteAddr()).thenReturn(IP);
        when(request.getHeader("User-Agent")).thenReturn(USER_AGENT);

        Log log = new Log(SHORTURL, "unknown",
                "naver", "123.123.123.123", "iPhone");
        when(logRepository.save(any())).thenReturn(log);

        String shortUrl = SHORTURL;
        Log newLog = logService.createLog(request, shortUrl);
        Assertions.assertThat(newLog.getReferer()).isEqualTo("naver");
        Assertions.assertThat(newLog.getDevice()).isEqualTo("iPhone");
    }

    @Test
    @DisplayName("유입량 확인 테스트")
    void 유입량_확인_테스트() {
        List<Object[]> logs = new ArrayList<>();
        logs.add(new Object[]{"2025-07-10", 2L});
        logs.add(new Object[]{"2025-07-11", 3L});
        when(logRepository.findLogsByDateRange(any(), any(), any())).thenReturn(logs);

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> oneGroup = new HashMap<>();
        Map<String, Long> stats = new HashMap<>();
        stats.put("count", 2L);
        oneGroup.put("STATS", stats);
        oneGroup.put("RANGE", "2025-07-10");
        result.add(oneGroup);
        when(dateRangeAggregator.groupByInterval(anyList(), anyInt(), any())).thenReturn(result);

        LocalDateTime startDate = LocalDateTime.of(2025, 07, 10, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(2025, 07, 15, 00, 00);

        Assertions.assertThat(logService.getTrafficsByDateRange(30, startDate, endDate, "shortUrl").getFirst().get("RANGE"))
                .isEqualTo("2025-07-10");
    }

    @Test
    @DisplayName("유입경로별 유입량 확인 테스트")
    void 유입경로별_유입량_확인_테스트() {
        List<Object[]> logs = new ArrayList<>();
        logs.add(new Object[]{"2025-07-10", "instagram", 2L});
        when(logRepository.findLogsByReferer(any(), any(), any())).thenReturn(logs);

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> group1 = new HashMap<>();
        Map<String, Long> stats = new HashMap<>();
        stats.put("instagram", 2L);
        group1.put("STATS", stats);
        group1.put("RANGE", "2025-07-10");
        result.add(group1);

        Map<String, Object> group2 = new HashMap<>();
        Map<String, Long> stats1 = new HashMap<>();
        stats1.put("naver", 2L);
        group2.put("STATS", stats1);
        group2.put("RANGE", "2025-07-11");
        result.add(group2);
        when(dateRangeAggregator.groupByInterval(anyList(), anyInt(), any())).thenReturn(result);

        LocalDateTime startDate = LocalDateTime.of(2025, 07, 10, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(2025, 07, 15, 00, 00);

        Assertions.assertThat(logService.getTrafficsByReferer(7, startDate, endDate, "shortUrl").size())
                .isEqualTo(2);
    }

    @Test
    @DisplayName("디바이스별 유입량 확인 테스트")
    void 디바이스별_유입량_확인_테스트() {
        List<Object[]> logs = new ArrayList<>();
        logs.add(new Object[]{"2025-07-10", "instagram", 2L});
        when(logRepository.findLogsByReferer(any(), any(), any())).thenReturn(logs);

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> group1 = new HashMap<>();
        Map<String, Long> stats = new HashMap<>();
        stats.put("iPhone", 2L);
        group1.put("STATS", stats);
        group1.put("RANGE", "2025-07-10");
        result.add(group1);
        when(dateRangeAggregator.groupByInterval(anyList(), anyInt(), any())).thenReturn(result);

        LocalDateTime startDate = LocalDateTime.of(2025, 07, 10, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(2025, 07, 15, 00, 00);

        Map<String, Long> response = (Map<String, Long>) logService.getTrafficsByReferer(7, startDate, endDate, "shortUrl").getFirst().get("STATS");
        Assertions.assertThat(response.get("iPhone")).isEqualTo(2L);
    }

    @Test
    @DisplayName("시간별 유입량 확인 테스트")
    void 시간별_유입량_확인_테스트() {
        List<Object[]> logs = new ArrayList<>();
        logs.add(new Object[]{"2025-07-13 01:00:00", 2L});
        when(logRepository.findLogsPerTime(any(), any(), any())).thenReturn(logs);

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> group1 = new HashMap<>();
        Map<String, Long> stats = new HashMap<>();
        stats.put("count", 2L);
        group1.put("STATS", stats);
        group1.put("RANGE", "01:00:00");
        result.add(group1);
        when(dateRangeAggregator.groupByPerTime(anyList())).thenReturn(result);

        LocalDateTime startDate = LocalDateTime.of(2025, 07, 10, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(2025, 07, 15, 00, 00);

        Assertions.assertThat(logService.getTrafficsByDateRange(1, startDate, endDate, "shortUrl1").size()).isEqualTo(1L);
    }
}
