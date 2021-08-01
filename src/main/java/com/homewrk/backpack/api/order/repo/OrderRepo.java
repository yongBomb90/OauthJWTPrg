package com.homewrk.backpack.api.order.repo;

import com.homewrk.backpack.api.order.domain.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepo  extends JpaRepository<OrderEntity,String> {

    /**
     * 회원의 주문 리스트 조회
     * @param memSeq
     * @param pageable
     * @return
     */
    @Query(
        value = "SELECT O FROM OrderEntity O JOIN MemberEntity M ON O.memSeq = M.seq and M.memId = :memId "
            , countQuery = "SELECT count(O.ordNum) FROM OrderEntity O JOIN MemberEntity M ON O.memSeq = M.seq and M.memId = :memId "
    )
    Page<OrderEntity> getOrderEntitiesByMemIdOrderByOrdDate(String memId,Pageable pageable);


}
