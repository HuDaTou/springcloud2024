package com.overthinker.cloud.web.handler;

import com.overthinker.cloud.common.core.resp.ResultData;
import com.overthinker.cloud.common.resp.ReturnCodeEnum;
import com.overthinker.cloud.web.exception.BusinessException;
import com.overthinker.cloud.web.exception.FileUploadException;
import com.overthinker.cloud.web.exception.ServerException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionControllerHandler {

    /**
     * 统一处理参数校验异常
     *
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultData<Void> handlerConstraintViolationException(ConstraintViolationException e) {
        log.error("参数校验异常:{}({})", e.getMessage(), e.getStackTrace());
        return ResultData.failure(ReturnCodeEnum.PARAM_ERROR.getCode(), e.getMessage().split(":")[1]);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultData<Void> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数校验异常:{}({})", e.getMessage(), e.getStackTrace());
        BindingResult bindingResult = e.getBindingResult();
        return ResultData.failure(ReturnCodeEnum.PARAM_ERROR.getCode(), Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    public ResultData<Void> handlerFileUploadException(FileUploadException e) {
        log.error("文件上传异常:{}({})", e.getMessage(), e.getStackTrace());
        String bindingResult = e.getMessage();

        return ResultData.failure(ReturnCodeEnum.FILE_UPLOAD_ERROR.getCode(), bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    public ResultData<Void> handlerBusinessException(BusinessException e) {
        log.error("业务异常:{}({})", e.getMessage(), e.getStackTrace());
        return ResultData.failure(e.getReturnCodeEnum());
    }



    @ExceptionHandler(ServerException.class)
    public ResultData<Void> handlerServerException(ServerException e) {
        log.error("服务器监控异常:{}({})", e.getMessage(), e.getStackTrace());
        return ResultData.failure(e.getReturnCodeEnum().getCode(), e.getMessage());
    }


}
