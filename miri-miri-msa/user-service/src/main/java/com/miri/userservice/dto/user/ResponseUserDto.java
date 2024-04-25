package com.miri.userservice.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miri.userservice.domain.user.User;
import com.miri.userservice.dto.goods.ResponseGoodsDto.RegisterGoodsListRespDto;
import com.miri.userservice.dto.order.ResponseOrderDto.OrderGoodsListRespDto;
import com.miri.userservice.dto.wishlist.ResponseWishListDto.WishListRespDto;
import java.time.LocalDateTime;
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
        private Long userId;
        private String email;
        private String userName;
        private String phoneNumber;
        private String address;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime signupDate;

        private RegisterGoodsListRespDto registerGoodsList;
        private WishListRespDto wishList;
        private OrderGoodsListRespDto orders;

        public GetUserRespDto(User user, RegisterGoodsListRespDto registerGoodsList,
                              WishListRespDto wishList, OrderGoodsListRespDto orders) {
            this.userId = user.getId();
            this.email = user.getEmail();
            this.userName = user.getUserName();
            this.phoneNumber = user.getPhoneNumber();
            this.address = user.getAddress();
            this.signupDate = user.getCreatedDate();
            this.registerGoodsList = registerGoodsList;
            this.wishList = wishList;
            this.orders = orders;
        }
    }
}
