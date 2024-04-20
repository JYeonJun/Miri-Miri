package com.miri.userservice.controller;

import static com.miri.userservice.dto.RequestUserDto.*;
import static com.miri.userservice.dto.RequestUserDto.SignUpReqDto;
import static com.miri.userservice.dto.RequestUserDto.UpdateUserProfileReqDto;

import com.miri.userservice.dto.RequestUserDto;
import com.miri.userservice.dto.ResponseDto;
import com.miri.userservice.dto.ResponseUserDto.GetUserRespDto;
import com.miri.userservice.dto.ResponseUserDto.UpdateUserProfileRespDto;
import com.miri.userservice.security.PrincipalDetails;
import com.miri.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpReqDto signUpReqDto) {

        userService.createUser(signUpReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입에 성공했습니다", null), HttpStatus.CREATED);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        return new ResponseEntity<>(new ResponseDto<>(1, "로그아웃 되었습니다", null), HttpStatus.OK);
    }

    @PatchMapping("/auth/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UpdateUserProfileReqDto updateUserProfileReqDto,
                                        BindingResult bindingResult,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        UpdateUserProfileRespDto result
                = userService.updateUserProfile(principalDetails.getUser().getId(), updateUserProfileReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "사용자 정보 변경이 완료되었습니다", result), HttpStatus.OK);
    }

    @PatchMapping("/auth/users/change-password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdateUserPasswordReqDto updateUserPasswordReqDto,
                                            BindingResult bindingResult,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.updateUserPassword(principalDetails.getUser().getId(), updateUserPasswordReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "비밀번호 변경이 완료되었습니다", null), HttpStatus.OK);
    }

    @GetMapping("/auth/users")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        GetUserRespDto result
                = userService.getUserProfile(principalDetails.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "사용자 정보 변경이 완료되었습니다", result), HttpStatus.OK);
    }
}
