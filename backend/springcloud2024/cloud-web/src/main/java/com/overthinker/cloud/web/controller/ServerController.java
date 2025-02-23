package com.overthinker.cloud.web.controller;


import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.annotation.AccessLimit;
import com.overthinker.cloud.web.controller.base.BaseController;
import com.overthinker.cloud.web.entity.PO.Server;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author overH
 * <p>
 * 创建时间：2023/12/14 15:12
 */
@RestController
@Tag(name = "服务监控")
@RequestMapping(value = "/monitor/server")
@Slf4j
public class ServerController extends BaseController
{

    private final static Server server = new Server();



    @PreAuthorize("hasAnyAuthority('monitor:server:list')")
    @AccessLimit(seconds = 60, maxCount = 30)
    @Operation(summary = "获取服务监控数据")
    @GetMapping()
    public ResultData<Server> getInfo() throws Exception
    {

        server.copyTo();
        return messageHandler(() -> server);
    }

    private final static Map<String ,SseEmitter> SSE_CACHE = new  ConcurrentHashMap<>();





//    @CrossOrigin(origins = "*", maxAge = 3600)
////    @PreAuthorize("hasAnyAuthority('monitor:server:list')")
//    @Operation(summary = "SSE获取服务监控数据")
//    @GetMapping(value = "/sse")
//    public SseEmitter getSseEmitter()  {
//        SseEmitter emitter = new SseEmitter(30_000L); // 设置30秒超时
////        为每个连接生成一个uuid
//        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
//        if (!SSE_CACHE.containsKey(uuid)) {
//            SSE_CACHE.put(uuid, emitter);
//        }
//        emitter.onTimeout(() -> {
//            emitter.complete();
//            log.info("SSE连接超时");
//        });
//        emitter.onError((e) -> {
//            emitter.complete();
//            log.info("SSE连接异常: {}", e.getMessage());
//            SSE_CACHE.remove(uuid);
//
//        });
//        emitter.onCompletion(() -> {
//            log.info("SSE连接正常关闭");
//            SSE_CACHE.remove(uuid);
//        });
//        return emitter;
//    }


//    @Scheduled(fixedDelay = 3, initialDelay = 1,timeUnit = TimeUnit.SECONDS)
//    private void job()  {
//
//        try {
//            server.copyTo();
//        } catch (Exception e) {
//            log.error("监控数据采集失败: {}", e.getMessage());
//            throw new ServerException(ReturnCodeEnum.SERVER_SSE_MONITORING_DATA_COLLECTION_FAILED);
//        }
//
//        SSE_CACHE.forEach((key, emitter) -> {
//            try {
//                log.info("SSE推送数据: {}", server);
////                ResultData<Server> result = messageHandler(() -> server);
//                emitter.send(server);
//            } catch (IOException e) {
//                log.error("SSE推送失败: {}", e.getMessage());
//                emitter.completeWithError(e);
//                SSE_CACHE.remove(key);
//                throw new ServerException(ReturnCodeEnum.SERVER_SSE_PUSH_ERROR);
//            }
//            发送当前在线人数

//            try {
//                log.info("SSE推送数据: {}", SSE_CACHE.size());
//                emitter.send(SseEmitter.event()
//                        .id(key)
//                        .data(SSE_CACHE.size()));
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//
//            }
//        });







//    }


}
