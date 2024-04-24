package com.miri.userservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miri.userservice.domain.goods.Goods;
import com.miri.userservice.domain.goods.GoodsCategory;
import com.miri.userservice.domain.goods.GoodsRepository;
import com.miri.userservice.domain.user.User;
import com.miri.userservice.domain.user.UserRepository;
import com.miri.userservice.domain.wishlist.WishListRepository;
import com.miri.userservice.dto.user.RequestUserDto.UpdateUserPasswordReqDto;
import com.miri.userservice.dto.user.RequestUserDto.UpdateUserProfileReqDto;
import com.miri.userservice.dummy.DummyObject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
@WithUserDetails(value = "test@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class UserApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    @DisplayName("로그아웃 기능 테스트")
    void logout() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mvc.perform(post("/api/auth/logout").contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("로그아웃 되었습니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("전화번호 및 주소 변경 기능 테스트")
    void updateUser() throws Exception {
        // given
        UpdateUserProfileReqDto reqDto = new UpdateUserProfileReqDto();
        reqDto.setPhoneNumber("010-1234-1234");
        reqDto.setAddress("경상북도 울릉군 울릉읍 울릉경비대");
        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions =
                mvc.perform(patch("/api/auth/users")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("사용자 정보 변경이 완료되었습니다."))
                .andExpect(jsonPath("$.data.address").value("경상북도 울릉군 울릉읍 울릉경비대"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-1234-1234"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("패스워드 변경 기능 테스트")
    void updatePassword() throws Exception {
        // given
        UpdateUserPasswordReqDto reqDto = new UpdateUserPasswordReqDto();
        reqDto.setPassword("testPassword1234!!");
        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions =
                mvc.perform(patch("/api/auth/users/change-password")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("비밀번호 변경이 완료되었습니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("사용자 정보 조회 기능 테스트")
    void getUserInfo() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/users")
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("사용자 정보가 조회되었습니다."))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                .andExpect(jsonPath("$.data.userName").value("홍길동"))
                .andExpect(jsonPath("$.data.registerGoodsList.registerGoods.content.size()").value(2))
                .andExpect(jsonPath("$.data.wishList.goods.content.size()").value(1))
                .andExpect(jsonPath("$.data.orders.orders.content.size()").value(0))
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {

        User user1 = userRepository.save(newUser("test@naver.com", "홍길동", "010-1111-1111", "서울특별시 종로구"));
        User user2 = userRepository.save(newUser("test22@google.com", "홍길동", "010-1234-1234", "경상북도 경산시"));
        goodsRepository.save(newGoods(user1.getId(), "test 목걸이", 11111, 100, GoodsCategory.FASHION));
        goodsRepository.save(newGoods(user1.getId(), "test 티셔츠", 10000, 10, GoodsCategory.FASHION));
        Goods goods = goodsRepository.save(newGoods(user2.getId(), "test 치마", 59000, 50, GoodsCategory.FASHION));
        wishListRepository.save(newWishList(user1.getId(), goods, 5));
        em.clear();
    }
}