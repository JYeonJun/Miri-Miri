package com.miri.orderservice.domain.shipping;

import static org.assertj.core.api.Assertions.assertThat;

import com.miri.orderservice.domain.order.Order;
import com.miri.orderservice.domain.order.OrderDetail;
import com.miri.orderservice.domain.order.OrderDetailRepository;
import com.miri.orderservice.domain.order.OrderRepository;
import com.miri.orderservice.domain.order.OrderStatus;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class ShippingRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ShippingRepository shippingRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        autoIncrementReset();
        dataSetting();
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("배송 취소 상태 변경 테스트")
    public void updateShippingStatusByOrderDetailsOrderId_test() {
        int result = shippingRepository.updateShippingStatusByOrderDetailsOrderId(1L, ShippingStatus.CANCELED);
        assertThat(result).isEqualTo(1);
        em.flush();
        em.clear();
        Shipping shipping = shippingRepository.findById(1L).orElseThrow();
        assertThat(shipping.getShippingStatus()).isEqualTo(ShippingStatus.CANCELED);
        System.out.println("shipping.getShippingStatus().getValue() = " + shipping.getShippingStatus().getValue());
    }

    private void autoIncrementReset() {

        em.createNativeQuery("ALTER TABLE shipping ALTER COLUMN shipping_id RESTART WITH 1").executeUpdate();
    }

    private void dataSetting() {
        Long userId = 1L;
        Order order = orderRepository.save(new Order(userId));
        OrderDetail orderDetail = orderDetailRepository.save(createOrderDetail(order, 1L));
        shippingRepository.save(createShipping(orderDetail));
    }

    private static OrderDetail createOrderDetail(Order order, Long goodsId) {
        return OrderDetail.builder()
                .order(order)
                .goodsId(goodsId)
                .orderStatus(OrderStatus.COMPLETED)
                .quantity(10)
                .unitPrice(1000)
                .build();
    }

    private static Shipping createShipping(OrderDetail orderDetail) {
        return Shipping.builder()
                .trackingNumber(UUID.randomUUID().toString())
                .orderDetailId(orderDetail.getId())
                .address("서울특별시")
                .shippingStatus(ShippingStatus.IN_TRANSIT)
                .build();
    }
}