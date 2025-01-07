package com.overthinker.cloud.web.handler;

import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.enums.UserEnum.RespEnum;
import com.overthinker.cloud.web.exception.BlackListException;
import com.overthinker.cloud.web.exception.FileUploadException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/20 9:14
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionControllerHandler {

    /**
     * 统一处理参数校验异常
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultData<Void> handlerConstraintViolationException(ConstraintViolationException e){
        log.error("参数校验异常:{}({})", e.getMessage(), e.getStackTrace());
        return ResultData.failure(RespEnum.PARAM_ERROR.getCode(), e.getMessage().split(":")[1]);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultData<Void> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("参数校验异常:{}({})", e.getMessage(), e.getStackTrace());
        BindingResult bindingResult = e.getBindingResult();
        return ResultData.failure(RespEnum.PARAM_ERROR.getCode(), Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    public ResultData<Void> handlerFileUploadException(FileUploadException e){
        log.error("文件上传异常:{}({})", e.getMessage(), e.getStackTrace());
        String bindingResult = e.getMessage();
        return ResultData.failure(RespEnum.FILE_UPLOAD_ERROR.getCode(), bindingResult);
    }

    @ExceptionHandler(BlackListException.class)
    public ResultData<Void> handlerBlackListException(BlackListException e){
        log.error("黑名单异常:{}({})", e.getMessage(), e.getStackTrace());
        return ResultData.failure(RespEnum.BLACK_LIST_ERROR.getCode(), e.getMessage());
    }

    // 最大的异常，防止出现其他不明异常无法处理
//    @ExceptionHandler(Exception.class)
//    public ResultData<Void> handlerException(Exception e){
//        log.error("系统异常:{}，异常:{}",e.getMessage(),e.getStackTrace());
//        return ResultData.failure(RespEnum.OTHER_ERROR.getCode(), e.getMessage());
//    }
}
