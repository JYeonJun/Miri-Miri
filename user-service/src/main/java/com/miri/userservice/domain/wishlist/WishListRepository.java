package com.miri.userservice.domain.wishlist;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    boolean existsByUserIdAndGoods_Id(Long userId, Long goodsId);

    Optional<WishList> findByIdAndUserId(Long wishListId, Long userId);

    @EntityGraph(attributePaths = {"goods"})
    List<WishList> findByUserIdWithGoods(Long userId);

    void deleteByIdAndUserId(Long wishListId, Long userId);
}
