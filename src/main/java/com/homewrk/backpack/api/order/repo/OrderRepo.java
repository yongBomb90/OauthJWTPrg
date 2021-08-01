package com.homewrk.backpack.api.order.repo;

import com.homewrk.backpack.api.order.domain.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo  extends JpaRepository<OrderEntity,String> {

    /**
     * 회원의 주문 리스트 조회
     * @param memSeq
     * @param pageable
     * @return
     */
    Page<OrderEntity> getOrderEntitiesByMemSeqEqualsOrderByOrdDate(Integer memSeq,Pageable pageable);


}
