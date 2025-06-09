package com.urlive.domain.view;

import com.urlive.service.ViewService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ViewCountScheduler {

    public ViewCountScheduler(ViewService viewService) {
        this.viewService = viewService;
    }

    private final ViewService viewService;

    @Scheduled(fixedRate = 300_000)
    public void run() {
        viewService.flushViewCountToDB();
    }
}
