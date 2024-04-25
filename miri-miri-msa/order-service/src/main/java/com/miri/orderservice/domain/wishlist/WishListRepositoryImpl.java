package com.miri.orderservice.domain.wishlist;

import static com.miri.orderservice.domain.goods.QGoods.goods;
import static com.miri.orderservice.domain.wishlist.QWishList.wishList;

import com.miri.orderservice.dto.wishlist.ResponseWishListDto.GoodsInWishListRespDto;
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

public class WishListRepositoryImpl implements WishListRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WishListRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GoodsInWishListRespDto> findPagingGoodsInWishList(Long userId, Pageable pageable) {
        JPAQuery<GoodsInWishListRespDto> query = queryFactory
                .select(Projections.constructor(GoodsInWishListRespDto.class,
                        wishList,
                        goods
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
