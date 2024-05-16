package com.miri.goodsservice.domain.goods;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long>, GoodsRepositoryCustom {

    List<Goods> findByIdIn(Set<Long> goodsIds);

    Optional<Goods> findByIdAndSellerId(Long goodsId, Long sellerId);
}
