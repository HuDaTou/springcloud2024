package com.overthinker.cloud.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通用结果类型
 * @param <T>
 */
@Data
@Accessors(chain = true)
public class ResultData<T> {
    private String code;
    private String message;
    private T data;
    private long timestamp;

    public ResultData() {
        this.timestamp = System.currentTimeMillis();
    }



    /**
     * 成功响应，不需要返回数据
     */
    public static <T> ResultData<T> success() {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMessage(ReturnCodeEnum.RC200.getMessage());
        resultData.setData(null);
        return resultData;
    }


    public static <T> ResultData<T> success(T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMessage(ReturnCodeEnum.RC200.getMessage());
        resultData.setData(data);
        return resultData;
    }
    public static <T> ResultData<T> success(T data , String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMessage(message);
        resultData.setData(data);
        return resultData;
    }

    /**
     * 异常报错
     * @param code
     * @param message
     * @return
     * @param <T>
     */
    public static <T> ResultData<T> failure(String code, String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(code);
        resultData.setMessage(message);
        resultData.setData(null);
        return resultData;
    }

    /**
     * 异常报错
     * @param message
     * @return
     * @param <T>
     */
    public static <T> ResultData<T> fail(String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.RC500.getCode());
        resultData.setMessage(message);
        resultData.setData(null);
        return resultData;
    }

    /**
     * 异常报错
     *
     * @param code
     * @param message
     * @return
     * @param <T>
     */
    public static <T> ResultData<T> fail( String code, String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(code);

        resultData.setData(null);
        return resultData;

    }

    public static ResultData<Void> failure(Integer code, String defaultMessage) {
        return null;
    }
}
