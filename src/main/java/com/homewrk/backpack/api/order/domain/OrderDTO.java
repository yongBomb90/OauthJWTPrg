package com.homewrk.backpack.api.order.domain;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Data
public class OrderDTO {

    @NotEmpty
    @Pattern(regexp = "[A-Z|0-9]{12}" , message = "주문번호를 확인해주세요")
    private String ordNum; // 주문번호

    @NotEmpty
    private String prdName; // 제품명

    @NotEmpty
    private Integer memSeq; // 주문자

    private Timestamp payDate; // 결제일자

    private Timestamp ordDate; // 주문일자

    public static OrderDTO of(OrderEntity order) {
        if ( order == null ) {
            return null;
        }
        return new ModelMapper().map(order,OrderDTO.class);
    }

    public static Page<OrderDTO> of(Page<OrderEntity> orderPage) {
        if ( orderPage == null ) {
            return null;
        }
        return orderPage.map(OrderDTO::of);
    }

    
}
