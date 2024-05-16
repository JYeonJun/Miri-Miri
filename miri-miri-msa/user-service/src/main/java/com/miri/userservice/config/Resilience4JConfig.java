package com.miri.userservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Resilience4JConfig {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 실패율 임계값 (50%)
                .waitDurationInOpenState(Duration.ofMillis(300000)) // 개방 상태 지속 시간 (5분)
                .automaticTransitionFromOpenToHalfOpenEnabled(true) // 자동 전환 활성화
                .slidingWindowType(SlidingWindowType.COUNT_BASED) // 슬라이딩 윈도우 유형 (카운트 기반, 시간 기반) - 호출 횟수에 기반
                .recordExceptions(IOException.class, TimeoutException.class) // 특정 예외 기록
                .permittedNumberOfCallsInHalfOpenState(5) // 반 열린 상태에서 허용되는 호출 수
                .maxWaitDurationInHalfOpenState(Duration.ofMillis(5000)) // 반 열린 상태 최대 대기 시간
                .slidingWindowSize(50) // 슬라이딩 윈도우 크기
                .minimumNumberOfCalls(20) // 최소 호출 수
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(5)) // 타임아웃 기간
                .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }
}
