package com.miri.goodsservice.domain.wishlist;

import static com.miri.goodsservice.domain.goods.QGoods.goods;
import static com.miri.goodsservice.domain.wishlist.QWishList.wishList;

import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.GoodsInWishListRespDto;
import com.miri.goodsservice.domain.goods.GoodsCategory;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

public class WishListRepositoryImpl implements WishListRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WishListRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GoodsInWishListRespDto> findPagingGoodsInWishList(Long userId, Pageable pageable) {
        Expression<String> categoryExpression = new CaseBuilder()
                .when(goods.category.eq(GoodsCategory.FASHION)).then("패션")
                .when(goods.category.eq(GoodsCategory.BEAUTY)).then("뷰티")
                .when(goods.category.eq(GoodsCategory.ETC)).then("기타")
                .otherwise("알수없음");

        NumberExpression<Integer> subTotalPriceExpression = wishList.quantity.multiply(goods.goodsPrice);

        JPAQuery<GoodsInWishListRespDto> query = queryFactory
                .select(Projections.constructor(GoodsInWishListRespDto.class,
                        wishList.id,
                        goods.id,
                        goods.goodsName,
                        goods.goodsPrice,
                        subTotalPriceExpression,
                        goods.stockQuantity,
                        categoryExpression,
                        wishList.quantity,
                        goods.reservationStartTime,
                        wishList.createdDate
                ))
                .from(wishList)
                .leftJoin(wishList.goods, goods)
                .where(wishList.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    wishList.getType(), wishList.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<GoodsInWishListRespDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(wishList.count())
                .from(wishList)
                .leftJoin(wishList.goods, goods)
                .where(wishList.userId.eq(userId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
