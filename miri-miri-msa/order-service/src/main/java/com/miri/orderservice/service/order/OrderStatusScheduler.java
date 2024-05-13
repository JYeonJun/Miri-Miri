package com.miri.orderservice.service.order;

import com.miri.orderservice.client.GoodsServiceClient;
import com.miri.orderservice.domain.order.OrderDetail;
import com.miri.orderservice.domain.returnrequest.ReturnRequest;
import com.miri.orderservice.domain.returnrequest.ReturnRequestRepository;
import com.miri.orderservice.domain.shipping.ShippingRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderStatusScheduler {

    private final ReturnRequestRepository returnRequestRepository;
    private final ShippingRepository shippingRepository;
    private final GoodsServiceClient goodsServiceClient;

    public OrderStatusScheduler(ReturnRequestRepository returnRequestRepository, ShippingRepository shippingRepository,
                                GoodsServiceClient goodsServiceClient) {
        this.returnRequestRepository = returnRequestRepository;
        this.shippingRepository = shippingRepository;
        this.goodsServiceClient = goodsServiceClient;
    }

    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void updatePendingToInTransit() {
        // '배송 대기'인 배송의 상태를 '배송 중'으로 변경하고, 변경된 행의 수를 반환
        int count = shippingRepository.updateShippingStatusToInTransitIfOlderThanADay();
        log.info("[배송 상태 업데이트]: 배송 대기 -> 배송 중, count={}", count);
    }

    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void updateInTransitDelivered() {
        // '배송 중'인 상품의 상태를 '배송 완료'로 변경하고, 변경된 행의 수를 반환
        int count = shippingRepository.updateShippingStatusToDeliveredIfOlderThanADay();
        log.info("[배송 상태 업데이트]: 배송 중 -> 배송 완료, count={}", count);
    }

    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void updateReturnStatusToCompleted() {
        // '반품 처리 중'인 상품의 상태를 '반품 완료'로 변경하고, 변경된 행의 수를 반환
        List<ReturnRequest> returnRequests = returnRequestRepository.findReturnRequestsToUpdate();
        int count = returnRequestRepository.updateReturnStatusToCompletedOlderThanADay();
        log.info("[반품 상태 업데이트]: 반품 처리 중 -> 반품 완료, count={}", count);

        if (count > 0) {
            List<OrderDetail> orderDetails = returnRequests.stream().map(ReturnRequest::getOrderDetail).toList();
            Map<Long, Integer> goodsIdToQuantityMap = orderDetails.stream()
                    .collect(Collectors.groupingBy(OrderDetail::getGoodsId,
                            Collectors.summingInt(OrderDetail::getQuantity)));

            // 상품 재고 증가 요청
            goodsServiceClient.increaseStock(goodsIdToQuantityMap);
        }
    }
}
