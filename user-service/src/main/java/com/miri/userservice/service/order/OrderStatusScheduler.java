package com.miri.userservice.service.order;

import com.miri.userservice.domain.goods.Goods;
import com.miri.userservice.domain.goods.GoodsRepository;
import com.miri.userservice.domain.order.OrderDetail;
import com.miri.userservice.domain.order.OrderDetailRepository;
import com.miri.userservice.domain.order.OrderStatus;
import com.miri.userservice.domain.shipping.ShippingRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderStatusScheduler {

    private final OrderDetailRepository orderDetailRepository;
    private final ShippingRepository shippingRepository;
    private final GoodsRepository goodsRepository;

    public OrderStatusScheduler(OrderDetailRepository orderDetailRepository, ShippingRepository shippingRepository,
                                GoodsRepository goodsRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.shippingRepository = shippingRepository;
        this.goodsRepository = goodsRepository;
    }

    @Scheduled(cron = "0 0 6 * * *") // 매일 오전 6시에 실행
    @Transactional
    public void updateOrderStatus() {
        // 터질 수 있으니 페이징 방식처럼 나눠서 처리할 수도 있음(데이터가 많아도 처리 가능)
        // 스케줄러 3개로 나눠서 처리하는 게 좋다.
        log.info("OrderStatus Update Scheduler Start!!");
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderStatusIn( // 쿼리 날릴 때 시간도 함께 날려서
                Arrays.asList(OrderStatus.PENDING, OrderStatus.IN_TRANSIT, OrderStatus.RETURN_IN_PROGRESS));

        LocalDate now = LocalDate.now();

        // 차라리 필터를 써서 주문상태별로 나눠서 처리하기

        for (OrderDetail orderDetail : orderDetails) {
            LocalDate lastModifiedDate = orderDetail.getLastModifiedDate().toLocalDate();

            if (now.isAfter(lastModifiedDate)) { // 쿼리로 한 번에 처리하기!!
                switch (orderDetail.getOrderStatus()) {
                    case PENDING:
                        updateToInTransit(orderDetail);
                        break;
                    case IN_TRANSIT:
                        updateToDelivered(orderDetail);
                        break;
                    case RETURN_IN_PROGRESS:
                        updateToReturnCompleted(orderDetail);
                }
            }
        }

        log.info("OrderStatus Update Scheduler End!!");
    }

    private void updateToInTransit(OrderDetail orderDetail) {
        orderDetail.changeOrderStatus(OrderStatus.IN_TRANSIT);
        log.info("Order {} status changed to IN_TRANSIT", orderDetail.getId());
    }

    private void updateToDelivered(OrderDetail orderDetail) {
        orderDetail.changeOrderStatus(OrderStatus.DELIVERED);
        log.info("Order {} status changed to DELIVERED", orderDetail.getId());
    }

    private void updateToReturnCompleted(OrderDetail orderDetail) {
        Optional<Goods> goodsOpt = goodsRepository.findById(orderDetail.getGoodsId());
        goodsOpt.ifPresent(goods -> goods.increaseStock(orderDetail.getQuantity()));
        // ex) 1~ 10, 10개의 반품 1번은 양말 한개, 2번은 티셔츠 두개, 3번은 양말 세개
        // -> 실제로는 양말이랑 티셔츠에 대해 쿼리가 2번만 발생하면 된다. (첫번째 개선 방법)
        // orderDetail을 여러개 받으면 상품ID도 알 수 있으니 In절로 처리 가능 -> Set 같은 자료구조를 활용해서 상품 처리 가능
        // Map으로 상품ID와 개수를 처리해서
        orderDetail.changeOrderStatus(OrderStatus.RETURN_COMPLETED);
        log.info("Order {} status changed to RETURN_COMPLETED", orderDetail.getId());
    }
}
