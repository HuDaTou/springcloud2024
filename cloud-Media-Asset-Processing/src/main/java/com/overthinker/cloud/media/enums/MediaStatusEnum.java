package com.overthinker.cloud.media.enums;

import lombok.Getter;

/**
 * 媒体资产状态枚举
 * <p>
 * 定义媒体资产在上传和处理过程中的各种状态
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Getter
public enum MediaStatusEnum {

    /**
     * 上传中
     */
    UPLOADING("UPLOADING", "上传中"),

    /**
     * 已上传
     */
    UPLOADED("UPLOADED", "已上传"),

    /**
     * 上传失败
     */
    FAILED("FAILED", "上传失败"),

    /**
     * 处理中（缩略图、转码等）
     */
    PROCESSING("PROCESSING", "处理中"),

    /**
     * 已处理完成
     */
    PROCESSED("PROCESSED", "已处理完成");

    private final String code;
    private final String desc;

    MediaStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MediaStatusEnum fromCode(String code) {
        for (MediaStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的媒体状态码: " + code);
    }
}
