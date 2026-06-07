package com.overthinker.cloud.common.core.resp;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 通用结果类型
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResultData<T> {
    private String code;
    private String msg;
    private T data;

    private long timestamp = System.currentTimeMillis();

    /**
     * 成功响应，不需要返回数据
     */
    public static <T> ResultData<T> success() {
        return new ResultData<T>()
                .setCode(ReturnCodeEnum.SUCCESS.getCode())
                .setMsg(ReturnCodeEnum.SUCCESS.getMsg())
                .setData(null);
    }


    public static <T> ResultData<T> success(T data) {
        return new ResultData<T>()
                .setCode(ReturnCodeEnum.SUCCESS.getCode())
                .setMsg(ReturnCodeEnum.SUCCESS.getMsg())
                .setData(data);
    }

    public static <T> ResultData<T> success(T data, String message) {
        return new ResultData<T>()
                .setCode(ReturnCodeEnum.SUCCESS.getCode())
                .setMsg(message)
                .setData(data);
    }


    /**
     * 失败响应，不需要返回数据
     */
    public static <T> ResultData<T> failure() {
        return new ResultData<T>()
                .setCode(ReturnCodeEnum.FAILURE.getCode())
                .setMsg(ReturnCodeEnum.FAILURE.getMsg())
                .setData(null);
    }

    public static <T> ResultData<T> failure(String msg) {
        return new ResultData<T>()
                .setCode(ReturnCodeEnum.FAILURE.getCode())
                .setMsg(msg)
                .setData(null);
    }

    public static <T> ResultData<T> failure(T data) {
        return new ResultData<T>()
                .setCode(ReturnCodeEnum.FAILURE.getCode())
                .setMsg(ReturnCodeEnum.FAILURE.getMsg())
                .setData(data);
    }

    public static <T> ResultData<T> failure(String code, String msg) {
        return new ResultData<T>()
                .setCode(code)
                .setMsg(msg)
                .setData(null);
    }

    public static <T> ResultData<T> failure(String code, String msg, T data) {
        return new ResultData<T>()
                .setCode(code)
                .setMsg(msg)
                .setData(data);
    }

    public static <T> ResultData<T> failure(ReturnCodeEnum returnCodeEnum) {
        return new ResultData<T>()
                .setCode(returnCodeEnum.getCode())
                .setMsg(returnCodeEnum.getMsg())
                .setData(null);
    }

    public static <T> ResultData<T> failure(ReturnCodeEnum returnCodeEnum, T data) {
        return new ResultData<T>()
                .setCode(returnCodeEnum.getCode())
                .setMsg(returnCodeEnum.getMsg())
                .setData(data);
    }

    public static <T> ResultData<T> failure(ReturnCodeEnum returnCodeEnum, String msg) {
        if (msg == null) {
            msg = returnCodeEnum.getMsg();
        }
        return new ResultData<T>()
                .setCode(returnCodeEnum.getCode())
                .setMsg(msg)
                .setData(null);
    }

        public static <T> ResultData<T> failure(ReturnCodeEnum returnCodeEnum, T data,String msg) {
        if (msg == null) {
            msg = returnCodeEnum.getMsg();
        }
        return new ResultData<T>()
                .setCode(returnCodeEnum.getCode())
                .setMsg(msg)
                .setData(data);
    }




    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
