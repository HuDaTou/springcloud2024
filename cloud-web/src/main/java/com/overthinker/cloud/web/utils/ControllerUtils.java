package com.overthinker.cloud.web.utils;

import com.overthinker.cloud.resp.ResultData;

import java.util.function.Supplier;

public class ControllerUtils {
    public static  <T> ResultData<T> messageHandler(Supplier<T> supplier) {
        return ResultData.success(supplier.get());
    }
}
