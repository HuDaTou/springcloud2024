package com.overthinker.cloud.resp;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 通用结果类型
 * @param <T>
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ResultData<T> {
    private Integer code;
    private String msg;
    private T data;
    private long timestamp;

    public ResultData() {
        this.timestamp = System.currentTimeMillis();
    }



    /**
     * 成功响应，不需要返回数据
     */
    public static <T> ResultData<T> success() {
        return ResultData.<T>builder().code(ReturnCodeEnum.RC200.getCode()).msg(ReturnCodeEnum.RC200.getMessage()).data(null).build();
    }


    public static <T> ResultData<T> success(T data) {
        return ResultData.<T>builder().code(ReturnCodeEnum.RC200.getCode()).msg(ReturnCodeEnum.RC200.getMessage()).data(data).build();
    }
    public static <T> ResultData<T> success(T data , String message) {
        return ResultData.<T>builder().code(ReturnCodeEnum.RC200.getCode()).msg(message).data(data).build();
    }




    /**
     * 失败响应，不需要返回数据
     */
    public static <T> ResultData<T> failure() {
        return ResultData.<T>builder().code(ReturnCodeEnum.FAILURE.getCode()).msg(ReturnCodeEnum.FAILURE.getMessage()).data(null).build();
    }
    public static <T> ResultData<T> failure(String msg) {
        return ResultData.<T>builder().code(ReturnCodeEnum.FAILURE.getCode()).msg(msg).data(null).build();
    }
    public static <T> ResultData<T> failure(T data)  {
        return ResultData.<T>builder().code(ReturnCodeEnum.FAILURE.getCode()).msg(ReturnCodeEnum.FAILURE.getMessage()).data(data).build();
    }

    public static <T> ResultData<T> failure(Integer code, String msg) {
        return ResultData.<T>builder().code(code).msg(msg).data(null).build();
    }

    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
