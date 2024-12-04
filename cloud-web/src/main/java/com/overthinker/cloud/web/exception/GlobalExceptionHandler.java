package com.overthinker.cloud.web.exception;

import com.overthinker.cloud.resp.ResultData;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

/**
 * @Title: GlobalExceptionHandler
 * @Author overthinker
 * @Package com.overthinker.cloud.web.exception
 * @Date 2024/11/24 18:07
 * @description: 全局异常处理器
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResultData<String>> handleBusiness(BusinessException e, HandlerMethod handlerMethod) {
        // BusinessException（自定义业务异常）的处理逻辑，比如：记录日志等逻辑。
        ResultData<String> resultData =  ResultData.fail(e.getUserMessage(), e.getErrorCode(), e.getErrorMessage());


        return new ResponseEntity<>(resultData ,e.getHttpStatusCode());

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultData<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HandlerMethod handlerMethod) {

        return new  ResponseEntity<>(ResultData.fail(e.getMessage()),HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResultData<String>> handleGoneException(CustomException e) {

        return new ResponseEntity<ResultData<String>>(ResultData.fail(e.getStatusCode().toString(),e.getMessage()), e.getStatusCode());
    }




}
