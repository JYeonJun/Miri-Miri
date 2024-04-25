package com.miri.orderservice.domain.goods;

import static com.miri.orderservice.domain.goods.QGoods.goods;

import com.miri.orderservice.dto.goods.ResponseGoodsDto.GoodsListDto;
import com.miri.orderservice.dto.goods.ResponseGoodsDto.RegisterGoodsDto;
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

public class GoodsRepositoryImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GoodsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GoodsListDto> findPagingGoods(Pageable pageable) {
        JPAQuery<GoodsListDto> query = queryFactory
                .select(Projections.constructor(GoodsListDto.class,
                        goods.id,
                        goods.goodsName,
                        goods.goodsPrice,
                        goods.category
                ))
                .from(goods)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    goods.getType(), goods.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<GoodsListDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(goods.count())
                .from(goods);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<RegisterGoodsDto> findPagingRegisterGoods(Long userId, Pageable pageable) {
        JPAQuery<RegisterGoodsDto> query = queryFactory
                .select(Projections.constructor(RegisterGoodsDto.class,
                        goods
                ))
                .from(goods)
                .where(goods.sellerId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    goods.getType(), goods.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<RegisterGoodsDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(goods.count())
                .from(goods)
                .where(goods.sellerId.eq(userId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
