package com.overthinker.cloud.api.apis.media.ENUM;

import com.overthinker.cloud.api.apis.media.CONSTANTS.MediaFileConst;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * 媒体上传规则枚举
 * <p>
 * 定义文件上传规则，包括文件类型、大小限制、存储路径等
 * 对应数据库表：file_upload_rules
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Getter
@AllArgsConstructor
public enum MediaUploadRuleEnum {

    /**
     * 站长头像上传规则
     */
    WEBSITE_INFO_AVATAR(
            "WEBSITE_INFO_AVATAR",
            Set.of("AVATAR", "PROFILE"),
            Set.of(MediaFileConst.JPG, MediaFileConst.JPEG, MediaFileConst.PNG, MediaFileConst.WEBP),
            10 * 1024,
            "websiteInfo/avatar/",
            "站长头像上传规则",
            true
    ),

    /**
     * 站长背景上传规则
     */
    WEBSITE_INFO_BACKGROUND(
            "WEBSITE_INFO_BACKGROUND",
            Set.of("BACKGROUND", "PROFILE"),
            Set.of(MediaFileConst.JPG, MediaFileConst.JPEG, MediaFileConst.PNG, MediaFileConst.WEBP),
            10 * 1024,
            "websiteInfo/background/",
            "站长背景上传规则",
            true
    ),

    /**
     * 文章封面上传规则
     */
    ARTICLE_COVER(
            "ARTICLE_COVER",
            Set.of("ARTICLE", "COVER"),
            Set.of(MediaFileConst.JPG, MediaFileConst.JPEG, MediaFileConst.PNG, MediaFileConst.WEBP),
            10 * 1024,
            "article/articleCover/",
            "文章封面上传规则",
            true
    ),

    /**
     * 文章图片上传规则
     */
    ARTICLE_IMAGE(
            "ARTICLE_IMAGE",
            Set.of("ARTICLE", "IMAGE"),
            Set.of(MediaFileConst.JPG, MediaFileConst.JPEG, MediaFileConst.PNG, MediaFileConst.GIF, MediaFileConst.WEBP),
            10 * 1024,
            "article/articleImage/",
            "文章图片上传规则",
            true
    ),

    /**
     * 用户头像上传规则
     */
    USER_AVATAR(
            "USER_AVATAR",
            Set.of("AVATAR", "USER"),
            Set.of(MediaFileConst.JPG, MediaFileConst.JPEG, MediaFileConst.PNG, MediaFileConst.WEBP),
            10 * 1024,
            "user/avatar/",
            "用户头像上传规则",
            true
    ),

    /**
     * 视频封面上传规则
     */
    VIDEO_COVER(
            "VIDEO_COVER",
            Set.of("VIDEO", "COVER"),
            Set.of(MediaFileConst.JPG, MediaFileConst.JPEG, MediaFileConst.PNG, MediaFileConst.WEBP),
            50 * 1024,
            "video/cover/",
            "视频封面上传规则",
            true
    ),

    /**
     * 视频文件上传规则（个人视频）
     */
    VIDEO_PRIVATE(
            "VIDEO_PRIVATE",
            Set.of("VIDEO", "PRIVATE"),
            Set.of(MediaFileConst.AVI, MediaFileConst.FLV, MediaFileConst.MP4, MediaFileConst.RM),
            1024 * 1024,
            "video/",
            "个人视频上传规则",
            true
    ),

    /**
     * 视频文件上传规则（公共视频）
     */
    VIDEO_PUBLIC(
            "VIDEO_PUBLIC",
            Set.of("VIDEO", "PUBLIC"),
            Set.of(MediaFileConst.AVI, MediaFileConst.FLV, MediaFileConst.MP4, MediaFileConst.RM),
            1024 * 1024,
            "video/public/",
            "公共视频上传规则",
            true
    ),

    /**
     * 视频文件上传规则（临时视频）
     */
    VIDEO_TEMP(
            "VIDEO_TEMP",
            Set.of("VIDEO", "TEMP"),
            Set.of(MediaFileConst.AVI, MediaFileConst.FLV, MediaFileConst.MP4, MediaFileConst.RM),
            1024 * 1024,
            "video/temp/",
            "临时视频上传规则",
            true
    ),

    /**
     * 前台首页Banner图片上传规则
     */
    UI_BANNERS(
            "UI_BANNERS",
            Set.of("BANNER", "UI"),
            Set.of(MediaFileConst.JPG, MediaFileConst.JPEG, MediaFileConst.PNG, MediaFileConst.WEBP),
            10 * 1024,
            "banners/",
            "前台首页Banner图片上传规则",
            true
    ),

    /**
     * 相册模块图片上传规则
     */
    PHOTO_ALBUM(
            "PHOTO_ALBUM",
            Set.of("PHOTO", "ALBUM"),
            Set.of(MediaFileConst.JPG, MediaFileConst.JPEG, MediaFileConst.PNG, MediaFileConst.WEBP, MediaFileConst.GIF),
            10 * 1024,
            "photoAlbum/",
            "相册模块图片上传规则",
            true
    );

    /**
     * 规则的唯一名称，用于程序中引用
     */
    private final String ruleName;

    /**
     * 文件分类集合，如 AVATAR, PRODUCT_IMAGE, DOCUMENT
     */
    private final Set<String> fileCategory;

    /**
     * 允许的文件扩展名集合。如果为空，则不限制。
     */
    private final Set<String> allowedExtensions;

    /**
     * 单个文件的最大大小（单位：KB）
     */
    private final Integer maxSizeKb;

    /**
     * 文件在对象存储中的存储前缀，用于拼接最终的对象名
     */
    private final String storagePrefix;

    /**
     * 规则的描述信息
     */
    private final String description;

    /**
     * 规则是否生效
     */
    private final Boolean isActive;

    /**
     * 获取最大文件大小（单位：MB）
     *
     * @return 最大文件大小（MB）
     */
    public Double getMaxSizeMb() {
        return maxSizeKb / 1024.0;
    }

    /**
     * 根据规则名称查找枚举
     *
     * @param ruleName 规则名称
     * @return 枚举对象，未找到返回 null
     */
    public static MediaUploadRuleEnum fromRuleName(String ruleName) {
        for (MediaUploadRuleEnum rule : values()) {
            if (rule.getRuleName().equals(ruleName)) {
                return rule;
            }
        }
        return null;
    }

    /**
     * 检查文件扩展名是否允许
     *
     * @param extension 文件扩展名
     * @return 是否允许
     */
    public boolean isExtensionAllowed(String extension) {
        if (allowedExtensions == null || allowedExtensions.isEmpty()) {
            return true;
        }
        return allowedExtensions.contains(extension.toLowerCase());
    }

    /**
     * 检查文件大小是否在限制范围内
     *
     * @param fileSizeKb 文件大小（KB）
     * @return 是否在限制范围内
     */
    public boolean isFileSizeAllowed(Long fileSizeKb) {
        return fileSizeKb <= maxSizeKb;
    }
}