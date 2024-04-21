package com.miri.userservice.domain.wishlist;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    boolean existsByUserIdAndGoods_Id(Long userId, Long goodsId);

    Optional<WishList> findByIdAndUserId(Long wishListId, Long userId);

    @Query("SELECT wl FROM WishList wl JOIN FETCH wl.goods WHERE  wl.userId = :userId")
    List<WishList> findByUserIdWithGoods(@Param("userId") Long userId);

    void deleteByIdAndUserId(Long wishListId, Long userId);

    @EntityGraph(attributePaths = {"goods"})
    List<WishList> findByIdInAndUserId(List<Long> wishListIds, Long userId);
}
