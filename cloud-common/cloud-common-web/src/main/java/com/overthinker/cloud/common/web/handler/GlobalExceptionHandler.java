package com.overthinker.cloud.common.web.handler;

import com.overthinker.cloud.common.core.exception.BusinessException;
import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.common.core.resp.ReturnCodeEnum;


import com.overthinker.cloud.common.web.exceptions.exceptions.FileUploadException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Order(1)
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> exception(Exception e) {

        log.error("全局异常信息处理：{}", e.getMessage(), e);

        return ResultData.failure(ReturnCodeEnum.INTERNAL_SERVER_ERROR, e.getMessage());
    }
    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<Object> handleFileUploadException(FileUploadException e) {
        log.error("文件上传异常：{}", e.getMessage(), e);
        return ResultData.failure(e.getReturnCodeEnum(),e.getData());
    }

    @ExceptionHandler(BusinessException.class)
    public ResultData<Object> handleBusinessException(BusinessException e) {
        ReturnCodeEnum returnCodeEnum = e.getReturnCodeEnum();
        if (returnCodeEnum == null) {
            returnCodeEnum = ReturnCodeEnum.INTERNAL_SERVER_ERROR;
        }

        log.warn("业务异常：{}", e.getMessage());
        return ResultData.failure(returnCodeEnum,e.getMessage());
    }


}
