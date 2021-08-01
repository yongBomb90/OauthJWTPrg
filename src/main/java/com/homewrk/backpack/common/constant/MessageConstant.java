package com.homewrk.backpack.common.constant;

import java.util.Arrays;

public class MessageConstant {


    public enum ResponseCodeEnum {

        SUCCESS("1", "SUCCESS"),
        BAD_REQUEST("9000", "BAD_REQUEST"),
        SERVER_ERROR("9500", "SERVER_ERROR"),
        NOT_FOUND("9400", "NOT_FOUND"),

        MEMBER_ID_AVAILABLE("9200", "사용 가능한 아이디 입니다."),
        MEMBER_ID_DUPLICATED("0409", "해당 아이디는 이미 가입되어 있습니다."),
        MEMBER_ID_NOT_FOUND("9202", "아이디를 찾을 수 없습니다."),

        NOT_EXIST_RESPONSE("9999", "NOT_FOUNT");


        private String code;
        private String msg;


        ResponseCodeEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public static String findDescByCode(String code) {
            return Arrays.stream(values()).
                    filter(responseEnum -> responseEnum.getCode().equals(code)).findAny().
                    orElse(NOT_EXIST_RESPONSE).getMsg();
        }


    }


}
