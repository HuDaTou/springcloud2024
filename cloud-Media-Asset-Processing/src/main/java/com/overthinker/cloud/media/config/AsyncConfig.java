package com.overthinker.cloud.media.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Spring 异步任务执行器配置。
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * 定义一个名为 "taskExecutor" 的线程池，用于执行 @Async 任务。
     *
     * @return 线程池执行器
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);       // 核心线程数
        executor.setMaxPoolSize(10);      // 最大线程数
        executor.setQueueCapacity(25);     // 任务队列容量
        executor.setThreadNamePrefix("MediaProcess-"); // 线程名前缀
        executor.initialize();
        return executor;
    }
}
