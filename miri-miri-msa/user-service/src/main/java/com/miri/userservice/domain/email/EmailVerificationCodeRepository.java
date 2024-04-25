package com.miri.userservice.domain.email;

import org.springframework.data.repository.CrudRepository;

public interface EmailVerificationCodeRepository extends CrudRepository<EmailVerificationCode, String> {
}
