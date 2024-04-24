package com.miri.userservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.miri.userservice.dto.wishlist.RequestWishListDto.AddToCartReqDto;
import com.miri.userservice.dto.wishlist.RequestWishListDto.WishListUpdateReqDto;
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
class WishListApiControllerTest extends DummyObject {

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
    @DisplayName("위시리스트 추가 기능 테스트")
    void addToWishList() throws Exception {
        // given
        AddToCartReqDto reqDto = new AddToCartReqDto();
        reqDto.setGoodsId(3L);
        reqDto.setGoodsQuantity(10);
        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions =
                mvc.perform(post("/api/auth/wishlist")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("장바구니에 상품이 추가되었습니다."))
                .andExpect(jsonPath("$.data.wishListId").value(2))
                .andExpect(jsonPath("$.data.goodsQuantity").value(10))
                .andExpect(jsonPath("$.data.unitPrice").value(59000))
                .andExpect(jsonPath("$.data.subTotalPrice").value(590000))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("위시리스트 추가 기능 예외 테스트")
    void addToWishListException() throws Exception {
        // given
        AddToCartReqDto reqDto = new AddToCartReqDto();
        reqDto.setGoodsId(4L);
        reqDto.setGoodsQuantity(10);
        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions =
                mvc.perform(post("/api/auth/wishlist")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("장바구니에 동일한 상품이 존재합니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("위시리스트 상품 목록 조회 기능 테스트")
    void getWishListGoods() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/wishlist")
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("장바구니 목록 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.goods.content.size()").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateGoodsQuantityInWishList() throws Exception {
        // given
        WishListUpdateReqDto reqDto = new WishListUpdateReqDto();
        reqDto.setGoodsQuantity(11);
        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions =
                mvc.perform(patch("/api/auth/wishlist/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("장바구니 상품 수량 변경에 성공하였습니다."))
                .andExpect(jsonPath("$.data.wishListId").value(1))
                .andExpect(jsonPath("$.data.goodsId").value(4))
                .andExpect(jsonPath("$.data.wishGoodsQuantity").value(11))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteGoodsInWishList() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mvc.perform(delete("/api/auth/wishlist/1")
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("장바구니 상품 삭제에 성공하였습니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
        User user1 = userRepository.save(newUser("test@naver.com", "홍길동", "010-1111-1111", "서울특별시 종로구"));
        User user2 = userRepository.save(newUser("test22@google.com", "홍길동", "010-1234-1234", "경상북도 경산시"));
        goodsRepository.save(newGoods(user1.getId(), "test 목걸이", 11111, 100, GoodsCategory.FASHION));
        goodsRepository.save(newGoods(user1.getId(), "test 티셔츠", 10000, 10, GoodsCategory.FASHION));
        goodsRepository.save(newGoods(user2.getId(), "test 치마", 59000, 50, GoodsCategory.FASHION));
        Goods goods = goodsRepository.save(newGoods(user2.getId(), "test 운동화", 127000, 150, GoodsCategory.FASHION));
        wishListRepository.save(newWishList(user1.getId(), goods, 10));
        em.clear();
    }
}