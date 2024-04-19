package com.miri.userservice.service;

import com.miri.userservice.dto.RequestUser.SignUpReqDto;

public interface UserService {

    void createUser(SignUpReqDto signUpReqDto); // 회원가입
}
