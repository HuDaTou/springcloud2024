package com.overthinker.cloud.common.core.utils;

import com.overthinker.cloud.common.core.constants.HttpConst;
import org.springframework.util.AntPathMatcher;
import java.util.*;

/**
 * 字符串工具类
 * 仅处理 String 相关的逻辑。继承自 Apache Commons Lang3。
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String NULLSTR = "";
    private static final char SEPARATOR = '_';

    /**
     * 获取字符串，如果为 null 则返回默认值
     * @param value 原始字符串
     * @param defaultValue 默认字符串
     * @return 字符串结果
     */
    public static String nvl(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * 驼峰转下划线命名 (例如: UserToken -> user_token)
     * 常用于 Redis 键或数据库字段名转换
     * @param str 待转换的字符串
     * @return 转换后的下划线字符串
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) return null;
        StringBuilder sb = new StringBuilder();
        boolean preCharIsUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                if (!preCharIsUpperCase) sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
            preCharIsUpperCase = Character.isUpperCase(c);
        }
        return sb.toString();
    }

    /**
     * 将多个字符串片段拼接成 Redis 键格式 (自动处理驼峰转下划线，并以冒号分隔)
     * @param segments 键的各个组成部分 (必须是 String)
     * @return 拼接后的 Redis Key，例如 "auth:login_code:123"
     */
    public static String buildRedisKey(String... segments) {
        if (segments == null || segments.length == 0) return NULLSTR;
        List<String> list = new ArrayList<>();
        for (String segment : segments) {
            if (isNotBlank(segment)) {
                list.add(toUnderScoreCase(segment));
            }
        }
        return String.join(":", list);
    }

    /**
     * 是否为 http(s):// 开头的链接
     * @param link 待检测的字符串
     * @return true：是；false：否
     */
    public static boolean ishttp(String link) {
        return startsWithAny(link, HttpConst.HTTP, HttpConst.HTTPS);
    }

    /**
     * 判断字符串路径是否匹配 Ant 风格规则
     * @param pattern 匹配规则 (如 /admin/**)
     * @param url 待匹配的字符串
     * @return 是否匹配
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }
}