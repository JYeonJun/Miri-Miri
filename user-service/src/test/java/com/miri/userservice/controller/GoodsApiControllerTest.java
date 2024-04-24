package com.miri.userservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miri.userservice.domain.goods.GoodsCategory;
import com.miri.userservice.domain.goods.GoodsRepository;
import com.miri.userservice.domain.user.User;
import com.miri.userservice.domain.user.UserRepository;
import com.miri.userservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
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
class GoodsApiControllerTest extends DummyObject {

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

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    @DisplayName("상품 등록 기능 테스트")
    void registerGoods() throws Exception {
        // given
        GoodsRegistrationReqDto reqDto = new GoodsRegistrationReqDto();
        String goodsName = "Miri-Miri 티셔츠";
        reqDto.setGoodsName(goodsName);
        String goodsDescription = "Miri-Miri 7주년 한정판 티셔츠입니다.";
        reqDto.setGoodsDescription(goodsDescription);
        int goodsPrice = 100000;
        reqDto.setGoodsPrice(goodsPrice);
        int stockQuantity = 100;
        reqDto.setStockQuantity(stockQuantity);
        reqDto.setCategory(GoodsCategory.FASHION);
        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions =
                mvc.perform(post("/api/auth/goods")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("상품 등록에 성공했습니다."))
                .andExpect(jsonPath("$.data.sellerId").value(1))
                .andExpect(jsonPath("$.data.goodsName").value(goodsName))
                .andExpect(jsonPath("$.data.goodsDescription").value(goodsDescription))
                .andExpect(jsonPath("$.data.goodsPrice").value(goodsPrice))
                .andExpect(jsonPath("$.data.stockQuantity").value(stockQuantity))
                .andExpect(jsonPath("$.data.category").value("패션"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("상품 목록 조회 기능 테스트")
    void getGoodsList() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mvc.perform(get("/api/goods")
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("상품 목록 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data.goodsList.content.size()").value(3))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("상품 상세 정보 조회 기능 테스트")
    void getGoodsDetail() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mvc.perform(get("/api/goods/1")
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("상품 상세 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data.sellerName").value("홍길동"))
                .andExpect(jsonPath("$.data.goodsName").value("test 목걸이"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("등록한 상품 목록 조회 기능 테스트")
    void getRegisterGoodsList() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/my/goods")
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("등록한 상품 목록 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.registerGoods.content.size()").value(2))
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
        User user1 = userRepository.save(newUser("test@naver.com", "홍길동", "010-1111-1111", "서울특별시 종로구"));
        User user2 = userRepository.save(newUser("test22@google.com", "홍길동", "010-1234-1234", "경상북도 경산시"));
        goodsRepository.save(newGoods(user1.getId(), "test 목걸이", 11111, 100, GoodsCategory.FASHION));
        goodsRepository.save(newGoods(user1.getId(), "test 티셔츠", 10000, 10, GoodsCategory.FASHION));
        goodsRepository.save(newGoods(user2.getId(), "test 치마", 59000, 50, GoodsCategory.FASHION));
        em.clear();
    }
}