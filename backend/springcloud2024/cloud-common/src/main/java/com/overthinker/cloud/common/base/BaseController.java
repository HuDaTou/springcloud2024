package com.overthinker.cloud.common.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.overthinker.cloud.common.resp.ResultData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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

    // 处理 Mono<T>
    public static <T> Mono<ResultData<T>> messageHandler(Mono<T> mono) {
        return mono.map(ResultData::success)
                .onErrorResume(e -> Mono.just(ResultData.failure("请求失败: " + e.getMessage())));
    }

    // 处理 Flux<T> → List<T>
    public static <T> Mono<ResultData<List<T>>> messageHandler(Flux<T> flux) {
        return flux.collectList()
                .map(ResultData::success)
                .onErrorResume(e -> Mono.just(ResultData.failure("流式请求失败: " + e.getMessage())));
    }


    public static <T> Page pageBuilder(String pageNum, String pageSize) {
        return new Page<>(Long.parseLong(pageNum), Long.parseLong(pageSize));
    }



}
