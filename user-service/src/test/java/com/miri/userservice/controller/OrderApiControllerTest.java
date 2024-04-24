package com.miri.userservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miri.userservice.domain.goods.Goods;
import com.miri.userservice.domain.goods.GoodsCategory;
import com.miri.userservice.domain.goods.GoodsRepository;
import com.miri.userservice.domain.order.Order;
import com.miri.userservice.domain.order.OrderDetailRepository;
import com.miri.userservice.domain.order.OrderRepository;
import com.miri.userservice.domain.order.OrderStatus;
import com.miri.userservice.domain.user.User;
import com.miri.userservice.domain.user.UserRepository;
import com.miri.userservice.domain.wishlist.WishListRepository;
import com.miri.userservice.dto.order.RequestOrderDto.CreateOrderReqDto;
import com.miri.userservice.dto.order.RequestOrderDto.ReturnOrderReqDto;
import com.miri.userservice.dummy.DummyObject;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
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
class OrderApiControllerTest extends DummyObject {

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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    @DisplayName("상품 주문 기능 테스트")
    void createOrder() throws Exception {
        // given
        CreateOrderReqDto reqDto = new CreateOrderReqDto();
        List<Long> wishListIds = Arrays.asList(1L);
        reqDto.setWishListIds(wishListIds);
        reqDto.setAddress("경상북도 울릉군 울릉읍 울릉경비대");
        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions =
                mvc.perform(post("/api/auth/orders")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("주문이 완료되었습니다."))
                .andExpect(jsonPath("$.data.orderGoods.size()").value(wishListIds.size()))
                .andDo(MockMvcResultHandlers.print());

        Goods findGoods = goodsRepository.findById(3L).get();
        assertThat(findGoods.getStockQuantity()).isEqualTo(40);
    }

    @Test
    @DisplayName("주문한 상품 목록 조회 기능 테스트")
    void getOrderGoodsList() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mvc.perform(get("/api/auth/orders")
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("주문한 상품 목록 조회가 완료되었습니다."))
                .andExpect(jsonPath("$.data.orders.content.size()").value(2))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("주문 취소 기능 테스트")
    void cancelOrder() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mvc.perform(post("/api/auth/orders/1/cancel")
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("주문 취소 되었습니다."))
                .andDo(MockMvcResultHandlers.print());

        Goods findGoods = goodsRepository.findById(4L).get();
        assertThat(findGoods.getStockQuantity()).isEqualTo(153);
    }

    @Test
    @DisplayName("주문 취소 기능 예외 테스트")
    void cancelOrderException() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mvc.perform(post("/api/auth/orders/2/cancel")
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("주문 취소가 불가능한 상태입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("반품 기능 예외 테스트")
    void returnOrderExcepiton() throws Exception {
        // given
        ReturnOrderReqDto reqDto = new ReturnOrderReqDto();
        reqDto.setReason("반품 기능 테스트중입니다!!");
        String requestBody = om.writeValueAsString(reqDto);
        // when
        ResultActions resultActions =
                mvc.perform(post("/api/auth/orders/1/return")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("반품이 불가능한 상태입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
        User user1 = userRepository.save(newUser("test@naver.com", "홍길동", "010-1111-1111", "서울특별시 종로구"));
        User user2 = userRepository.save(newUser("test22@google.com", "홍길동", "010-1234-1234", "경상북도 경산시"));
        goodsRepository.save(newGoods(user1.getId(), "test 목걸이", 11111, 100, GoodsCategory.FASHION));
        goodsRepository.save(newGoods(user1.getId(), "test 티셔츠", 10000, 10, GoodsCategory.FASHION));
        Goods goods1 = goodsRepository.save(newGoods(user2.getId(), "test 치마", 59000, 50, GoodsCategory.FASHION));
        Goods goods2 = goodsRepository.save(newGoods(user2.getId(), "test 운동화", 127000, 150, GoodsCategory.FASHION));
        Goods goods3 = goodsRepository.save(newGoods(user2.getId(), "test 향수", 50000, 55, GoodsCategory.BEAUTY));
        wishListRepository.save(newWishList(user1.getId(), goods1, 10));
        wishListRepository.save(newWishList(user1.getId(), goods2, 10));
        Order order = orderRepository.save(newOrder(user1.getId()));
        orderDetailRepository.save(
                newOrderDetail(order, goods2.getId(), OrderStatus.PENDING, 3, goods2.getGoodsPrice()));
        orderDetailRepository.save(
                newOrderDetail(order, goods3.getId(), OrderStatus.DELIVERED, 3, goods2.getGoodsPrice()));
        em.clear();
    }
}