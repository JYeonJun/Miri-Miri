package com.miri.userservice.domain;

import org.springframework.data.repository.CrudRepository;

public interface EmailVerificationCodeRepository extends CrudRepository<EmailVerificationCode, String> {
}
