package com.miri.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ResponseUserDto {

    @AllArgsConstructor
    @Data
    public static class UpdateUserProfileRespDto {
        private String address;
        private String phoneNumber;
    }
}
