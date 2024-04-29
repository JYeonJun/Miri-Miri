package com.miri.goodsservice.domain.wishlist;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListRepository extends JpaRepository<WishList, Long>, WishListRepositoryCustom {

    boolean existsByUserIdAndGoods_Id(Long userId, Long goodsId);

    Optional<WishList> findByIdAndUserId(Long wishListId, Long userId);

    @Query("SELECT wl FROM WishList wl JOIN FETCH wl.goods WHERE  wl.userId = :userId")
    List<WishList> findByUserIdWithGoods(@Param("userId") Long userId);

    void deleteByIdAndUserId(Long wishListId, Long userId);

    @Query("SELECT wl FROM WishList wl JOIN FETCH wl.goods WHERE wl.id IN :wishListIds AND wl.userId = :userId")
    List<WishList> findByIdInAndUserIdWithGoods(List<Long> wishListIds, Long userId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE  FROM WishList wl WHERE wl.id IN :wishListIds")
    void deleteByIdIn(List<Long> wishListIds);
}
