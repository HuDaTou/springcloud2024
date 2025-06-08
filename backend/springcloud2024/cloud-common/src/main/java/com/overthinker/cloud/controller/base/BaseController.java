package com.overthinker.cloud.controller.base;

import com.overthinker.cloud.resp.ResultData;

import java.util.function.Supplier;

/**
 * @Title: ABaseController
 * @Author overthinker
 * @Package com.overthinker.cloud.web.controller
 * @Date 2024/11/21 下午9:35
 * @description: 公共接口
 */
//TODO 配置基础统一
public class BaseController {

    public static <T> ResultData<T> messageHandler(Supplier<T> supplier) {
        return ResultData.success(supplier.get());
    }

}
