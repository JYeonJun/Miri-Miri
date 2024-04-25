package com.miri.orderservice.service.order;

import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.orderservice.domain.goods.Goods;
import com.miri.orderservice.domain.goods.GoodsRepository;
import com.miri.orderservice.domain.order.Order;
import com.miri.orderservice.domain.order.OrderDetail;
import com.miri.orderservice.domain.order.OrderDetailRepository;
import com.miri.orderservice.domain.order.OrderRepository;
import com.miri.orderservice.domain.order.OrderStatus;
import com.miri.orderservice.domain.returnrequest.ReturnRequest;
import com.miri.orderservice.domain.returnrequest.ReturnRequestRepository;
import com.miri.orderservice.domain.shipping.Shipping;
import com.miri.orderservice.domain.shipping.ShippingRepository;
import com.miri.orderservice.domain.wishlist.WishList;
import com.miri.orderservice.domain.wishlist.WishListRepository;
import com.miri.orderservice.dto.order.RequestOrderDto.CreateOrderReqDto;
import com.miri.orderservice.dto.order.RequestOrderDto.ReturnOrderReqDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.CreateOrderRespDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.OrderGoodsListRespDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.OrderGoodsRespDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WishListRepository wishListRepository;
    private final ShippingRepository shippingRepository;
    private final GoodsRepository goodsRepository;
    private final ReturnRequestRepository returnRequestRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
                            WishListRepository wishListRepository, ShippingRepository shippingRepository,
                            GoodsRepository goodsRepository, ReturnRequestRepository returnRequestRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.wishListRepository = wishListRepository;
        this.shippingRepository = shippingRepository;
        this.goodsRepository = goodsRepository;
        this.returnRequestRepository = returnRequestRepository;
    }

    // 상품 주문할 때, 상품 재고 마이너스!!
    // 장바구니에 있는 상품 구매하고, 해당 객체 삭제하기
    @Override
    @Transactional
    public CreateOrderRespDto createOrder(Long userId, CreateOrderReqDto reqDto) {

        Order order = orderRepository.save(new Order(userId));

        List<Long> wishListIds = reqDto.getWishListIds();
        List<WishList> wishLists = wishListRepository.findByIdInAndUserIdWithGoods(wishListIds, userId);

        validateWishListIds(wishListIds, wishLists);

        List<OrderDetail> orderDetails = new ArrayList<>();
        List<OrderGoodsRespDto> orderGoods = new ArrayList<>();
        int totalOrderPrice
                = calculateTotalOrderPriceAndPrepareOrderDetails(wishLists, order, orderDetails, orderGoods);

        List<OrderDetail> createdOrderDetails = orderDetailRepository.saveAll(orderDetails);
        wishListRepository.deleteAll(wishLists);

        List<Shipping> shippings = new ArrayList<>();
        for (OrderDetail createdOrderDetail : createdOrderDetails) {
            shippings.add(new Shipping(createdOrderDetail.getId(), reqDto.getAddress()));
        }

        shippingRepository.saveAll(shippings);

        return new CreateOrderRespDto(order, orderGoods, totalOrderPrice);
    }

    private void validateWishListIds(List<Long> requestedIds, List<WishList> foundWishLists) {
        Set<Long> foundIds = foundWishLists.stream().map(WishList::getId).collect(Collectors.toSet());
        if (!foundIds.containsAll(requestedIds)) {
            throw new CustomApiException("유효하지 않은 위시리스트 ID가 포함되어 있습니다.");
        }
    }

    private int calculateTotalOrderPriceAndPrepareOrderDetails(List<WishList> wishLists, Order order,
                                                               List<OrderDetail> orderDetails,
                                                               List<OrderGoodsRespDto> orderGoods) {
        int totalOrderPrice = 0;
        for (WishList wishList : wishLists) {
            Goods goods = wishList.getGoods();
            goods.decreaseStock(wishList.getQuantity());

            int totalPriceForGoods = goods.getGoodsPrice() * wishList.getQuantity();
            totalOrderPrice += totalPriceForGoods;

            OrderDetail orderDetail = new OrderDetail(order, wishList, goods);
            orderDetails.add(orderDetail);
            orderGoods.add(new OrderGoodsRespDto(wishList, orderDetail, goods, totalPriceForGoods));
        }
        return totalOrderPrice;
    }


    @Override
    public OrderGoodsListRespDto getOrderGoodsList(Long userId, Pageable pageable) {
        return new OrderGoodsListRespDto(orderRepository.findPagingOrderList(userId, pageable));
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderDetailId) {
        OrderDetail findOrderDetail = validateOrderDetail(userId, orderDetailId);

        validateCancelCondition(findOrderDetail);

        Goods goods = findGoodsOrThrow(findOrderDetail);

        restoreGoodsStock(goods, findOrderDetail);

        updateOrderStatusToCanceled(findOrderDetail);
    }

    @Override
    @Transactional
    public void returnOrder(Long userId, Long orderDetailId, ReturnOrderReqDto reqDto) {
        OrderDetail findOrderDetail = validateOrderDetail(userId, orderDetailId);

        validateReturnCondition(findOrderDetail);

        processReturnRequest(findOrderDetail, reqDto);
    }

    private void validateReturnCondition(OrderDetail findOrderDetail) {
        LocalDate now = LocalDate.now();
        if (findOrderDetail.getOrderStatus() == OrderStatus.DELIVERED
                && !now.isAfter(findOrderDetail.getLastModifiedDate().toLocalDate().plusDays(1))) {
            return;
        }
        throw new CustomApiException("반품이 불가능한 상태입니다.");
    }

    private void processReturnRequest(OrderDetail findOrderDetail, ReturnOrderReqDto reqDto) {
        findOrderDetail.changeOrderStatus(OrderStatus.RETURN_IN_PROGRESS);
        returnRequestRepository.save(new ReturnRequest(findOrderDetail, reqDto.getReason()));
    }

    private OrderDetail validateOrderDetail(Long userId, Long orderDetailId) {
        return orderDetailRepository.findByIdAndUserId(orderDetailId, userId)
                .orElseThrow(() -> new CustomApiException("유효하지 않은 주문입니다."));
    }

    private void validateCancelCondition(OrderDetail findOrderDetail) {
        if (findOrderDetail.getOrderStatus() != OrderStatus.PENDING) {
            throw new CustomApiException("주문 취소가 불가능한 상태입니다.");
        }
    }

    private Goods findGoodsOrThrow(OrderDetail findOrderDetail) {
        return goodsRepository.findById(findOrderDetail.getGoodsId())
                .orElseThrow(() -> new CustomApiException("존재하지 않는 상품입니다."));
    }

    private void restoreGoodsStock(Goods goods, OrderDetail findOrderDetail) {
        goods.increaseStock(findOrderDetail.getQuantity());
    }

    private void updateOrderStatusToCanceled(OrderDetail findOrderDetail) {
        findOrderDetail.changeOrderStatus(OrderStatus.CANCELED);
    }
}
