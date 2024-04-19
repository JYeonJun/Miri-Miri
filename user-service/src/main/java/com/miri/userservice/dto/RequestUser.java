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

    @Data
    public static class SignUpReqDto {

        @NotEmpty
        @Length(min = 36, max = 36, message = "이메일 인증 토큰이 부정확합니다.")
        private String emailVerificationToken;

        @NotEmpty
        @Pattern(regexp = "^[가-힣]{1,10}$", message = "한글 1~10자 이내로 작성해주세요")
        private String userName;

        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>/?]{10,20}$", message = "영문/숫자/특수 기호 10~20자 이내로 작성해주세요")
        private String password;

        @NotEmpty
        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식이 유효하지 않습니다. 예: 010-1234-5678")
        private String phoneNumber;

        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s-_.]{1,100}$", message = "주소 형식이 올바르지 않습니다.")
        private String address;
    }
}
