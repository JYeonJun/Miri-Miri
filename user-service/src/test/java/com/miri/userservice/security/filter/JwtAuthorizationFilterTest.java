package com.miri.userservice.security.filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.miri.userservice.domain.user.User;
import com.miri.userservice.domain.user.UserRole;
import com.miri.userservice.security.PrincipalDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Environment environment;

    @Test
    @DisplayName("AuthorizationFilter 성공 테스트")
    public void authorization_success_test() throws Exception {

        // given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .userName("홍길동")
                .password(encPassword)
                .phoneNumber("010-1111-1111")
                .address("서울특별시")
                .role(UserRole.USER)
                .build();

        String token = createToken(user);
        System.out.println("token = " + token);

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/hello/test").header(HttpHeaders.AUTHORIZATION, token));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void authorization_fail_test() throws Exception {

        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/hello/test"));

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void authorization_admin_test() throws Exception {

        // given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .userName("홍길동")
                .password(encPassword)
                .phoneNumber("010-1111-1111")
                .address("서울특별시")
                .role(UserRole.USER)
                .build();

        String token = createToken(user);
        System.out.println("token = " + token);

        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/hello/test").header(HttpHeaders.AUTHORIZATION, token));

        // then
        resultActions.andExpect(status().isForbidden());
    }

    private String createToken(User user) {
        PrincipalDetails loginUser = new PrincipalDetails(user);

        byte[] secretKeyBytes = Base64.getEncoder().encode(environment.getProperty("token.secret").getBytes());

        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();

        String token = Jwts.builder()
                .setSubject(loginUser.getUsername())
                .claim("id", user.getId())
                .claim("roles", user.getRole().toString())
                .setExpiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
                .setIssuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();
        return token;
    }
}