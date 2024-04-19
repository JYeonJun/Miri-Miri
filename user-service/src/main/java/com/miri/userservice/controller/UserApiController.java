package com.miri.userservice.controller;

import static com.miri.userservice.dto.RequestUser.*;

import com.miri.userservice.service.UserService;
import com.miri.userservice.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        return new ResponseEntity<>(new ResponseDto<>(1, "로그아웃 되었습니다", null), HttpStatus.CREATED);
    }
}
