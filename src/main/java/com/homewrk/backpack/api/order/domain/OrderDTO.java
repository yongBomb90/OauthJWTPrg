package com.homewrk.backpack.api.order.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Data
@ApiModel(value = "주문정보", description = "고객의 주문정보")
public class OrderDTO {

    @ApiModelProperty(value = "주문번호" , example = "1234567890AB")
    @NotEmpty
    @Pattern(regexp = "[A-Z|0-9]{12}" , message = "주문번호를 확인해주세요")
    private String ordNum; // 주문번호

    @ApiModelProperty(value = "제품명" , example = "테스트제품")
    @NotEmpty
    private String prdName; // 제품명

    @ApiModelProperty(value = "결제일자" )
    private String payDate; // 결제일자
    @ApiModelProperty(value = "주문일자" )
    private String ordDate; // 주문일자

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
