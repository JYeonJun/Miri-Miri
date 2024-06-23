package com.miri.userservice.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 초기 생성되는 쓰레드 수
        executor.setMaxPoolSize(20); // 최대 쓰레드 수
        executor.setQueueCapacity(500); // 큐 용량
        executor.setThreadNamePrefix("AsyncThread-"); // 쓰레드 이름 prefix
        executor.initialize(); // 초기화
        return executor;
    }
}