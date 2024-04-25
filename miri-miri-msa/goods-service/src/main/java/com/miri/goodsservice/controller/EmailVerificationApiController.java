package com.miri.goodsservice.controller;


import com.miri.coremodule.dto.ResponseDto;
import com.miri.goodsservice.dto.user.RequestUserDto.VerifyEmailDto;
import com.miri.goodsservice.service.email.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class EmailVerificationApiController {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationApiController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid VerifyEmailDto verifyEmailDto,
                                         BindingResult bindingResult) {

        emailVerificationService.sendVerificationEmail(verifyEmailDto.getEmail());
        return new ResponseEntity<>(new ResponseDto<>(1, "이메일을 확인해주세요.", null), HttpStatus.OK);
    }
}
