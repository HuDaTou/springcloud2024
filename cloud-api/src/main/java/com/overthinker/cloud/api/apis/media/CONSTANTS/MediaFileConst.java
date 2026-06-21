package com.overthinker.cloud.api.apis.media.CONSTANTS;

/**
 * 媒体文件格式常量类
 * <p>
 * 定义图片和视频文件的格式常量
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
public class MediaFileConst {

    /**
     * 图片格式常量
     */
    public static final String JPG = "jpg";
    public static final String JPEG = "jpeg";
    public static final String PNG = "png";
    public static final String WEBP = "webp";
    public static final String GIF = "gif";

    /**
     * 视频格式常量
     */
    public static final String MP4 = "mp4";
    public static final String AVI = "avi";
    public static final String RMVB = "rmvb";
    public static final String RM = "rm";
    public static final String FLV = "flv";

    /**
     * 图片格式集合（用于快速校验）
     */
    public static final java.util.Set<String> IMAGE_FORMATS = java.util.Set.of(JPG, JPEG, PNG, WEBP, GIF);

    /**
     * 视频格式集合（用于快速校验）
     */
    public static final java.util.Set<String> VIDEO_FORMATS = java.util.Set.of(MP4, AVI, RMVB, RM, FLV);

    /**
     * 判断是否为图片格式
     *
     * @param format 文件格式
     * @return 是否为图片格式
     */
    public static boolean isImageFormat(String format) {
        return IMAGE_FORMATS.contains(format.toLowerCase());
    }

    /**
     * 判断是否为视频格式
     *
     * @param format 文件格式
     * @return 是否为视频格式
     */
    public static boolean isVideoFormat(String format) {
        return VIDEO_FORMATS.contains(format.toLowerCase());
    }
}