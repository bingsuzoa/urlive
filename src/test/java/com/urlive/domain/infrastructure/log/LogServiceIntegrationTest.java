package com.urlive.domain.infrastructure.log;

import com.urlive.config.AsyncSyncTestConfig;
import com.urlive.config.TestRedisConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({AsyncSyncTestConfig.class, TestRedisConfig.class})
public class LogServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private DateRangeAggregator aggregator;

    @Autowired
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
        String shortUrl = SHORTURL;

        System.out.println("rawReferer = " + request.getRemoteAddr());
        Log log = logService.createLog(request, shortUrl);
        System.out.println(log.getReferer());
        Assertions.assertThat(log.getReferer()).isEqualTo("naver");
        Assertions.assertThat(log.getDevice()).isEqualTo("iPhone");
    }

    @Test
    @DisplayName("시간별 유입량 확인 테스트")
    void 유입량_확인_테스트() {
        logRepository.save(new Log("shortUrl1", "unknown", "instagram", "231.180.227.51", "iPhone"));

        LocalDateTime startDate = LocalDateTime.of(2025, 07, 10, 00, 00);
        LocalDateTime endDate = LocalDateTime.of(2025, 07, 15, 00, 00);

        Assertions.assertThat(logService.getTrafficsByDateRange(0, startDate, endDate, "shortUrl1").size())
                .isEqualTo(24);
    }
}
