package com.miri.goodsservice.dummy;

import com.miri.goodsservice.domain.goods.Goods;
import com.miri.goodsservice.domain.goods.GoodsCategory;
import com.miri.goodsservice.domain.wishlist.WishList;

public class DummyObject {
    protected Goods newGoods(Long sellerId, String goodsName
            , int goodsPrice, int stockQuantity
            , GoodsCategory category) {

        return Goods.builder()
                .sellerId(sellerId)
                .goodsName(goodsName)
                .goodsDescription("test용 상품")
                .goodsPrice(goodsPrice)
                .stockQuantity(stockQuantity)
                .category(category)
                .build();
    }

    protected WishList newWishList(Long userId, Goods goods, int quantity) {

        return WishList.builder()
                .userId(userId)
                .goods(goods)
                .quantity(quantity)
                .build();
    }
}
