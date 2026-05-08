package com.overthinker.cloud.common.core.utils;

import com.overthinker.cloud.common.core.constants.HttpConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyStringUtils {

    private static final String NULLSTR = "";
    private static final char SEPARATOR = '_';
    private static final Random RANDOM = new Random();
    private static final String RANDOM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String nvl(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

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

    public static boolean ishttp(String link) {
        return StringUtils.startsWithAny(link, HttpConst.HTTP, HttpConst.HTTPS);
    }

    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        // 添加 null 检查以避免 Null type safety 警告
        if (pattern == null || url == null) {
            return false;
        }
        return matcher.match(pattern, url);
    }

    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static String trim(String str) {
        return StringUtils.trim(str);
    }

    public static String trimAll(String str) {
        return str == null ? null : str.replaceAll("\\s", "");
    }

    public static boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }

    public static boolean startsWith(String str, String prefix) {
        return StringUtils.startsWith(str, prefix);
    }

    public static boolean endsWith(String str, String suffix) {
        return StringUtils.endsWith(str, suffix);
    }

    public static boolean contains(String str, String searchStr) {
        return StringUtils.contains(str, searchStr);
    }

    public static int length(String str) {
        return StringUtils.length(str);
    }

    public static String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    public static String substring(String str, int start) {
        return StringUtils.substring(str, start);
    }

    public static String replace(String str, String target, String replacement) {
        return StringUtils.replace(str, target, replacement);
    }

    public static String replaceAll(String str, String regex, String replacement) {
        return str.replaceAll(regex, replacement);
    }

    public static String[] split(String str, String separator) {
        return StringUtils.split(str, separator);
    }

    public static String toUpperCase(String str) {
        return StringUtils.upperCase(str);
    }

    public static String toLowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

    public static String reverse(String str) {
        return StringUtils.reverse(str);
    }

    public static String repeat(String str, int count) {
        return StringUtils.repeat(str, count);
    }

    public static String leftPad(String str, int length, char padChar) {
        return StringUtils.leftPad(str, length, padChar);
    }

    public static String rightPad(String str, int length, char padChar) {
        return StringUtils.rightPad(str, length, padChar);
    }

    public static String leftTrim(String str, String stripChars) {
        return StringUtils.stripStart(str, stripChars);
    }

    public static String rightTrim(String str, String stripChars) {
        return StringUtils.stripEnd(str, stripChars);
    }

    public static int indexOf(String str, char searchChar) {
        return StringUtils.indexOf(str, searchChar);
    }

    public static int indexOf(String str, String searchStr) {
        return StringUtils.indexOf(str, searchStr);
    }

    public static int lastIndexOf(String str, char searchChar) {
        return StringUtils.lastIndexOf(str, searchChar);
    }

    public static int countMatches(String str, String sub) {
        return StringUtils.countMatches(str, sub);
    }

    public static boolean isNumeric(String str) {
        return StringUtils.isNumeric(str);
    }

    public static boolean isAlpha(String str) {
        return StringUtils.isAlpha(str);
    }

    public static boolean isAlphanumeric(String str) {
        return StringUtils.isAlphanumeric(str);
    }

    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }

    public static String uncapitalize(String str) {
        return StringUtils.uncapitalize(str);
    }

    public static String camelToUnderline(String str) {
        return toUnderScoreCase(str);
    }

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

    public static String random(int length) {
        if (length <= 0) return NULLSTR;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM_CHARS.charAt(RANDOM.nextInt(RANDOM_CHARS.length())));
        }
        return sb.toString();
    }

    public static String randomNumeric(int length) {
        if (length <= 0) return NULLSTR;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String toString(Object obj, String defaultStr) {
        return obj == null ? defaultStr : obj.toString();
    }

    public static String abbreviate(String str, int maxLength) {
        return StringUtils.abbreviate(str, maxLength);
    }

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
}