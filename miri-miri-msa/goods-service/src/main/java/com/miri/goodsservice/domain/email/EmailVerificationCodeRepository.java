package com.miri.goodsservice.domain.email;

import org.springframework.data.repository.CrudRepository;

public interface EmailVerificationCodeRepository extends CrudRepository<EmailVerificationCode, String> {
}
