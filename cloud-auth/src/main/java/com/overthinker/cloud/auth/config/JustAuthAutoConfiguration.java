package com.overthinker.cloud.auth.config;

import com.overthinker.cloud.auth.config.properties.JustAuthProperties;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JustAuthProperties.class)
@ConditionalOnProperty(prefix = "justauth", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JustAuthAutoConfiguration {

    private final JustAuthProperties properties;

    public JustAuthAutoConfiguration(JustAuthProperties properties) {
        this.properties = properties;
    }

    public AuthRequest getAuthRequest(String source) {
        switch (source.toLowerCase()) {
            case "gitee":
                return new AuthGiteeRequest(properties.getGitee());
            case "github":
                return new AuthGithubRequest(properties.getGithub());
            case "wechat":
                return new AuthWeChatOpenRequest(properties.getWechat());
            default:
                throw new IllegalArgumentException("Unknown source: " + source);
        }
    }
}
