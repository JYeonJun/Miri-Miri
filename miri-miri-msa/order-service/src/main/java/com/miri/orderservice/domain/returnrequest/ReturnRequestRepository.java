package com.miri.orderservice.domain.returnrequest;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {

    @Query("SELECT r FROM ReturnRequest r JOIN FETCH r.orderDetail WHERE r.returnStatus = 'RETURN_IN_PROGRESS' AND FUNCTION('DATE', r.lastModifiedDate) < CURRENT_DATE")
    List<ReturnRequest> findReturnRequestsToUpdate();

    @Modifying
    @Query("UPDATE ReturnRequest r SET r.returnStatus = 'RETURN_COMPLETED' " +
            "WHERE r.returnStatus = 'RETURN_IN_PROGRESS' AND " +
            "FUNCTION('DATE', r.lastModifiedDate) < CURRENT_DATE")
    int updateReturnStatusToCompletedOlderThanADay();
}
