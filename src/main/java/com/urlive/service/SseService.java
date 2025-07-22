package com.urlive.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private static final String SSE_EMITTER_ERROR = "서버와 클라이언트 간 연결에 에러가 발생했습니다.";

    public void addEmitter(Long urlId, SseEmitter emitter) {
        this.emitters.computeIfAbsent(urlId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(urlId, emitter));

        emitter.onTimeout(() -> {
            emitter.complete();
            removeEmitter(urlId, emitter);
        });

        emitter.onError(e -> {
            System.err.println(SSE_EMITTER_ERROR + urlId + ": " + e.getMessage());
            removeEmitter(urlId, emitter);
        });
    }

    public void removeEmitter(Long urlId, SseEmitter emitter) {
        List<SseEmitter> urlEmitters = emitters.get(urlId);
        if (urlEmitters != null) {
            urlEmitters.remove(emitter);
            if (urlEmitters.isEmpty()) {
                emitters.remove(urlId);
            }
        }
    }

    public void sendViewUpdate(Long urlId, Long newViewCount) {
        List<SseEmitter> urlEmitters = emitters.get(urlId);
        System.out.println("sendViewUpdate called for urlId: " + urlId + ", newViewCount: " + newViewCount + ", active emitters: " + (urlEmitters != null ? urlEmitters.size() : 0));
        if (urlEmitters != null && !urlEmitters.isEmpty()) {
            Map<String, Object> eventData = Map.of(
                    "urlId", urlId,
                    "viewCount", newViewCount
            );

            urlEmitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                            .id(String.valueOf(System.currentTimeMillis()))
                            .name("viewUpdate")
                            .data(eventData));
                    System.out.println("Successfully sent SSE to emitter for urlId: " + urlId); // 로그 추가
                } catch (IOException e) {
                    System.err.println("Failed to send SSE to emitter for urlId: " + urlId + ", Error: " + e.getMessage()); // 로그 추가
                    emitter.completeWithError(e);
                }
            });
        } else {
            System.out.println("No active SSE emitters found for urlId: " + urlId + " to send update."); // 로그 추가
        }
    }
}
