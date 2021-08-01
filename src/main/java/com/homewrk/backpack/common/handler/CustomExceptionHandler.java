package com.homewrk.backpack.common.handler;


import com.homewrk.backpack.common.constant.MessageConstant;
import com.homewrk.backpack.common.domain.ResultDTO;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;
import java.util.HashMap;
import java.util.Map;

/**
 * Exception Handler
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler {


    /**
     * 404 에러 처리
     * @param request
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler( {NoHandlerFoundException.class })
    public ResultDTO handleNotFoundException(HttpServletRequest request, Exception e) {
        return new ResultDTO(MessageConstant.ResponseCodeEnum.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler( {  MethodArgumentNotValidException.class})
    public ResultDTO handleVallidException(HttpServletRequest request,MethodArgumentNotValidException  e) {
        ResultDTO res =  new ResultDTO(MessageConstant.ResponseCodeEnum.BAD_REQUEST);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        res.setData(errors);
        return res;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler( { HttpMediaTypeNotSupportedException.class
            , HttpRequestMethodNotSupportedException.class
            , HttpMessageNotReadableException.class
            , BindException.class
            , MethodArgumentTypeMismatchException.class

    })
    public ResultDTO handleBadRequestException(HttpServletRequest request,Exception e) {
        return new ResultDTO(MessageConstant.ResponseCodeEnum.BAD_REQUEST);
    }

}
