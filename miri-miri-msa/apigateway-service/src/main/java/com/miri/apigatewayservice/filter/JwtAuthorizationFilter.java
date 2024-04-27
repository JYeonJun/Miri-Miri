package com.miri.apigatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthorizationFilter extends AbstractGatewayFilterFactory<JwtAuthorizationFilter.Config> {
    private static final String USER_ID_CLAIM = "id";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String INVALID_TOKEN_RESPONSE = "The requested token is invalid.";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ROLES_CLAIM = "roles";
    private static final String ADMIN_PATH_PREFIX = "/api/admin/";

    private final SecretKey signingKey;
    private final JwtParser jwtParser;

    public JwtAuthorizationFilter(@Value("${token.secret}") String secret) {
        super(Config.class);
        this.signingKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
        this.jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build();
    }

    public static class Config {
        // Put configuration properties here
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
                return fail(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String jwt = authorizationHeader.substring(BEARER_PREFIX.length());
            Claims claims = parseJwt(jwt);

            if (!isValidClaims(claims)) {
                return fail(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            List<String> userRoles = extractRoles(claims);
            if (!isValidRoles(userRoles) || !isAuthorized(request.getURI().getPath(), userRoles)) {
                return fail(exchange, "Access Denied", HttpStatus.FORBIDDEN);
            }

            return addUserIdToHeaderAndFilter(exchange, chain, claims.get(USER_ID_CLAIM, Long.class));
        };
    }

    private boolean isValidClaims(Claims claims) {
        return claims != null && claims.getSubject() != null && !claims.getSubject().isEmpty();
    }

    private boolean isValidRoles(List<String> roles) {
        return roles != null;
    }

    private boolean isAuthorized(String path, List<String> userRoles) {
        return !path.startsWith(ADMIN_PATH_PREFIX) || userRoles.contains("ADMIN");
    }

    private Mono<Void> addUserIdToHeaderAndFilter(ServerWebExchange exchange, GatewayFilterChain chain, Long userId) {
        if (userId == null) {
            return fail(exchange, "Unable to extract user ID from JWT", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ServerHttpRequest updatedRequest = exchange.getRequest().mutate()
                .header(USER_ID_HEADER, String.valueOf(userId))
                .build();
        return chain.filter(exchange.mutate().request(updatedRequest).build());
    }

    private Mono<Void> fail(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        log.error(err);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        byte[] bytes = INVALID_TOKEN_RESPONSE.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
    }

    private Claims parseJwt(String jwt) {
        try {
            return jwtParser.parseClaimsJws(jwt).getBody();
        } catch (Exception ex) {
            log.error("JWT parsing error", ex);
            return null;
        }
    }

    private List<String> extractRoles(Claims claims) {
        Object rolesObj = claims.get(ROLES_CLAIM);
        if (rolesObj instanceof List) {
            return (List<String>) rolesObj;
        } else if (rolesObj instanceof String) {
            return Collections.singletonList((String) rolesObj);
        } else {
            log.error("Invalid roles information in JWT");
            return null;
        }
    }
}
