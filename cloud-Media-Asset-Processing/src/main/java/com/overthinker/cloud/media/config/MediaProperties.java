package com.overthinker.cloud.media.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 媒体服务相关配置属性。
 * <p>
 * 用于从 application.yml 文件中读取文件上传限制和定时任务相关的配置。
 */
@Data
@Component
@ConfigurationProperties(prefix = "media.upload")
public class MediaProperties {

    /**
     * 允许上传的最大文件大小，单位为字节。
     * 默认值为 524288000 (500MB)。
     */
    private long maxFileSize = 524288000L;

    /**
     * 允许上传的MIME类型列表。
     * 默认允许 "image/jpeg", "image/png", "video/mp4"。
     */
    private List<String> allowedContentTypes = List.of("image/jpeg", "image/png", "video/mp4");

    /**
     * 清理任务的配置。
     */
    private Cleanup cleanup = new Cleanup();

    @Data
    public static class Cleanup {
        /**
         * 是否启用清理过期分片任务的定时任务。
         * 默认为 true。
         */
        private boolean enabled = true;

        /**
         * 定义上传任务被视为过期的阈值，单位为小时。
         * 状态为 UPLOADING 且创建时间超过此阈值的记录将被清理。
         * 默认为 24 小时。
         */
        private int staleThresholdHours = 24;
    }
}
