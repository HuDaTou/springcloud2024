package com.overthinker.cloud.resp;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 通用结果类型
 * @param <T>
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMsg(ReturnCodeEnum.RC200.getMessage());
        resultData.setData(null);
        return resultData;
    }


    public static <T> ResultData<T> success(T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMsg(ReturnCodeEnum.RC200.getMessage());
        resultData.setData(data);
        return resultData;
    }
    public static <T> ResultData<T> success(T data , String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMsg(message);
        resultData.setData(data);
        return resultData;
    }




    /**
     * 失败响应，不需要返回数据
     */
    public static <T> ResultData<T> failure() {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.FAILURE.getCode());
        resultData.setMsg(ReturnCodeEnum.FAILURE.getMessage());
        resultData.setData(null);
        return resultData;
    }
    public static <T> ResultData<T> failure(String msg) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.FAILURE.getCode());
        resultData.setMsg(msg);
        resultData.setData(null);
        return resultData;
    }
    public static <T> ResultData<T> failure(T data)  {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.FAILURE.getCode());
        resultData.setMsg(ReturnCodeEnum.FAILURE.getMessage());
        resultData.setData(data);
        return resultData;
    }

    public static <T> ResultData<T> failure(Integer code, String msg) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(code);
        resultData.setMsg(msg);
        resultData.setData(null);
        return resultData;
    }

    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
