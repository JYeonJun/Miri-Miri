package com.miri.userservice.dto;

import com.miri.userservice.domain.user.User;
import lombok.Data;

public class ResponseUserDto {

    @Data
    public static class UpdateUserProfileRespDto {
        private String address;
        private String phoneNumber;

        public UpdateUserProfileRespDto(User user) {
            this.address = user.getAddress();
            this.phoneNumber = user.getPhoneNumber();
        }
    }

    @Data
    public static class GetUserRespDto {
        private String email;
        private String userName;
        private String phoneNumber;
        private String address;

        public GetUserRespDto(User user) {
            this.email = user.getEmail();
            this.userName = user.getUserName();
            this.phoneNumber = user.getPhoneNumber();
            this.address = user.getAddress();
        }
    }
}
