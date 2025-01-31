package com.overthinker.cloud.web.utils;



import com.overthinker.cloud.resp.ResultData;

import java.util.function.Supplier;

/**
 * @author overH
 * <p>
 * 创建时间：2023/10/30 9:52
 */
public class ControllerUtils {
    public static  <T> ResultData<T> messageHandler(Supplier<T> supplier) {
        return ResultData.success(supplier.get());
    }
}
