package com.miri.userservice.service.user;

import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.userservice.domain.email.EmailVerificationCode;
import com.miri.userservice.domain.email.EmailVerificationCodeRepository;
import com.miri.userservice.domain.user.User;
import com.miri.userservice.domain.user.UserRepository;
import com.miri.userservice.domain.user.UserRole;
import com.miri.userservice.dto.goods.ResponseGoodsDto.RegisterGoodsListRespDto;
import com.miri.userservice.dto.order.ResponseOrderDto.OrderGoodsListRespDto;
import com.miri.userservice.dto.user.RequestUserDto.SignUpReqDto;
import com.miri.userservice.dto.user.RequestUserDto.UpdateUserPasswordReqDto;
import com.miri.userservice.dto.user.RequestUserDto.UpdateUserProfileReqDto;
import com.miri.userservice.dto.user.ResponseUserDto.GetUserRespDto;
import com.miri.userservice.dto.user.ResponseUserDto.UpdateUserProfileRespDto;
import com.miri.userservice.dto.wishlist.ResponseWishListDto.WishListRespDto;
import com.miri.userservice.service.goods.GoodsService;
import com.miri.userservice.service.order.OrderService;
import com.miri.userservice.service.wishlist.WishListService;
import com.miri.userservice.util.AESUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GoodsService goodsService;
    private final WishListService wishListService;
    private final OrderService orderService;
    private final EmailVerificationCodeRepository emailVerificationCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AESUtils aesUtils;

    public UserServiceImpl(UserRepository userRepository, GoodsService goodsService, WishListService wishListService,
                           OrderService orderService, EmailVerificationCodeRepository emailVerificationCodeRepository,
                           BCryptPasswordEncoder passwordEncoder, AESUtils aesUtils) {
        this.userRepository = userRepository;
        this.goodsService = goodsService;
        this.wishListService = wishListService;
        this.orderService = orderService;
        this.emailVerificationCodeRepository = emailVerificationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.aesUtils = aesUtils;
    }

    @Override
    @Transactional
    public void createUser(SignUpReqDto signUpReqDto) {
        String userEmail = verifyEmailAndGetUserEmail(signUpReqDto.getEmailVerificationToken());
        createUserInRepository(signUpReqDto, userEmail);
    }

    private String verifyEmailAndGetUserEmail(String emailVerificationToken) {
        EmailVerificationCode emailVerificationCode = emailVerificationCodeRepository.findById(emailVerificationToken)
                .orElseThrow(() -> new CustomApiException("인증되지 않은 이메일입니다."));
        String email = emailVerificationCode.getEmail();
        emailVerificationCodeRepository.delete(emailVerificationCode);
        return aesUtils.decodeUnique(email);
    }

    private void createUserInRepository(SignUpReqDto signUpReqDto, String userEmail) {
        userRepository.save(User.builder()
                .email(userEmail)
                .userName(signUpReqDto.getUserName())
                .password(passwordEncoder.encode(signUpReqDto.getPassword()))
                .phoneNumber(signUpReqDto.getPhoneNumber())
                .address(signUpReqDto.getAddress())
                .role(UserRole.USER)
                .build());
    }

    @Override
    @Transactional
    public UpdateUserProfileRespDto updateUserProfile(Long userId, UpdateUserProfileReqDto userProfile) {

        User findUser = findUserByIdOrThrow(userId);
        findUser.changeUserProfile(userProfile.getPhoneNumber(), userProfile.getAddress());

        return new UpdateUserProfileRespDto(findUser);
    }

    @Override
    @Transactional
    public void updateUserPassword(Long userId, UpdateUserPasswordReqDto userPassword) {
        User findUser = findUserByIdOrThrow(userId);
        findUser.changePassword(passwordEncoder.encode(userPassword.getPassword()));
    }

    @Override
    public GetUserRespDto getUserInfo(Long userId) {
        User findUser = findUserByIdOrThrow(userId);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("desc"), "createdDate"));
        // 등록한 상품 목록(GoodsService)
        RegisterGoodsListRespDto registerGoodsList = goodsService.findRegisterGoodsList(userId, pageRequest);
        // 장바구니에 추가한 상품 목록(WishListService)
        WishListRespDto wishListGoods = wishListService.getWishListGoods(userId, pageRequest);
        // 주문한 상품 목록(OrderService)
        OrderGoodsListRespDto orderGoodsList = orderService.getOrderGoodsList(userId, pageRequest);

        return new GetUserRespDto(findUser, registerGoodsList, wishListGoods, orderGoodsList);
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("해당 사용자가 존재하지 않습니다."));
    }
}
