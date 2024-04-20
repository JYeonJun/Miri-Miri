package com.miri.userservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miri.userservice.domain.user.User;
import com.miri.userservice.security.PrincipalDetails;
import com.miri.userservice.util.CustomResponseUtil;
import com.miri.userservice.dto.user.RequestUserDto.LoginReqDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Environment environment;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   Environment environment) {
        super(authenticationManager);
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {

            LoginReqDto creds = new ObjectMapper().readValue(req.getInputStream(), LoginReqDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        CustomResponseUtil.fail(response, "로그인 실패", HttpStatus.BAD_REQUEST);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) {

        PrincipalDetails principalDetails = (PrincipalDetails) auth.getPrincipal();

        byte[] secretKeyBytes = Base64.getEncoder().encode(environment.getProperty("token.secret").getBytes());

        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();

        User user = principalDetails.getUser();
        String token = Jwts.builder()
                .setSubject(principalDetails.getUsername())
                .claim("id", user.getId())
                .claim("roles", user.getRole().toString())
                .setExpiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
                .setIssuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();

        res.addHeader("token", token);
        res.addHeader("userId", String.valueOf(principalDetails.getUser().getId()));
    }
}
