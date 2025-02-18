package com.overthinker.cloud.web.exception;

import com.overthinker.cloud.resp.ReturnCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServerException extends RuntimeException {


    private final ReturnCodeEnum returnCodeEnum;

    public ServerException(ReturnCodeEnum returnCodeEnum) {
        super(returnCodeEnum.getMsg());
        this.returnCodeEnum = returnCodeEnum;
    }
}
