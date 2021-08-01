package com.homewrk.backpack.api.member.repo;

import com.homewrk.backpack.api.member.domain.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MemberRepo extends JpaRepository<MemberEntity,Integer> {

    /**
     * 로그인처리시 사용
     * @param memId
     * @return
     */
    MemberEntity getMemberEntityByMemId(String memId);

    @Query(
            value = "select m from MemberEntity m where m.email like  CONCAT('%',:email,'%') or m.name like  CONCAT('%',:name,'%')"
            , countQuery = "select count(m.memId) from MemberEntity m where m.email like  CONCAT('%',:email,'%') or m.name like  CONCAT('%',:name,'%')"
    )
    Page<MemberEntity> findMemberEntitiesByEmailNameOrderByName(String name, String email, Pageable pageable);


}
