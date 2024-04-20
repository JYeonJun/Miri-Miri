package com.miri.userservice.service;

import com.miri.userservice.dto.RequestUserDto.SignUpReqDto;
import com.miri.userservice.dto.RequestUserDto.UpdateUserPasswordReqDto;
import com.miri.userservice.dto.RequestUserDto.UpdateUserProfileReqDto;
import com.miri.userservice.dto.ResponseUserDto.GetUserRespDto;
import com.miri.userservice.dto.ResponseUserDto.UpdateUserProfileRespDto;

public interface UserService {

    void createUser(SignUpReqDto signUpReqDto); // 회원가입

    UpdateUserProfileRespDto updateUserProfile(Long userId, UpdateUserProfileReqDto userProfile);

    void updateUserPassword(Long userId, UpdateUserPasswordReqDto userPassword);

    GetUserRespDto getUserProfile(Long userId);
}
