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
        log.info("OrderStatus Update Scheduler Start!!");
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderStatusIn(
                Arrays.asList(OrderStatus.PENDING, OrderStatus.IN_TRANSIT, OrderStatus.RETURN_IN_PROGRESS));

        LocalDate now = LocalDate.now();

        for (OrderDetail orderDetail : orderDetails) {
            LocalDate lastModifiedDate = orderDetail.getLastModifiedDate().toLocalDate();

            if (now.isAfter(lastModifiedDate)) {
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
        orderDetail.changeOrderStatus(OrderStatus.RETURN_COMPLETED);
        log.info("Order {} status changed to RETURN_COMPLETED", orderDetail.getId());
    }
}
