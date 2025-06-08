package com.overthinker.cloud.handler;

import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.resp.ReturnCodeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(1)
//TODO:这段是什么意思为什么不直接用HttpStatus？
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> exception(Exception e) {

        log.error("全局异常信息处理：{}", e.getMessage(), e);

        return ResultData.failure(ReturnCodeEnum.RC500, e.getMessage());
    }

}
