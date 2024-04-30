package com.miri.userservice.controller;

import com.miri.userservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal")
@Slf4j
public class UserClientController {

    private final UserService userService;

    public UserClientController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{userId}/name")
    public ResponseEntity<String> getUserNameById(@PathVariable Long userId) {
        String userName = userService.findUserNameById(userId);
        return ResponseEntity.ok(userName);
    }
}
