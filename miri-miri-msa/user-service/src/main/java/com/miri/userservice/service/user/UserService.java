package com.miri.userservice.service.user;

import com.miri.userservice.dto.user.RequestUserDto.SignUpReqDto;
import com.miri.userservice.dto.user.RequestUserDto.UpdateUserPasswordReqDto;
import com.miri.userservice.dto.user.RequestUserDto.UpdateUserProfileReqDto;
import com.miri.userservice.dto.user.ResponseUserDto.GetUserRespDto;
import com.miri.userservice.dto.user.ResponseUserDto.UpdateUserProfileRespDto;

public interface UserService {

    void createUser(SignUpReqDto signUpReqDto); // 회원가입

    UpdateUserProfileRespDto updateUserProfile(Long userId, UpdateUserProfileReqDto userProfile);

    void updateUserPassword(Long userId, UpdateUserPasswordReqDto userPassword);

    GetUserRespDto getUserInfo(Long userId);
}
