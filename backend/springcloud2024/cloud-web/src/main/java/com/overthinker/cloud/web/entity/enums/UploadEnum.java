package com.overthinker.cloud.web.entity.enums;

import com.overthinker.cloud.web.entity.constants.ImageConst;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * @author overH
 * <p>
 * 创建时间：2023/12/27 14:20
 * 文件上传枚举
 */
@Getter
@AllArgsConstructor
public enum UploadEnum {

    // 站长头像
    WEBSITE_INFO_AVATAR("websiteInfo/avatar/", "站长头像", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 10.0),
    // 站长背景
    WEBSITE_INFO_BACKGROUND("websiteInfo/background/", "站长背景", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 10.0),
    // 文章封面
    ARTICLE_COVER("article/articleCover/", "文章封面", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 10.0),
    // 文章图片
    ARTICLE_IMAGE("article/articleImage/", "文章图片", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.GIF, ImageConst.WEBP), 10.0),
    // 用户头像
    USER_AVATAR("user/avatar/", "用户头像", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 10.0),


    VEDIO_COVER("video/cover/", "视频封面", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 10.0),




    // 前台首页Banners图片
    UI_BANNERS("banners/", "前台首页Banners图片", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 10.0),
    // 相册模块图片
    PHOTO_ALBUM("photoAlbum/", "相册模块图片", Set.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP, ImageConst.GIF), 10.0);






    // 上传目录
    private final String dir;

    // 描述
    private final String description;

    // 支持的格式
    private final Set<String> format;

    // 文件最大大小 单位：MB
    private final Double limitSize;
}
