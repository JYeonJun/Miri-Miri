package com.miri.userservice.service;

import com.miri.userservice.domain.EmailVerificationCode;
import com.miri.userservice.domain.EmailVerificationCodeRepository;
import com.miri.userservice.domain.user.User;
import com.miri.userservice.domain.user.UserRepository;
import com.miri.userservice.domain.user.UserRole;
import com.miri.userservice.dto.RequestUser.SignUpReqDto;
import com.miri.userservice.handler.ex.CustomApiException;
import com.miri.userservice.util.AESUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AESUtils aesUtils;

    public UserServiceImpl(UserRepository userRepository,
                           EmailVerificationCodeRepository emailVerificationCodeRepository,
                           BCryptPasswordEncoder passwordEncoder, AESUtils aesUtils) {
        this.userRepository = userRepository;
        this.emailVerificationCodeRepository = emailVerificationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.aesUtils = aesUtils;
    }

    @Override
    public void createUser(SignUpReqDto signUpReqDto) {
        String userEmail = verifyEmailAndGetUserEmail(signUpReqDto.getEmailVerificationToken());
        createUserInRepository(signUpReqDto, userEmail);
    }

    private String verifyEmailAndGetUserEmail(String emailVerificationToken) {
        EmailVerificationCode emailVerificationCode = emailVerificationCodeRepository.findById(emailVerificationToken)
                .orElseThrow(() -> new CustomApiException("인증되지 않은 이메일입니다."));
        String email = emailVerificationCode.getEmail();
        emailVerificationCodeRepository.delete(emailVerificationCode);
        return aesUtils.decodeUnique(email);
    }

    private void createUserInRepository(SignUpReqDto signUpReqDto, String userEmail) {
        userRepository.save(User.builder()
                .email(userEmail)
                .userName(signUpReqDto.getUserName())
                .password(passwordEncoder.encode(signUpReqDto.getPassword()))
                .phoneNumber(signUpReqDto.getPhoneNumber())
                .address(signUpReqDto.getAddress())
                .role(UserRole.USER)
                .build());
    }
}
