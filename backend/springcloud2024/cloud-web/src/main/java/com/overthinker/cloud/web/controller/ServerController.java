package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.resp.ReturnCodeEnum;
import com.overthinker.cloud.web.annotation.AccessLimit;
import com.overthinker.cloud.web.controller.base.BaseController;
import com.overthinker.cloud.web.entity.PO.Server;
import com.overthinker.cloud.web.exception.ServerException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


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
//    @PreAuthorize("hasAnyAuthority('monitor:server:list')")
    @Operation(summary = "SSE获取服务监控数据")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE ,value = "/sse")
    public SseEmitter getSseEmitter(HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setHeader("Content-Type", "text/event-stream");
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setHeader("Connection", "keep-alive");
        httpServletResponse.setCharacterEncoding("UTF-8");

        SseEmitter emitter = new SseEmitter(30_000L); // 设置30秒超时

//        为每个连接生成一个uuid
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        if (!SSE_CACHE.containsKey(uuid)) {
            SSE_CACHE.put(uuid, emitter);
        }


        emitter.onTimeout(() -> {
            emitter.complete();
            log.info("SSE连接超时");

        });
        emitter.onCompletion(() -> {
            log.info("SSE连接正常关闭");
        });


        return emitter;
    }
    @Scheduled(fixedDelay = 3, initialDelay = 1,timeUnit = TimeUnit.SECONDS)
    private void job()  {
        for (Map.Entry<String, SseEmitter> entry : SSE_CACHE.entrySet()) {
            SseEmitter sseEmitter = SSE_CACHE.get(entry.getKey());
            try {
                server.copyTo();
                ResultData<Server> result = messageHandler(() -> server);
                sseEmitter.send(result);
            } catch (IOException e) {
                log.error("SSE推送失败: {}", e.getMessage());
                sseEmitter.completeWithError(e);
                throw new ServerException(ReturnCodeEnum.SERVER_SSE_PUSH_ERROR);
            } catch (Exception e) {
                log.error("监控数据采集失败: {}", e.getMessage());
                sseEmitter.completeWithError(e);
                throw new ServerException(ReturnCodeEnum.SERVER_SSE_MONITORING_DATA_COLLECTION_FAILED);
            }
        }
    }


}
