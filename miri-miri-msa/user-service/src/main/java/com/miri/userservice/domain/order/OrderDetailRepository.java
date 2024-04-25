package com.miri.userservice.domain.order;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderStatusIn(List<OrderStatus> orderStatusList);

    @Query("SELECT od FROM OrderDetail od JOIN od.order o ON o.userId = :userId WHERE od.id = :orderDetailId")
    Optional<OrderDetail> findByIdAndUserId(@Param("orderDetailId") Long orderDetailId, @Param("userId") Long userId);
}
