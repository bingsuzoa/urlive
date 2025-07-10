package com.urlive.web;

import com.urlive.domain.infrastructure.LogService;
import com.urlive.global.responseFormat.ApiResponse;
import com.urlive.global.responseFormat.ApiResponseBuilder;
import com.urlive.global.responseFormat.ResponseMessage;
import com.urlive.service.UrliveFacade;
import com.urlive.web.dto.log.TrafficByDateRange;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class LogController {

    public LogController(
            UrliveFacade urliveFacade,
            LogService logService,
            ApiResponseBuilder apiResponseBuilder
    ) {
        this.urliveFacade = urliveFacade;
        this.logService = logService;
        this.apiResponseBuilder = apiResponseBuilder;
    }

    private final UrliveFacade urliveFacade;
    private final LogService logService;
    private final ApiResponseBuilder apiResponseBuilder;

    @GetMapping("/{short-url}")
    public ResponseEntity<ApiResponse<Void>> redirectToOriginalUrl(
            @PathVariable(name = "short-url") String shortUrl,
            HttpServletRequest request
    ) {
        String originalUrl = urliveFacade.decodeShortUrl(shortUrl);
        logService.createLog(request, shortUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));

        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }

    @GetMapping("/user-url/{shortUrl}")
    public ResponseEntity<ApiResponse<List<TrafficByDateRange>>> getTrafficByDateRange(
            @PathVariable(name = "shortUrl") String shortUrl,
            @RequestParam("days") int days
    ) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        return apiResponseBuilder.ok(ResponseMessage.LOG_TRAFFIC_DATE_RANGE_SUCCESS,
                logService.getTrafficsByDateRange(start, end, shortUrl));
    }
}
