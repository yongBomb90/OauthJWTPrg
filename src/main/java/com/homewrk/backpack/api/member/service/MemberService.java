package com.homewrk.backpack.api.member.service;


import com.homewrk.backpack.api.member.domain.MemberEntity;

import com.homewrk.backpack.api.member.repo.MemberRepo;
import com.homewrk.backpack.api.order.domain.OrderEntity;
import com.homewrk.backpack.api.order.repo.OrderRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;

/**
 * 유저관련 서비스
 */
@Service
public class MemberService implements UserDetailsService {

    @Resource
    private MemberRepo memberRepo;

    @Resource
    private OrderRepo orderRepo;

    /**
     * 회원가입
     * @param param
     * @return
     */
    public MemberEntity registerMember(MemberEntity param) {
        return memberRepo.save(param);
    }

    /**
     * 회원아이디 중복 체크
     * @param memId
     * @return
     */
    public boolean isDuplMemId(String memId) {
        return memberRepo.getMemberByMemId(memId) == null ? false : true;
    }

    /**
     * 회원 email로 찾기
     * @param memId
     * @return
     */
    public MemberEntity searchMember(String memId) {
        return memberRepo.getMemberByMemId(memId);
    }


    /**
     * 회원 리스트 정보 조회
     * @param pageable
     * @return
     */
    public Page<MemberEntity> searchMembers(String name, String email, Pageable pageable) {
        return memberRepo.findMembersByNameContainsOrEmailContains(name,email,pageable);
    }

    /**
     * 회원의 주문정보 조회
     * @param memId
     * @param pageable
     * @return
     */
    public Page<OrderEntity> searchMemberOrders(String memId, Pageable pageable) {

        MemberEntity member = memberRepo.getMemberByMemId(memId);
        if ( member == null ) {
            return null;
        }
        return orderRepo.getOrderEntitiesByMemSeqEqualsOrderByOrdDate(member.getSeq(),pageable);
    }

    /**
     * 유저 인증 정보 Load
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity user =  memberRepo.getMemberByMemId(username);
        if ( user == null ) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }


}
