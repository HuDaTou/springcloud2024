package com.overthinker.cloud.web.exception;

import com.overthinker.cloud.resp.ReturnCodeEnum;
import lombok.Getter;

@Getter
public class FileUploadException extends RuntimeException {
    private final ReturnCodeEnum returnCodeEnum;

    public FileUploadException(String message) {
        super(message);
        this.returnCodeEnum = ReturnCodeEnum.FILE_UPLOAD_ERROR;
    }

    public FileUploadException(String message, ReturnCodeEnum returnCodeEnum) {
        super(message);
        this.returnCodeEnum = returnCodeEnum;
    }

    public FileUploadException(ReturnCodeEnum returnCodeEnum) {
        super(returnCodeEnum.getMsg());
        this.returnCodeEnum = returnCodeEnum;
    }
}
