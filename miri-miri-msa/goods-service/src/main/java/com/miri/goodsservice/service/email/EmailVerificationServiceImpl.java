package com.miri.goodsservice.service.email;

import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.coremodule.handler.ex.EmailAlreadyExistsException;
import com.miri.goodsservice.domain.email.EmailVerificationCode;
import com.miri.goodsservice.domain.email.EmailVerificationCodeRepository;
import com.miri.goodsservice.domain.user.UserRepository;
import com.miri.goodsservice.util.AESUtils;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final UserRepository userRepository;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;
    private final JavaMailSender javaMailSender;
    private final AESUtils aesUtils;

    public EmailVerificationServiceImpl(UserRepository userRepository,
                                        EmailVerificationCodeRepository emailVerificationCodeRepository,
                                        JavaMailSender javaMailSender, AESUtils aesUtils) {
        this.userRepository = userRepository;
        this.emailVerificationCodeRepository = emailVerificationCodeRepository;
        this.javaMailSender = javaMailSender;
        this.aesUtils = aesUtils;
    }

    @Override
    public void sendVerificationEmail(String email) {
        validateEmailUniqueness(email);
        String verificationCode = createAndSaveEmailVerificationCode(email);
        sendEmail(email, verificationCode);
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }
    }

    private String createAndSaveEmailVerificationCode(String email) {
        String encryptedEmail = aesUtils.encodeUnique(email);
        String verificationCode = UUID.randomUUID().toString();
        EmailVerificationCode emailVerificationCode = EmailVerificationCode.builder()
                .verifyCode(verificationCode)
                .email(encryptedEmail)
                .build();
        emailVerificationCodeRepository.save(emailVerificationCode);

        return verificationCode;
    }

    private void sendEmail(String email, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[Miri-Miri] 이메일 인증 코드");
            message.setText("인증 코드: " + verificationCode);
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new CustomApiException("이메일 전송을 실패했습니다");
        }
    }
}
