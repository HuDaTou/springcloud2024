package com.overthinker.cloud.common.core.utils;

import com.overthinker.cloud.common.core.constants.HttpConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 字符串工具类
 * <p>
 * 提供基于 Apache Commons Lang3 的字符串操作方法，包括判空、格式化、转换等功能
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
public class MyStringUtils {

    /** 空字符串 */
    private static final String NULLSTR = "";

    /** 下划线字符 */
    private static final char SEPARATOR = '_';

    /** 随机数生成器 */
    private static final Random RANDOM = new Random();

    /** 随机字符串字符集 */
    private static final String RANDOM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 私有构造器，禁止实例化
     */
    private MyStringUtils() {
    }

    /**
     * 判断字符串是否为空（null 或空字符串）
     *
     * @param str 要判断的字符串
     * @return 如果字符串为空返回 true
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 要判断的字符串
     * @return 如果字符串不为空返回 true
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    /**
     * 判断字符串是否为空白（null、空字符串或仅包含空白字符）
     *
     * @param str 要判断的字符串
     * @return 如果字符串为空白返回 true
     */
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    /**
     * 判断字符串是否不为空白
     *
     * @param str 要判断的字符串
     * @return 如果字符串不为空白返回 true
     */
    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    /**
     * 去除字符串首尾空白
     *
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */
    public static String trim(String str) {
        return StringUtils.trim(str);
    }

    /**
     * 去除字符串所有空白字符
     *
     * @param str 要处理的字符串
     * @return 处理后的字符串，原始字符串为 null 返回 null
     */
    public static String trimAll(String str) {
        return str == null ? null : str.replaceAll("\\s", "");
    }

    /**
     * 如果字符串为 null，返回默认值
     *
     * @param value        原始值
     * @param defaultValue 默认值
     * @return 如果原始值为 null 返回默认值，否则返回原始值
     */
    public static String nvl(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 如果相等返回 true
     */
    public static boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    /**
     * 判断两个字符串是否相等（忽略大小写）
     *
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 如果相等返回 true
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }

    /**
     * 判断字符串是否以指定前缀开始
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 如果以指定前缀开始返回 true
     */
    public static boolean startsWith(String str, String prefix) {
        return StringUtils.startsWith(str, prefix);
    }

    /**
     * 判断字符串是否以指定后缀结束
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 如果以指定后缀结束返回 true
     */
    public static boolean endsWith(String str, String suffix) {
        return StringUtils.endsWith(str, suffix);
    }

    /**
     * 判断字符串是否包含指定子串
     *
     * @param str       字符串
     * @param searchStr 子串
     * @return 如果包含返回 true
     */
    public static boolean contains(String str, String searchStr) {
        return StringUtils.contains(str, searchStr);
    }

    /**
     * 获取字符串长度
     *
     * @param str 字符串
     * @return 字符串长度，null 返回 0
     */
    public static int length(String str) {
        return StringUtils.length(str);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @return 截取后的字符串
     */
    public static String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    /**
     * 从指定位置截取字符串到末尾
     *
     * @param str   字符串
     * @param start 开始位置（包含）
     * @return 截取后的字符串
     */
    public static String substring(String str, int start) {
        return StringUtils.substring(str, start);
    }

    /**
     * 替换字符串中的目标内容
     *
     * @param str         原始字符串
     * @param target      目标字符串
     * @param replacement 替换内容
     * @return 替换后的字符串
     */
    public static String replace(String str, String target, String replacement) {
        return StringUtils.replace(str, target, replacement);
    }

    /**
     * 使用正则表达式替换字符串
     *
     * @param str         原始字符串
     * @param regex       正则表达式
     * @param replacement 替换内容
     * @return 替换后的字符串
     */
    public static String replaceAll(String str, String regex, String replacement) {
        return str.replaceAll(regex, replacement);
    }

    /**
     * 分割字符串
     *
     * @param str       原始字符串
     * @param separator 分隔符
     * @return 分割后的字符串数组
     */
    public static String[] split(String str, String separator) {
        return StringUtils.split(str, separator);
    }

    /**
     * 转换为大写
     *
     * @param str 字符串
     * @return 大写字符串
     */
    public static String toUpperCase(String str) {
        return StringUtils.upperCase(str);
    }

    /**
     * 转换为小写
     *
     * @param str 字符串
     * @return 小写字符串
     */
    public static String toLowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

    /**
     * 反转字符串
     *
     * @param str 字符串
     * @return 反转后的字符串
     */
    public static String reverse(String str) {
        return StringUtils.reverse(str);
    }

    /**
     * 重复字符串
     *
     * @param str   要重复的字符串
     * @param count 重复次数
     * @return 重复后的字符串
     */
    public static String repeat(String str, int count) {
        return StringUtils.repeat(str, count);
    }

    /**
     * 左填充字符串
     *
     * @param str     原始字符串
     * @param length  目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串
     */
    public static String leftPad(String str, int length, char padChar) {
        return StringUtils.leftPad(str, length, padChar);
    }

    /**
     * 右填充字符串
     *
     * @param str     原始字符串
     * @param length  目标长度
     * @param padChar 填充字符
     * @return 填充后的字符串
     */
    public static String rightPad(String str, int length, char padChar) {
        return StringUtils.rightPad(str, length, padChar);
    }

    /**
     * 去除字符串左侧指定字符
     *
     * @param str        字符串
     * @param stripChars 要去除的字符集
     * @return 处理后的字符串
     */
    public static String leftTrim(String str, String stripChars) {
        return StringUtils.stripStart(str, stripChars);
    }

    /**
     * 去除字符串右侧指定字符
     *
     * @param str        字符串
     * @param stripChars 要去除的字符集
     * @return 处理后的字符串
     */
    public static String rightTrim(String str, String stripChars) {
        return StringUtils.stripEnd(str, stripChars);
    }

    /**
     * 查找字符在字符串中第一次出现的位置
     *
     * @param str       字符串
     * @param searchChar 要查找的字符
     * @return 字符第一次出现的位置，未找到返回 -1
     */
    public static int indexOf(String str, char searchChar) {
        return StringUtils.indexOf(str, searchChar);
    }

    /**
     * 查找子串在字符串中第一次出现的位置
     *
     * @param str       字符串
     * @param searchStr 要查找的子串
     * @return 子串第一次出现的位置，未找到返回 -1
     */
    public static int indexOf(String str, String searchStr) {
        return StringUtils.indexOf(str, searchStr);
    }

    /**
     * 查找字符在字符串中最后一次出现的位置
     *
     * @param str       字符串
     * @param searchChar 要查找的字符
     * @return 字符最后一次出现的位置，未找到返回 -1
     */
    public static int lastIndexOf(String str, char searchChar) {
        return StringUtils.lastIndexOf(str, searchChar);
    }

    /**
     * 统计子串出现的次数
     *
     * @param str 要搜索的字符串
     * @param sub 子串
     * @return 子串出现的次数
     */
    public static int countMatches(String str, String sub) {
        return StringUtils.countMatches(str, sub);
    }

    /**
     * 判断字符串是否仅包含数字
     *
     * @param str 字符串
     * @return 如果仅包含数字返回 true
     */
    public static boolean isNumeric(String str) {
        return StringUtils.isNumeric(str);
    }

    /**
     * 判断字符串是否仅包含字母
     *
     * @param str 字符串
     * @return 如果仅包含字母返回 true
     */
    public static boolean isAlpha(String str) {
        return StringUtils.isAlpha(str);
    }

    /**
     * 判断字符串是否仅包含字母和数字
     *
     * @param str 字符串
     * @return 如果仅包含字母和数字返回 true
     */
    public static boolean isAlphanumeric(String str) {
        return StringUtils.isAlphanumeric(str);
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 首字母大写后的字符串
     */
    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }

    /**
     * 首字母小写
     *
     * @param str 字符串
     * @return 首字母小写后的字符串
     */
    public static String uncapitalize(String str) {
        return StringUtils.uncapitalize(str);
    }

    /**
     * 驼峰转下划线
     *
     * @param str 驼峰格式字符串
     * @return 下划线格式字符串（如 userName → user_name）
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
     * 下划线转驼峰
     *
     * @param str 下划线格式字符串
     * @return 驼峰格式字符串（如 user_name → userName）
     */
    public static String underlineToCamel(String str) {
        if (str == null) return null;
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == SEPARATOR) {
                nextUpperCase = true;
            } else if (nextUpperCase) {
                sb.append(Character.toUpperCase(c));
                nextUpperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰转下划线（别名）
     *
     * @param str 驼峰格式字符串
     * @return 下划线格式字符串
     * @see #toUnderScoreCase(String)
     */
    public static String camelToUnderline(String str) {
        return toUnderScoreCase(str);
    }

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     * @return 随机字符串，长度小于等于 0 返回空字符串
     */
    public static String random(int length) {
        if (length <= 0) return NULLSTR;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM_CHARS.charAt(RANDOM.nextInt(RANDOM_CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * 生成随机数字字符串
     *
     * @param length 字符串长度
     * @return 随机数字字符串，长度小于等于 0 返回空字符串
     */
    public static String randomNumeric(int length) {
        if (length <= 0) return NULLSTR;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 对象转字符串（null 转空字符串）
     *
     * @param obj 对象
     * @return 对象的字符串表示，null 转空字符串
     */
    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 对象转字符串（null 转默认字符串）
     *
     * @param obj         对象
     * @param defaultStr 默认字符串
     * @return 对象的字符串表示，null 转默认字符串
     */
    public static String toString(Object obj, String defaultStr) {
        return obj == null ? defaultStr : obj.toString();
    }

    /**
     * 字符串缩写
     *
     * @param str       字符串
     * @param maxLength 最大长度
     * @return 缩写后的字符串
     */
    public static String abbreviate(String str, int maxLength) {
        return StringUtils.abbreviate(str, maxLength);
    }

    /**
     * 比较两个字符串的差异
     *
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 差异描述字符串
     */
    public static String diff(String str1, String str2) {
        if (equals(str1, str2)) return "";
        if (isEmpty(str1)) return str2;
        if (isEmpty(str2)) return str1;

        StringBuilder diff = new StringBuilder();
        int minLength = Math.min(str1.length(), str2.length());

        for (int i = 0; i < minLength; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                diff.append("[")
                        .append(str1.charAt(i))
                        .append("->")
                        .append(str2.charAt(i))
                        .append("]");
            } else {
                diff.append(str1.charAt(i));
            }
        }

        if (str1.length() > minLength) {
            diff.append("{+").append(str1.substring(minLength)).append("}");
        } else if (str2.length() > minLength) {
            diff.append("{+").append(str2.substring(minLength)).append("}");
        }

        return diff.toString();
    }

    /**
     * 构建 Redis Key
     *
     * @param segments Key 片段
     * @return 格式化的 Redis Key（如 user:token:12345）
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
     * 判断字符串是否为 HTTP/HTTPS 链接
     *
     * @param link 字符串
     * @return 如果是 HTTP/HTTPS 链接返回 true
     */
    public static boolean ishttp(String link) {
        return StringUtils.startsWithAny(link, HttpConst.HTTP, HttpConst.HTTPS);
    }

    /**
     * 判断 URL 是否匹配 Ant 风格的路径模式
     *
     * @param pattern 路径模式（如 /api/**）
     * @param url     URL 路径
     * @return 如果匹配返回 true，任一参数为 null 返回 false
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        if (pattern == null || url == null) {
            return false;
        }
        return matcher.match(pattern, url);
    }

    /**
     * 判断对象是否不为 null
     *
     * @param obj 对象
     * @return 如果不为 null 返回 true
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * 判断对象是否为 null
     *
     * @param obj 对象
     * @return 如果为 null 返回 true
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }
}