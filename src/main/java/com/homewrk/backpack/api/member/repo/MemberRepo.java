package com.homewrk.backpack.api.member.repo;

import com.homewrk.backpack.api.member.domain.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepo extends JpaRepository<MemberEntity,Integer> {

    /**
     * 로그인처리시 사용
     * @param memId
     * @return
     */
    MemberEntity getMemberByMemId(String memId);


    /**
     * 회원 리스트 정보 조회
     * @param name
     * @param email
     * @param pageable
     * @return
     */
    Page<MemberEntity> findMembersByNameContainsOrEmailContains(String name, String email, Pageable pageable);






}
