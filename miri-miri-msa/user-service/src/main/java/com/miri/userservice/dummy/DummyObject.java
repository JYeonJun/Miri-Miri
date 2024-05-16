package com.miri.userservice.dummy;

import com.miri.userservice.domain.user.User;
import com.miri.userservice.domain.user.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyObject {

    protected User newUser(String email, String userName,
                           String phoneNumber, String address) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        return User.builder()
                .email(email)
                .userName(userName)
                .password(encPassword)
                .phoneNumber(phoneNumber)
                .address(address)
                .role(UserRole.USER)
                .build();
    }
}
