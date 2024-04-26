package com.miri.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests((authz) -> authz
//                        .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).authenticated()
//                        .requestMatchers(new AntPathRequestMatcher("/api/admin/**")).hasRole("" + UserRole.ADMIN)
//                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/**").access(
                                        new WebExpressionAuthorizationManager(
                                                // 접근 제어를 위한 IP 기반의 조건
                                                "hasIpAddress('127.0.0.1') or hasIpAddress('::1')"))
                                .anyRequest().authenticated()
                )
                .authenticationManager(authenticationManager)
                .formLogin((form) -> form.disable())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()));
        return http.build();
    }
}