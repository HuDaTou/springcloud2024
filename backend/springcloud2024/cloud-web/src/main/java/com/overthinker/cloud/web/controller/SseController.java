package com.overthinker.cloud.web.controller;


import com.overthinker.cloud.controller.base.BaseController;
import com.overthinker.cloud.web.entity.VO.SseDataVO;
import com.overthinker.cloud.web.service.ArticleService;
import com.overthinker.cloud.web.service.PhotoService;
import com.overthinker.cloud.web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@Tag(name = "大屏监控")
@RequestMapping(value = "/sse")
@Slf4j
public class SseController extends BaseController {
//    数据量
    private final static Map<String ,SseEmitter> SSE_DATA = new ConcurrentHashMap<>();

    //    在线人数
    private final static Map<String,SseEmitter> SSE_COUNT = new  ConcurrentHashMap<>();

    private final static SseDataVO sseDataVO = new SseDataVO();

    @Resource
    private ArticleService articleService;

    @Resource
    private UserService userService;

    @Resource
    private PhotoService photoService;

    @CrossOrigin(origins = "*", maxAge = 3600)
//    @PreAuthorize("hasAnyAuthority('monitor:server:list')")
    @Operation(summary = "SSE获取服务监控数据")
    @GetMapping(value = "/data")
    public SseEmitter getSseEmitter()  {
        SseEmitter emitter = new SseEmitter(0L); // 设置30秒超时
//        为每个连接生成一个uuid
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        if (!SSE_DATA.containsKey(uuid)) {
            SSE_DATA.put(uuid, emitter);
        }
        emitter.onTimeout(() -> {
            emitter.complete();
            log.info("SSE连接超时");
        });
        emitter.onError((e) -> {
            emitter.complete();
            log.info("SSE连接异常: {}", e.getMessage());
            SSE_DATA.remove(uuid);

        });
        emitter.onCompletion(() -> {
            log.info("SSE连接正常关闭");
            SSE_DATA.remove(uuid);
        });
        return emitter;
    }


    @CrossOrigin(origins = "*", maxAge = 3600)
//    @PreAuthorize("hasAnyAuthority('monitor:server:list')")
    @Operation(summary = "SSE统计在线人数")
    @GetMapping(value = "/user/count")
    public SseEmitter getCountEmitter()  {
        SseEmitter emitter = new SseEmitter(0L); // 设置30秒超时
//        为每个连接生成一个uuid
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        if (!SSE_COUNT.containsKey(uuid)) {
            SSE_COUNT.put(uuid, emitter);
        }
        emitter.onTimeout(() -> {
            emitter.complete();
            log.info("SSEUSERCOUNT连接超时");
        });
        emitter.onError((e) -> {
            emitter.complete();
            log.info("SSEUSERCOUNT: {}", e.getMessage());
            SSE_COUNT.remove(uuid);

        });
        emitter.onCompletion(() -> {
            log.info("SSE连接正常关闭");
            SSE_COUNT.remove(uuid);
        });
        return emitter;
    }


    @Scheduled(fixedDelay = 3, initialDelay = 1,timeUnit = TimeUnit.SECONDS)
    private void job()  {

        if (!SSE_DATA.isEmpty()) {
//            sseDataVO.setOnlineCount(SSE_COUNT.size());
//            sseDataVO.setArticleCount(articleService.count());
//            sseDataVO.setUserCount(userService.count());
//            sseDataVO.setPhotoCount(photoService.count());
            sseDataVO.setOnlineCount(1);
            sseDataVO.setArticleCount(1L);
            sseDataVO.setUserCount(1L);
            sseDataVO.setPhotoCount(1L);
            SSE_DATA.forEach((key, emitter) -> {
                try {
//                    log.info("SSE推送数据: {}", sseDataVO);

                    emitter.send(SseEmitter.event()
                            .id(key)
                            .data(sseDataVO)
                    );
                } catch (IOException e) {
                    emitter.completeWithError(e);
                    SSE_DATA.remove(key);

                }
            });

        }
    }
    @Scheduled(fixedDelay = 3, initialDelay = 1,timeUnit = TimeUnit.SECONDS)
    private void jobCount()  {

        if (!SSE_COUNT.isEmpty()) {
            SSE_COUNT.forEach((key, emitter) -> {
                try {
//                    log.info("SSEUSERCOUNT推送数据: {}", key);

                    emitter.send(SseEmitter.event()
                            .id(key)
                    );
                } catch (IOException e) {
                    SSE_COUNT.remove(key);
                    log.info("已经删除：{}",key);
                    emitter.completeWithError(e);



                }
            });

        }
    }

}
