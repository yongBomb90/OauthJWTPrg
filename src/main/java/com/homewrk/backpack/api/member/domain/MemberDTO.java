package com.homewrk.backpack.api.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.homewrk.backpack.api.order.domain.OrderDTO;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import javax.validation.constraints.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO{

    private Integer seq;// 사용자 번호

    @NotEmpty
    @Pattern(regexp = "[a-z|A-z|0-9]{1,128}" , message = "사용자 아이디정보를 확인해주세요")
    private String memId; // 사용자 아이디
    @NotEmpty
    @Pattern(regexp = "[a-z|A-z|가-힣]{1,20}" , message = "사용자 이름정보를 확인해주세요")
    private String name; // 사용자 이름
    @NotEmpty
    @Pattern(regexp = "[a-z]{1,30}" , message = "사용자 별명정보를 확인해주세요")
    private String nickName; // 사용자 별명

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @NotEmpty
    @Pattern(regexp = "^.*(?=^.{10,}$)((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])).*$" , message = "사용자 비밀번호를 확인해주세요")
    private String pwd; // 사용자 비밀번호

    @Pattern(regexp = "\\d{10,11}" , message = "사용자 전화번호를 확인해주세요")
    @NotEmpty
    private String phnNum; // 사용자 핸드폰번호
    @Email(message = "사용자 이메일 정보를 확인해주세요")
    private String email; // 사용자 email
    @Pattern(regexp = "[X,F,M]", message = "성별 정보를 확인해주세요")
    private String sex; // 사용자 성별

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private OrderDTO order; // 최근 주문 정보


    public static MemberDTO of(MemberEntity member) {
        if ( member == null ) {
            return null;
        }
        MemberDTO res = new ModelMapper().map(member,MemberDTO.class);
        // 최근 주문정보만 Object로 처리
        if ( member.getOrders() != null && !member.getOrders().isEmpty() ) {
            res.setOrder( OrderDTO.of(member.getOrders().get(0)));
        }
        res.setPwd(null);
        return res;
    }

    public static Page<MemberDTO> of(Page<MemberEntity> memberPage) {
        if ( memberPage == null ) {
            return null;
        }
        return memberPage.map(MemberDTO::of);
    }

}
