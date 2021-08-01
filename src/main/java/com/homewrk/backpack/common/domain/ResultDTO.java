package com.homewrk.backpack.common.domain;


import antlr.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.homewrk.backpack.common.constant.MessageConstant;
import lombok.Data;

@Data
public class ResultDTO {

    private String code;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;


    public ResultDTO() {
        init();
    }
    public ResultDTO(Object data) {
        this.data = data;
        init();
    }
    public ResultDTO(Object data, String code) {
        this.data = data;
        this.code = code;
        init();
    }
    public ResultDTO(Object data, String code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
        init();
    }
    public ResultDTO(MessageConstant.ResponseCodeEnum code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
        init();
    }

    protected void init() {
        this.msg = (this.msg == null ?  MessageConstant.ResponseCodeEnum.SUCCESS.getMsg() : this.msg );
        this.code = (this.code == null ? MessageConstant.ResponseCodeEnum.SUCCESS.getCode() : this.code);
    }





}
