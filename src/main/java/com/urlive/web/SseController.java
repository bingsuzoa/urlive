package com.urlive.web;


import com.urlive.domain.url.UrlService;
import com.urlive.service.SseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@RestController
public class SseController {

    public SseController(SseService sseService,
                         UrlService urlService
    ) {
        this.sseService = sseService;
        this.urlService = urlService;
    }

    private final SseService sseService;
    private final UrlService urlService;

    @GetMapping("/sse/{urlId}")
    public SseEmitter subscribe(@PathVariable Long urlId) {
        SseEmitter emitter = new SseEmitter(3600000L);

        sseService.addEmitter(urlId, emitter);

        try {
            Long initialViewCount = urlService.getCurrentViewCount(urlId);
            Map<String, Object> initialData = Map.of(
                    "urlId", urlId,
                    "viewCount", initialViewCount
            );
            emitter.send(SseEmitter.event()
                    .name("initialView")
                    .data(initialData));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }
}
