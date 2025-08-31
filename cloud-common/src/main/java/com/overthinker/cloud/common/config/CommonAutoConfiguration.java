package com.overthinker.cloud.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 公共模块自动配置类。
 * <p>
 * 当此 starter 被任何微服务依赖时，该配置类会被自动加载。
 * 它通过 @ComponentScan 注解，确保 cloud-common 模块下的所有 Spring 组件
 * (如 @Service, @Component, @RestControllerAdvice) 都能被正确地扫描并注册到 Spring 容器中。
 */
@Configuration
@ComponentScan("com.overthinker.cloud")
public class CommonAutoConfiguration {
}
