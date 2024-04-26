package com.miri.orderservice.domain.order;

import static com.miri.orderservice.domain.order.QOrder.order;
import static com.miri.orderservice.domain.order.QOrderDetail.orderDetail;

import com.miri.orderservice.dto.order.ResponseOrderDto.OrderGoodsDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<OrderGoodsDto> findPagingOrderList(Long userId, Pageable pageable) {

        JPAQuery<OrderGoodsDto> query = queryFactory
                .select(Projections.constructor(OrderGoodsDto.class,
                        order,
                        orderDetail
//                        goods
                ))
                .from(orderDetail)
                .join(orderDetail.order, order)
//                .leftJoin(goods).on(orderDetail.goodsId.eq(goods.id))
                .where(order.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    orderDetail.getType(), orderDetail.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? com.querydsl.core.types.Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<OrderGoodsDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(orderDetail.count())
                .from(orderDetail)
                .join(orderDetail.order, order)
                .where(order.userId.eq(userId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
