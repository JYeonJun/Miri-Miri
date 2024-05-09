package com.miri.orderservice.service.order;

import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.orderservice.domain.order.Order;
import com.miri.orderservice.domain.order.OrderDetail;
import com.miri.orderservice.domain.order.OrderDetailRepository;
import com.miri.orderservice.domain.order.OrderRepository;
import com.miri.orderservice.domain.order.OrderStatus;
import com.miri.orderservice.domain.shipping.Shipping;
import com.miri.orderservice.domain.shipping.ShippingRepository;
import com.miri.orderservice.domain.shipping.ShippingStatus;
import com.miri.orderservice.event.OrderToPaymentEvent;
import com.miri.orderservice.service.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderInternalServiceImpl implements OrderInternalService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ShippingRepository shippingRepository;
    private final KafkaSender kafkaSender;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderInternalServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
                                    ShippingRepository shippingRepository, KafkaSender kafkaSender,
                                    ApplicationEventPublisher applicationEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.shippingRepository = shippingRepository;
        this.kafkaSender = kafkaSender;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public void processOrder(OrderRequestEventDto orderRequestEventDto) {
        Order order = orderRepository.save(new Order(orderRequestEventDto.getUserId()));
        OrderDetail orderDetail = orderDetailRepository.save(new OrderDetail(order, orderRequestEventDto));
        shippingRepository.save(new Shipping(orderDetail.getId(), orderRequestEventDto.getAddress()));
        applicationEventPublisher.publishEvent(new OrderToPaymentEvent(this, orderRequestEventDto, order.getId()));
    }

    @Override
    @Transactional
    public void updateOrderStatusOnFailure(Long orderId) {
        orderRepository.findById(orderId).orElseThrow(() -> new CustomApiException("존재하지 않는 주문입니다."));
        orderDetailRepository.updateOrderStatusByOrderId(orderId, OrderStatus.CANCELED);
        shippingRepository.updateShippingStatusByOrderDetailsOrderId(orderId, ShippingStatus.CANCELED);
    }
}
