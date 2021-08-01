package com.homewrk.backpack.api.order.domain;

import com.homewrk.backpack.api.member.domain.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "ORD")
@DynamicInsert
@DynamicUpdate
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    @Column(name = "ORD_NUM")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String ordNum;

    @Column(name = "PRD_NAME")
    private String prdName;

    @Column(name = "MEM_SEQ")
    private Integer memSeq;

    @Column(name = "PAY_DATE")
    private Timestamp payDate; // 결제일자

    @Column(name = "ORD_DATE")
    private Timestamp ordDate; // 주문일자

    @Column(name = "REG_DATE")
    @CreationTimestamp
    private Timestamp regDate; // 생성일자
    @Column(name = "UPT_DATE")
    @UpdateTimestamp
    private Timestamp uptDate; // 수정일자

    @ManyToOne
    @JoinColumn( name = "MEM_SEQ" , referencedColumnName = "SEQ" , insertable = false, updatable = false)
    private MemberEntity member;

    public static OrderEntity of(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }
        return new ModelMapper().map(orderDTO, OrderEntity.class);
    }

    public static Page<OrderEntity> of(Page<OrderDTO> orderDTOPage) {
        if ( orderDTOPage == null ) {
            return null;
        }
        return orderDTOPage.map(OrderEntity::of);
    }
}
