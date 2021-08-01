package com.homewrk.backpack.api.member.controller;


import com.homewrk.backpack.api.member.domain.MemberEntity;
import com.homewrk.backpack.api.member.domain.MemberDTO;
import com.homewrk.backpack.api.member.service.MemberService;
import com.homewrk.backpack.api.order.domain.OrderEntity;
import com.homewrk.backpack.api.order.domain.OrderDTO;
import com.homewrk.backpack.common.constant.MessageConstant;
import com.homewrk.backpack.common.domain.ResultDTO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@ApiOperation(value = "유저관련")
public class MemberController {


    @Autowired
    private MemberService memberService;



    @ApiOperation(
            value = "아이디중복확인"
            , notes = "아이디중복확인."
            , httpMethod = "GET"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memId", value = "고객의 아이디", required = true, dataType = "string",  paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200 , message = "성공" )
            , @ApiResponse(code = 409 , message = "해당 요청의 고객아이디 중복" )
            , @ApiResponse(code = 500 , message = "서버에러" )
    })
    @RequestMapping(value = "/member/{memId}/dupl", method = {RequestMethod.GET})
    public ResponseEntity registerMember(final @PathVariable(value = "memId") String memId) {



        MemberEntity member = null;
        ResultDTO resDTO =  null;

        boolean isDupl = memberService.isDuplMemId(memId);

        if (isDupl) {
            resDTO = new ResultDTO(MessageConstant.ResponseCodeEnum.MEMBER_ID_DUPLICATED );
        } else {
            resDTO = new ResultDTO();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<ResultDTO> response = new ResponseEntity<ResultDTO>(resDTO,httpHeaders, isDupl ?  HttpStatus.CONFLICT  : HttpStatus.OK);
        return response;
    }

    @ApiOperation(
            value = "유저정보 입력"
            , notes = "유저정보를 저장합니다."
            , httpMethod = "POST"
            , consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memId", value = "고객의 아이디", required = true, dataType = "string",  example = "testId" ),
            @ApiImplicitParam(name = "name", value = "고객의 이름", required = true, dataType = "string",  example = "test"),
            @ApiImplicitParam(name = "nickName", value = "고객의 별명", required = true, dataType = "string",  example = "testnick"),
            @ApiImplicitParam(name = "pwd", value = "고객의 비밀번호 (편의상 평문사용)", required = true, dataType = "string",  example = "Test@123456789"),
            @ApiImplicitParam(name = "phnNum", value = "고객의 전화번호", required = true, dataType = "string",  example = "01091713590"),
            @ApiImplicitParam(name = "email", value = "고객의 이메일", required = true, dataType = "string",  example = "docsuli90@gmail.com"),
            @ApiImplicitParam(name = "sex", value = "고객의 성별", required = false, dataType = "string",  example = "X", defaultValue = "X")
    })
    @ApiResponses({
                @ApiResponse(code = 201 , message = "회원가입 성공" )
              , @ApiResponse(code = 409 , message = "아이디 중복" )
              , @ApiResponse(code = 500 , message = "서버에러" )
    })
    @RequestMapping(value = "/member/join", method = {RequestMethod.POST})
    public ResponseEntity registerMember(final @Valid @RequestBody  MemberDTO param) {



        MemberEntity member = null;
        ResultDTO resDTO =  null;

        if (!memberService.isDuplMemId(param.getMemId())) {
            member = memberService.registerMember(MemberEntity.of(param));
        }

        if ( member == null ) {
            resDTO = new ResultDTO(MessageConstant.ResponseCodeEnum.MEMBER_ID_DUPLICATED );
        } else {
            resDTO = new ResultDTO(MemberDTO.of(member));
        }


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<ResultDTO> response = new ResponseEntity<ResultDTO>(resDTO,httpHeaders, member == null ?  HttpStatus.CONFLICT  : HttpStatus.CREATED);
        return response;
    }

    @ApiOperation(
            value = "단일 회원 상세 정보 조회"
            , notes = "단일 회원의 상세정보를 조회합니다. 조회는 아이디로 합니다."
            , httpMethod = "GET"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memId", value = "고객의 아이디", required = true, dataType = "string",  paramType = "path")
    })
    @ApiResponses({
              @ApiResponse(code = 200 , message = "성공" )
            , @ApiResponse(code = 404 , message = "해당 요청의 고객아이디 없음" )
            , @ApiResponse(code = 500 , message = "서버에러" )
    })
    @RequestMapping(value = "/api/member/{memId}", method = {RequestMethod.GET})
    public ResponseEntity searchMember(final @PathVariable(value = "memId") String memId ) {

        ResultDTO resultDTO = null;
        HttpStatus httpStatus = null;



        MemberEntity member = memberService.searchMember(memId);


        if ( member == null ) {
            resultDTO = new ResultDTO(MessageConstant.ResponseCodeEnum.NOT_FOUND );
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            resultDTO = new ResultDTO(MemberDTO.of(member));
            httpStatus = HttpStatus.OK;
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<ResultDTO> response = new ResponseEntity<ResultDTO>(resultDTO,httpHeaders, httpStatus);

        return response;
    }


    @ApiOperation(
            value = "다수 회원 정보 조회"
            , notes = "다수 회원의 상세정보를 조회합니다."
            , httpMethod = "GET"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "고객 검색 이름", required = false, dataType = "string")
            , @ApiImplicitParam(name = "email", value = "고객 검색 email", required = false, dataType = "string")
            , @ApiImplicitParam(name = "page", value = "페이지", required = false, dataType = "int", defaultValue = "0")
            , @ApiImplicitParam(name = "size", value = "페이지사이즈", required = false, dataType = "int",  defaultValue = "10")
    })
    @ApiResponses({
            @ApiResponse(code = 200 , message = "성공" )
            , @ApiResponse(code = 404 , message = "해당 요청의 고객 없음" )
            , @ApiResponse(code = 500 , message = "서버에러" )
    })
    @RequestMapping(value = "/api/member/list", method = {RequestMethod.GET})
    public ResponseEntity searchMembers( final @RequestParam(value = "name", required = false) String name,
                                         final @RequestParam(value = "email", required = false) String email,
                                         final @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                         final @RequestParam(value = "size" , required = false, defaultValue = "10") int size ) {

        ResultDTO resultDTO = null;
        HttpStatus httpStatus = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);

        Pageable paging = PageRequest.of(page,size);

        Page<MemberEntity> member = memberService.searchMembers(name,email,paging);

        if ( member == null || member.isEmpty()) {
            resultDTO = new ResultDTO(MessageConstant.ResponseCodeEnum.NOT_FOUND );
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            resultDTO = new ResultDTO(MemberDTO.of(member));
            httpStatus = HttpStatus.OK;
        }

        ResponseEntity<ResultDTO> response = new ResponseEntity<ResultDTO>(resultDTO,httpHeaders, httpStatus);

        return response;
    }


    @ApiOperation(
            value = "회원 주문 정보 조회"
            , notes = "회원 주문 정보를 조회합니다."
            , httpMethod = "GET"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memId", value = "고객의 아이디", required = true, dataType = "string",  paramType = "path")
            , @ApiImplicitParam(name = "page", value = "페이지", required = false, dataType = "int", defaultValue = "0")
            , @ApiImplicitParam(name = "size", value = "페이지사이즈", required = false, dataType = "int",  defaultValue = "10")
    })
    @ApiResponses({
            @ApiResponse(code = 200 , message = "성공" )
            , @ApiResponse(code = 404 , message = "해당 요청의 고객아이디 주문정보 없음" )
            , @ApiResponse(code = 500 , message = "서버에러" )
    })
    @RequestMapping(value = "/api/member/{memId}/orders", method = {RequestMethod.GET})
    public ResponseEntity searchMemberOrders(  @PathVariable(value = "memId") final String memId , final @RequestParam(value = "page", required = false, defaultValue = "0") int page, final @RequestParam(value = "size" , required = false, defaultValue = "10") int size ) {

        ResultDTO resultDTO = null;
        HttpStatus httpStatus = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        Pageable paging = PageRequest.of(page,size);


        Page<OrderEntity> orders = memberService.searchMemberOrders(memId ,paging);

        if ( orders == null || orders.isEmpty()) {
            resultDTO = new ResultDTO(MessageConstant.ResponseCodeEnum.NOT_FOUND );
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            resultDTO = new ResultDTO(OrderDTO.of(orders));
            httpStatus = HttpStatus.OK;
        }

        ResponseEntity<ResultDTO> response = new ResponseEntity<ResultDTO>(resultDTO,httpHeaders, httpStatus);

        return response;
    }

}
