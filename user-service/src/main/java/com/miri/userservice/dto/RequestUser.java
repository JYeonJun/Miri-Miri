package com.miri.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

public class RequestUser {

    @Data
    public static class VerifyEmailDto {
        @NotEmpty
        @Length(min = 2, max = 30)
        @Email(message = "잘못된 이메일 형식입니다.")
        private String email;
    }
}
