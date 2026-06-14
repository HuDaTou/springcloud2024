package com.overthinker.cloud.media.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;

/**
 * 媒体处理工具类
 * <p>
 * 提供媒体文件处理相关的工具方法
 * </p>
 *
 * @author overthinker
 * @since 2025-08-02
 */
@Slf4j
public class MediaUtils {

    private static final String[] SIZE_UNITS = {"B", "KB", "MB", "GB", "TB"};
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private MediaUtils() {
    }

    /**
     * 格式化文件大小
     *
     * @param bytes 文件大小（字节）
     * @return 格式化后的文件大小字符串，如 "10.25 MB"
     */
    public static String formatFileSize(long bytes) {
        if (bytes <= 0) {
            return "0 B";
        }
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        digitGroups = Math.min(digitGroups, SIZE_UNITS.length - 1);
        double size = bytes / Math.pow(1024, digitGroups);
        return DECIMAL_FORMAT.format(size) + " " + SIZE_UNITS[digitGroups];
    }

    /**
     * 从文件扩展名获取 MIME 类型
     *
     * @param filename 文件名
     * @return MIME 类型
     */
    public static String getMimeType(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "application/octet-stream";
        }
        String lowerName = filename.toLowerCase();
        if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerName.endsWith(".png")) {
            return "image/png";
        } else if (lowerName.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerName.endsWith(".webp")) {
            return "image/webp";
        } else if (lowerName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (lowerName.endsWith(".webm")) {
            return "video/webm";
        } else if (lowerName.endsWith(".mov")) {
            return "video/quicktime";
        } else if (lowerName.endsWith(".avi")) {
            return "video/x-msvideo";
        } else if (lowerName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (lowerName.endsWith(".doc")) {
            return "application/msword";
        } else if (lowerName.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        return "application/octet-stream";
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 文件扩展名（包含点），如 ".mp4"
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex);
    }

    /**
     * 检查是否为图片文件
     *
     * @param contentType MIME 类型
     * @return 是否为图片
     */
    public static boolean isImage(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * 检查是否为视频文件
     *
     * @param contentType MIME 类型
     * @return 是否为视频
     */
    public static boolean isVideo(String contentType) {
        return contentType != null && contentType.startsWith("video/");
    }
}
