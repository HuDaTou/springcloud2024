package com.overthinker.cloud.system.starter.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * 统一的 JWT 认证转换器配置
 * 负责从 JWT Token 中提取用户权限
 *
 * 抽取到公共模块，避免重复代码
 */
@Configuration
public class JwtAuthenticationConverterConfig {

    /**
     * 配置 JWT 认证转换器
     * 从 JWT 的 "authorities" 声明中提取权限，而不是默认的 "scope" 或 "scp"
     * 移除默认的 "SCOPE_" 前缀
     *
     * @return JwtAuthenticationConverter 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
