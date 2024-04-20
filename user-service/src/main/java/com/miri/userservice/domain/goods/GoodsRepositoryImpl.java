package com.miri.userservice.domain.goods;

import static com.miri.userservice.domain.goods.QGoods.goods;

import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsListRespDto;
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
    public Page<GoodsListRespDto> findPagingGoods(Pageable pageable) {
        JPAQuery<GoodsListRespDto> query = queryFactory
                .select(Projections.constructor(GoodsListRespDto.class,
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

        List<GoodsListRespDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(goods.count())
                .from(goods);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
