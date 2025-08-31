package com.overthinker.cloud.web.exception;

import com.overthinker.cloud.common.resp.ReturnCodeEnum;
import lombok.Getter;

/**
 * @Title: BusinessException
 * @Author overthinker
 * @Package com.overthinker.cloud.web.exception
 * @Date 2024/11/25 23:00
 * @description: 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {
    private final ReturnCodeEnum returnCodeEnum;

    public BusinessException(ReturnCodeEnum returnCodeEnum) {
        super(returnCodeEnum.getMsg());
        this.returnCodeEnum = returnCodeEnum;
    }


}
