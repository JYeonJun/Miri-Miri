package com.miri.userservice.domain.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "emailVerificationCode", timeToLive = 600)
public class EmailVerificationCode {

    @Id
    @Indexed
    private String verifyCode;

    private String email;
}
