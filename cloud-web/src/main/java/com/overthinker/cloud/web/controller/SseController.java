package com.overthinker.cloud.web.controller;

import com.overthinker.cloud.common.core.base.BaseController;
import com.overthinker.cloud.web.entity.VO.SseDataVO;
import com.overthinker.cloud.web.service.ArticleService;
import com.overthinker.cloud.web.service.PhotoService;
import com.overthinker.cloud.web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

@RestController
@Tag(name = "大屏监控")
@RequestMapping(value = "/sse")
@RequiredArgsConstructor
@Slf4j
public class SseController extends BaseController {

    private final ArticleService articleService;
    private final UserService userService;
    private final PhotoService photoService;

    // 缓存数据的 Flux
    private final Flux<SseDataVO> dataFlux = Flux.interval(Duration.ofSeconds(3))
            .map(seq -> {
                SseDataVO vo = new SseDataVO();
                vo.setOnlineCount(1);
                vo.setArticleCount(1L);
                vo.setUserCount(1L);
                vo.setPhotoCount(1L);
                return vo;
            })
            .share(); // 使用 share() 实现多播

    // 在线人数的 Flux
    private final Flux<String> countFlux = Flux.interval(Duration.ofSeconds(3))
            .map(seq -> UUID.randomUUID().toString().replaceAll("-", ""))
            .share();

    @CrossOrigin(origins = "*", maxAge = 3600)
    @Operation(summary = "SSE获取服务监控数据")
    @GetMapping(value = "/data", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<SseDataVO>> streamData() {
        return dataFlux.map(data -> ServerSentEvent.<SseDataVO>builder()
                        .id(UUID.randomUUID().toString())
                        .data(data)
                        .build())
                .doOnSubscribe(subscription -> log.info("Data subscription started"))
                .doOnTerminate(() -> log.info("Data subscription terminated"));
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @Operation(summary = "SSE统计在线人数")
    @GetMapping(value = "/user/count", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamCount() {
        return countFlux.map(id -> ServerSentEvent.<String>builder()
                        .id(id)
                        .build())
                .doOnSubscribe(subscription -> log.info("Count subscription started"))
                .doOnTerminate(() -> log.info("Count subscription terminated"));
    }
}