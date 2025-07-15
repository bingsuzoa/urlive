package com.urlive.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

@TestConfiguration
public class AsyncSyncTestConfig {

    @Bean
    @Primary
    public AsyncTaskExecutor taskExecutor() {
        return new ConcurrentTaskExecutor(new SyncTaskExecutor());
    }
}