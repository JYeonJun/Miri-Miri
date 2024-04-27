package com.miri.userservice.config;

import com.miri.userservice.security.PrincipalDetailsService;
import com.miri.userservice.security.filter.JwtAuthenticationFilter;
import com.miri.userservice.util.CustomResponseUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PrincipalDetailsService principalDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;

    public SecurityConfig(PrincipalDetailsService principalDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder,
                          Environment env) {
        this.principalDetailsService = principalDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(principalDetailsService).passwordEncoder(bCryptPasswordEncoder);

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
                                                "hasIpAddress('127.0.0.1') or hasIpAddress('::1') or " +  "hasIpAddress('" + env.getProperty("api.gateway.ip") + "')"))
                                .anyRequest().authenticated()
                )
                .authenticationManager(authenticationManager)
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint((request, response, authException) ->
                                CustomResponseUtil.fail(response, "로그인이 필요합니다", HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, e) ->
                                CustomResponseUtil.fail(response, "권한이 없습니다.", HttpStatus.FORBIDDEN)))
                .formLogin((form) -> form.disable())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilter(new JwtAuthenticationFilter(authenticationManager, env));
        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()));
        return http.build();
    }
}