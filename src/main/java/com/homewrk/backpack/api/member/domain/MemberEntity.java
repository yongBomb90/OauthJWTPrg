package com.homewrk.backpack.api.member.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homewrk.backpack.api.order.domain.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "MEMBER")
@DynamicInsert
@DynamicUpdate
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntity implements UserDetails {

    @Id
    @Column(name = "SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;// 사용자 번호
    @Column(name = "MEM_ID" , unique = true)
    private String memId; // 사용자 아이디
    @Column(name = "NAME" )
    private String name; // 사용자 이름
    @Column(name = "NICK_NAME")
    private String nickName; // 사용자 별명
    @Column(name = "PWD")
    private String pwd; // 사용자 비밀번호
    @Column(name = "PHN_NUM")
    private String phnNum; // 사용자 핸드폰번호
    @Column(name = "EMAIL" )
    private String email;  // 사용자 이메일
    @Column(name = "SEX" , columnDefinition = "ENUM('M', 'F', 'X')")
    @Enumerated(EnumType.STRING)
    private SEX sex; // 사용자 성별
    @Column(name = "REG_DATE")
    @CreationTimestamp
    private Timestamp regDate; // 사용자 가입일자
    @Column(name = "UPT_DATE")
    @UpdateTimestamp
    private Timestamp uptDate; // 사용자 정보 수정일자

    @OneToMany(mappedBy = "member", cascade = CascadeType.REFRESH )
    @OrderBy("ordDate desc")
    @BatchSize(size = 10)
    private List<OrderEntity> orders;


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return "{noop}"+pwd;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return name;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum SEX {
        M, // 남자
        F, // 여자
        X; // 미입력
    }

    public static MemberEntity of(MemberDTO memberDTO) {
        if ( memberDTO == null) {
            return null;
        }
        return new ModelMapper().map(memberDTO, MemberEntity.class);
    }

    public static Page<MemberEntity> of(Page<MemberDTO> memberDTOPage) {
        if ( memberDTOPage == null) {
            return null;
        }
        return memberDTOPage.map(MemberEntity::of);
    }

}

