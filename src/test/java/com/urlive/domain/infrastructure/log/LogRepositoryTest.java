package com.urlive.domain.infrastructure.log;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class LogRepositoryTest {

    @Autowired
    private LogRepository logRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void insert() {
        Log log = new Log("shortUrl1", "unknown", "instagram", "231.180.227.51", "iPhone");
        log.setCreatedAtForTest(LocalDateTime.parse("2025-07-12 02:10", formatter));
        logRepository.save(log);

        Log log2 = new Log("shortUrl1", "unknown", "kakao", "231.180.227.51", "Mac");
        log2.setCreatedAtForTest(LocalDateTime.parse("2025-07-12 16:10", formatter));
        logRepository.save(log2);

        Log log3 = new Log("shortUrl1", "unknown", "google", "231.180.227.51", "iPhone");
        log3.setCreatedAtForTest(LocalDateTime.parse("2025-07-01 16:10", formatter));
        logRepository.save(log3);
    }

    @AfterEach
    void deleteAll() {
        logRepository.deleteAll();
    }

    @Test
    @DisplayName("설정된 기간의 로그 얻어 오기")
    void 설정된_기간의_로그_얻기() {
        LocalDateTime end = LocalDateTime.parse("2025-07-12 23:59", formatter);
        LocalDateTime start = LocalDateTime.parse("2025-07-05 00:00", formatter);
        List<Object[]> result = logRepository.findLogsByDateRange("shortUrl1", start, end);

        LocalDate date = ((java.sql.Date) result.getFirst()[0]).toLocalDate();
        Assertions.assertThat(date.toString()).isEqualTo("2025-07-12");
        Assertions.assertThat((long) result.getFirst()[1]).isEqualTo(2L);
    }

    @Test
    @DisplayName("설정된 기간의 유입경로별 로그 얻어 오기")
    void 설정된_기간의_유입경로별_로그_얻기() {
        LocalDateTime end = LocalDateTime.parse("2025-07-12 23:59", formatter);
        LocalDateTime start = LocalDateTime.parse("2025-07-05 00:00", formatter);
        List<Object[]> result = logRepository.findLogsByReferer("shortUrl1", start, end);

        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("설정된 기간의 디바이스별 로그 얻어 오기")
    void 설정된_기간의_디바이스_로그_얻기() {
        LocalDateTime end = LocalDateTime.parse("2025-07-12 23:59", formatter);
        LocalDateTime start = LocalDateTime.parse("2025-07-05 00:00", formatter);
        List<Object[]> result = logRepository.findLogsByDevice("shortUrl1", start, end);

        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("시간별 유입량 얻기")
    void 시간별_유입량_얻기() {
        String start = "2025-07-12 00:00";
        String end = "2025-07-12 23:59";
        List<Object[]> result = logRepository.findLogsPerTime("shortUrl1", start, end);
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("시간별 유입경로별 유입량 얻기")
    void 시간별_유입경로별_얻기() {
        String start = "2025-07-01 00:00";
        String end = "2025-07-01 23:59";
        List<Object[]> result = logRepository.findLogsByRefererPerTime("shortUrl1", start, end);

        Object[] first = result.getFirst();
        String referer = (String) first[1];
        Assertions.assertThat(referer).isEqualTo("google");
    }

    @Test
    @DisplayName("시간별 디바이스별 유입량 얻기")
    void 시간별_디바이스별_얻기() {
        String start = "2025-07-01 00:00";
        String end = "2025-07-01 23:59";
        List<Object[]> result = logRepository.findLogsByDevicePerTime("shortUrl1", start, end);

        Object[] first = result.getFirst();
        String device = (String) first[1];
        Assertions.assertThat(device).isEqualTo("iPhone");
    }
}
