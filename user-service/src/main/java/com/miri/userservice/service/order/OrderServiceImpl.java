package com.miri.userservice.service.order;

import com.miri.userservice.domain.goods.Goods;
import com.miri.userservice.domain.order.Order;
import com.miri.userservice.domain.order.OrderDetail;
import com.miri.userservice.domain.order.OrderDetailRepository;
import com.miri.userservice.domain.order.OrderRepository;
import com.miri.userservice.domain.wishlist.WishList;
import com.miri.userservice.domain.wishlist.WishListRepository;
import com.miri.userservice.dto.order.RequestOrderDto.CreateOrderReqDto;
import com.miri.userservice.dto.order.ResponseOrderDto.CreateOrderRespDto;
import com.miri.userservice.dto.order.ResponseOrderDto.OrderGoodsRespDto;
import com.miri.userservice.handler.ex.CustomApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WishListRepository wishListRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
                            WishListRepository wishListRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.wishListRepository = wishListRepository;
    }

    // 상품 주문할 때, 상품 재고 마이너스!!
    // 장바구니에 있는 상품 구매하고, 해당 객체 삭제하기
    @Override
    @Transactional
    public CreateOrderRespDto createOrder(Long userId, CreateOrderReqDto reqDto) {

        Order order = orderRepository.save(new Order(userId));

        // userId와 wishListId 목록으로 조회한 List<WishList> 가지고 처리하기!!
        List<WishList> wishLists
                = wishListRepository.findByIdInAndUserId(reqDto.getWishListIds(), userId);

        validateWishListIds(reqDto.getWishListIds(), wishLists);

        List<OrderDetail> orderDetails = new ArrayList<>();
        List<OrderGoodsRespDto> orderGoods = new ArrayList<>();
        int totalOrderPrice = calculateTotalOrderPriceAndPrepareOrderDetails(wishLists, order, orderDetails, orderGoods);

        orderDetailRepository.saveAll(orderDetails);
        wishListRepository.deleteAll(wishLists);

        return new CreateOrderRespDto(order, orderGoods, totalOrderPrice);
    }

    private void validateWishListIds(List<Long> requestedIds, List<WishList> foundWishLists) {
        Set<Long> foundIds = foundWishLists.stream().map(WishList::getId).collect(Collectors.toSet());
        if (!foundIds.containsAll(requestedIds)) {
            throw new CustomApiException("유효하지 않은 위시리스트 ID가 포함되어 있습니다.");
        }
    }

    private int calculateTotalOrderPriceAndPrepareOrderDetails(List<WishList> wishLists, Order order, List<OrderDetail> orderDetails, List<OrderGoodsRespDto> orderGoods) {
        int totalOrderPrice = 0;
        for (WishList wishList : wishLists) {
            Goods goods = wishList.getGoods();
            goods.decreaseStock(wishList.getQuantity());

            int totalPriceForGoods = goods.getGoodsPrice() * wishList.getQuantity();
            totalOrderPrice += totalPriceForGoods;

            orderDetails.add(new OrderDetail(order, wishList, goods));
            orderGoods.add(new OrderGoodsRespDto(wishList, goods, totalPriceForGoods));
        }
        return totalOrderPrice;
    }
}
