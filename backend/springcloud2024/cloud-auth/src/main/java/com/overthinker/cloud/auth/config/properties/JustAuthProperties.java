package com.overthinker.cloud.auth.config.properties;

import lombok.Data;
import me.zhyd.oauth.config.AuthConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "justauth")
public class JustAuthProperties {

    private boolean enabled;
    private AuthConfig gitee;
    private AuthConfig github;
    private AuthConfig wechat;

}
