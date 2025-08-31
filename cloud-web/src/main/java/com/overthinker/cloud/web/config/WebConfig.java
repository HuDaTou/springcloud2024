package com.overthinker.cloud.web.config;

import com.overthinker.cloud.web.interceptor.AccessLimitInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author overH
 * <p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource(name = "accessLimitInterceptor")
    private AccessLimitInterceptor accessLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // redis限流拦截器
//        registry.addInterceptor(accessLimitInterceptor).addPathPatterns("/**");

    }
}
