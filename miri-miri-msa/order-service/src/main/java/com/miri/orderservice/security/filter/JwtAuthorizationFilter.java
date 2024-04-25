package com.miri.orderservice.security.filter;

import static com.miri.orderservice.util.CustomResponseUtil.fail;

import com.miri.orderservice.domain.user.User;
import com.miri.orderservice.domain.user.UserRole;
import com.miri.orderservice.security.PrincipalDetails;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final Environment env;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, Environment env) {
        super(authenticationManager);
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (authorizationHeader != null) {
                String jwt = authorizationHeader.replace("Bearer ", "");
                setAuthenticationFromAccessToken(jwt);
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            fail(response, "유효하지 않은 토큰입니다.", HttpStatus.FORBIDDEN);
            return;
        }
    }

    private void setAuthenticationFromAccessToken(String jwt) {
        PrincipalDetails principalDetails = verify(jwt);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails, null, principalDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private PrincipalDetails verify(String jwt) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();

        Long userId = jwtParser.parseClaimsJws(jwt).getBody().get("id", Long.class);
        String userRole = jwtParser.parseClaimsJws(jwt).getBody().get("roles", String.class);
        User user = User.builder().id(userId).role(UserRole.valueOf(userRole)).build();
        return new PrincipalDetails(user);
    }
}
