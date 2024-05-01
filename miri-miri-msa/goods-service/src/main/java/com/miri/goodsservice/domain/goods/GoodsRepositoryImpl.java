package com.miri.goodsservice.domain.goods;

import static com.miri.goodsservice.domain.goods.QGoods.goods;

import com.miri.coremodule.dto.goods.FeignGoodsRespDto.RegisterGoodsDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsListDto;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
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

        Expression<String> categoryExpression = new CaseBuilder()
                .when(goods.category.eq(GoodsCategory.FASHION)).then("패션")
                .when(goods.category.eq(GoodsCategory.BEAUTY)).then("뷰티")
                .when(goods.category.eq(GoodsCategory.ETC)).then("기타")
                .otherwise("알수없음");

        JPAQuery<RegisterGoodsDto> query = queryFactory
                .select(Projections.constructor(RegisterGoodsDto.class,
                        goods.id,
                        goods.goodsName,
                        goods.goodsPrice,
                        goods.stockQuantity,
                        categoryExpression,
                        goods.createdDate,
                        goods.lastModifiedDate
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
