package com.overthinker.cloud.common.core.utils;

import java.time.*;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

/**
 * 日期时间工具类
 * <p>
 * 提供基于 java.time 的日期时间操作方法，包括格式化、解析、计算、比较等功能
 * </p>
 *
 * @author overthinker
 * @since 2024-01-15
 */
public class MyDateUtils {

    /**
     * 日期格式：yyyyMMdd
     */
    public static final String PATTERN_yyyyMMdd = "yyyyMMdd";

    /**
     * 日期格式：yyyy-MM-dd
     */
    public static final String PATTERN_yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * 日期时间格式：yyyyMMddHHmmss
     */
    public static final String PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";

    /**
     * 日期时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String PATTERN_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式：HH:mm:ss
     */
    public static final String PATTERN_HH_mm_ss = "HH:mm:ss";

    /**
     * 月份格式：yyyyMM
     */
    public static final String PATTERN_yyyyMM = "yyyyMM";

    /**
     * 月份格式：yyyy-MM
     */
    public static final String PATTERN_yyyy_MM = "yyyy-MM";

    /** 默认日期时间格式化器 */
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_yyyy_MM_dd_HH_mm_ss);

    /** 日期格式化器 */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_yyyy_MM_dd);

    /** 时间格式化器 */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_HH_mm_ss);

    /** yyyyMMdd 格式化器 */
    private static final DateTimeFormatter yyyyMMdd_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_yyyyMMdd);

    /** 英文星期名称数组 */
    private static final String[] WEEK_NAMES = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    /** 中文星期名称数组 */
    private static final String[] CHINESE_WEEK_NAMES = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 私有构造器，禁止实例化
     */
    private MyDateUtils() {
    }

    /**
     * 获取当前日期时间
     *
     * @return 当前日期时间的 LocalDateTime 对象
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期的 LocalDate 对象
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间的 LocalTime 对象
     */
    public static LocalTime nowTime() {
        return LocalTime.now();
    }

    /**
     * 获取当前时间戳（毫秒）
     *
     * @return 当前时间戳
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 使用默认格式（yyyy-MM-dd HH:mm:ss）格式化日期时间
     *
     * @param dateTime 要格式化的日期时间对象
     * @return 格式化后的字符串，如果 dateTime 为 null 则返回空字符串
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * 使用指定格式格式化日期时间
     *
     * @param dateTime 要格式化的日期时间对象
     * @param pattern 日期时间格式模式（如 "yyyy-MM-dd HH:mm:ss"）
     * @return 格式化后的字符串，如果 dateTime 或 pattern 为 null 则返回空字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || pattern == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 使用默认格式（yyyy-MM-dd）格式化日期
     *
     * @param date 要格式化的日期对象
     * @return 格式化后的字符串，如果 date 为 null 则返回空字符串
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * 使用指定格式格式化日期
     *
     * @param date    要格式化的日期对象
     * @param pattern 日期格式模式（如 "yyyy-MM-dd"）
     * @return 格式化后的字符串，如果 date 或 pattern 为 null 则返回空字符串
     */
    public static String formatDate(LocalDate date, String pattern) {
        if (date == null || pattern == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 使用默认格式（HH:mm:ss）格式化时间
     *
     * @param time 要格式化的时间对象
     * @return 格式化后的字符串，如果 time 为 null 则返回空字符串
     */
    public static String formatTime(LocalTime time) {
        if (time == null) {
            return "";
        }
        return time.format(TIME_FORMATTER);
    }

    /**
     * 使用指定格式格式化时间
     *
     * @param time    要格式化的时间对象
     * @param pattern 时间格式模式（如 "HH:mm:ss"）
     * @return 格式化后的字符串，如果 time 或 pattern 为 null 则返回空字符串
     */
    public static String formatTime(LocalTime time, String pattern) {
        if (time == null || pattern == null) {
            return "";
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化当前日期时间（使用默认格式 yyyy-MM-dd HH:mm:ss）
     *
     * @return 格式化后的当前日期时间字符串
     */
    public static String formatNow() {
        return LocalDateTime.now().format(DEFAULT_FORMATTER);
    }

    /**
     * 使用默认格式解析日期时间字符串
     *
     * @param dateStr 日期时间字符串
     * @return 解析后的 LocalDateTime 对象，解析失败返回 null
     */
    public static LocalDateTime parse(String dateStr) {
        return parseToLocalDateTime(dateStr);
    }

    /**
     * 使用指定格式解析日期时间字符串
     *
     * @param dateStr 日期时间字符串
     * @param pattern 日期时间格式模式
     * @return 解析后的 LocalDateTime 对象，解析失败返回 null
     */
    public static LocalDateTime parse(String dateStr, String pattern) {
        return parseToLocalDateTime(dateStr, pattern);
    }

    /**
     * 使用默认格式解析日期字符串
     *
     * @param dateStr 日期字符串
     * @return 解析后的 LocalDate 对象，解析失败返回 null
     */
    public static LocalDate parseDate(String dateStr) {
        return parseToLocalDate(dateStr);
    }

    /**
     * 使用指定格式解析日期字符串
     *
     * @param dateStr 日期字符串
     * @param pattern 日期格式模式
     * @return 解析后的 LocalDate 对象，解析失败返回 null
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        return parseToLocalDate(dateStr, pattern);
    }

    /**
     * 使用默认格式（HH:mm:ss）解析时间字符串
     *
     * @param timeStr 时间字符串
     * @return 解析后的 LocalTime 对象，解析失败返回 null
     */
    public static LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用指定格式解析时间字符串
     *
     * @param timeStr 时间字符串
     * @param pattern 时间格式模式
     * @return 解析后的 LocalTime 对象，解析失败返回 null
     */
    public static LocalTime parseTime(String timeStr, String pattern) {
        if (timeStr == null || timeStr.isEmpty() || pattern == null) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 自动识别格式解析日期时间字符串
     * <p>
     * 支持的格式：yyyyMMdd、yyyy-MM-dd、yyyy-MM-dd HH:mm:ss
     * </p>
     *
     * @param dateStr 日期时间字符串
     * @return 解析后的 LocalDateTime 对象，解析失败返回 null
     */
    public static LocalDateTime parseToLocalDateTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            if (dateStr.length() == 8) {
                return LocalDate.parse(dateStr, yyyyMMdd_FORMATTER).atStartOfDay();
            } else if (dateStr.contains("-") && !dateStr.contains(":")) {
                return LocalDate.parse(dateStr, DATE_FORMATTER).atStartOfDay();
            } else {
                return LocalDateTime.parse(dateStr, DEFAULT_FORMATTER);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用指定格式解析日期时间字符串
     *
     * @param dateStr 日期时间字符串
     * @param pattern 日期时间格式模式
     * @return 解析后的 LocalDateTime 对象，解析失败返回 null
     */
    public static LocalDateTime parseToLocalDateTime(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty() || pattern == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            if (pattern.contains("HH") || pattern.contains("mm") || pattern.contains("ss")) {
                return LocalDateTime.parse(dateStr, formatter);
            } else {
                return LocalDate.parse(dateStr, formatter).atStartOfDay();
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 自动识别格式解析日期字符串
     * <p>
     * 支持的格式：yyyyMMdd、yyyy-MM-dd
     * </p>
     *
     * @param dateStr 日期字符串
     * @return 解析后的 LocalDate 对象，解析失败返回 null
     */
    public static LocalDate parseToLocalDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            if (dateStr.length() == 8) {
                return LocalDate.parse(dateStr, yyyyMMdd_FORMATTER);
            } else {
                return LocalDate.parse(dateStr, DATE_FORMATTER);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用指定格式解析日期字符串
     *
     * @param dateStr 日期字符串
     * @param pattern 日期格式模式
     * @return 解析后的 LocalDate 对象，解析失败返回 null
     */
    public static LocalDate parseToLocalDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty() || pattern == null) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期时间加天数
     *
     * @param dateTime 日期时间
     * @param days     要增加的天数（可以为负数）
     * @return 增加天数后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusDays(days);
    }

    /**
     * 日期时间加小时
     *
     * @param dateTime 日期时间
     * @param hours    要增加的小时数（可以为负数）
     * @return 增加小时后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusHours(hours);
    }

    /**
     * 日期时间加分钟
     *
     * @param dateTime 日期时间
     * @param minutes  要增加的分钟数（可以为负数）
     * @return 增加分钟后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusMinutes(minutes);
    }

    /**
     * 日期时间加秒
     *
     * @param dateTime 日期时间
     * @param seconds  要增加的秒数（可以为负数）
     * @return 增加秒后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime plusSeconds(LocalDateTime dateTime, long seconds) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusSeconds(seconds);
    }

    /**
     * 日期时间加月数
     *
     * @param dateTime 日期时间
     * @param months   要增加的月数（可以为负数）
     * @return 增加月数后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime plusMonths(LocalDateTime dateTime, long months) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusMonths(months);
    }

    /**
     * 日期时间加年数
     *
     * @param dateTime 日期时间
     * @param years    要增加的年数（可以为负数）
     * @return 增加年数后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime plusYears(LocalDateTime dateTime, long years) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusYears(years);
    }

    /**
     * 日期时间减天数
     *
     * @param dateTime 日期时间
     * @param days     要减少的天数（可以为负数）
     * @return 减少天数后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime minusDays(LocalDateTime dateTime, long days) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusDays(days);
    }

    /**
     * 日期时间减小时
     *
     * @param dateTime 日期时间
     * @param hours    要减少的小时数（可以为负数）
     * @return 减少小时后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime minusHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusHours(hours);
    }

    /**
     * 日期时间减分钟
     *
     * @param dateTime 日期时间
     * @param minutes  要减少的分钟数（可以为负数）
     * @return 减少分钟后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime minusMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusMinutes(minutes);
    }

    /**
     * 日期时间减秒
     *
     * @param dateTime 日期时间
     * @param seconds  要减少的秒数（可以为负数）
     * @return 减少秒后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime minusSeconds(LocalDateTime dateTime, long seconds) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusSeconds(seconds);
    }

    /**
     * 日期时间减月数
     *
     * @param dateTime 日期时间
     * @param months   要减少的月数（可以为负数）
     * @return 减少月数后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime minusMonths(LocalDateTime dateTime, long months) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusMonths(months);
    }

    /**
     * 日期时间减年数
     *
     * @param dateTime 日期时间
     * @param years    要减少的年数（可以为负数）
     * @return 减少年数后的日期时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime minusYears(LocalDateTime dateTime, long years) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusYears(years);
    }

    /**
     * 获取昨天的日期时间
     *
     * @return 昨天的日期时间（当前时间减一天）
     */
    public static LocalDateTime yesterday() {
        return LocalDateTime.now().minusDays(1);
    }

    /**
     * 获取明天的日期时间
     *
     * @return 明天的日期时间（当前时间加一天）
     */
    public static LocalDateTime tomorrow() {
        return LocalDateTime.now().plusDays(1);
    }

    /**
     * 获取日期时间的当天开始时间（00:00:00）
     *
     * @param dateTime 日期时间
     * @return 当天开始时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime beginOfDay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalDate().atStartOfDay();
    }

    /**
     * 获取日期时间的当天结束时间（23:59:59.999999999）
     *
     * @param dateTime 日期时间
     * @return 当天结束时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime endOfDay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalDate().atTime(LocalTime.MAX);
    }

    /**
     * 获取日期时间所在周的开始时间（周一 00:00:00）
     *
     * @param dateTime 日期时间
     * @return 周开始时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime beginOfWeek(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
    }

    /**
     * 获取日期时间所在周的结束时间（周日 23:59:59.999999999）
     *
     * @param dateTime 日期时间
     * @return 周结束时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime endOfWeek(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toLocalDate().atTime(LocalTime.MAX);
    }

    /**
     * 获取日期时间所在月的开始时间（1日 00:00:00）
     *
     * @param dateTime 日期时间
     * @return 月开始时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime beginOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
    }

    /**
     * 获取日期时间所在月的结束时间（最后一日 23:59:59.999999999）
     *
     * @param dateTime 日期时间
     * @return 月结束时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime endOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().atTime(LocalTime.MAX);
    }

    /**
     * 获取日期时间所在季度的开始时间
     *
     * @param dateTime 日期时间
     * @return 季度开始时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime beginOfQuarter(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        int month = dateTime.getMonthValue();
        int quarterStartMonth = ((month - 1) / 3) * 3 + 1;
        return dateTime.withMonth(quarterStartMonth).withDayOfMonth(1).toLocalDate().atStartOfDay();
    }

    /**
     * 获取日期时间所在季度的结束时间
     *
     * @param dateTime 日期时间
     * @return 季度结束时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime endOfQuarter(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        int month = dateTime.getMonthValue();
        int quarterEndMonth = ((month - 1) / 3) * 3 + 3;
        return dateTime.withMonth(quarterEndMonth).with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().atTime(LocalTime.MAX);
    }

    /**
     * 获取日期时间所在年度的开始时间（1月1日 00:00:00）
     *
     * @param dateTime 日期时间
     * @return 年度开始时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime beginOfYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.firstDayOfYear()).toLocalDate().atStartOfDay();
    }

    /**
     * 获取日期时间所在年度的结束时间（12月31日 23:59:59.999999999）
     *
     * @param dateTime 日期时间
     * @return 年度结束时间，dateTime 为 null 返回 null
     */
    public static LocalDateTime endOfYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.lastDayOfYear()).toLocalDate().atTime(LocalTime.MAX);
    }

    /**
     * 计算两个日期时间之间的天数差
     *
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 天数差的绝对值，任一参数为 null 返回 0
     */
    public static long betweenDays(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.DAYS.between(start, end));
    }

    /**
     * 计算两个日期时间之间的小时差
     *
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 小时差的绝对值，任一参数为 null 返回 0
     */
    public static long betweenHours(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.HOURS.between(start, end));
    }

    /**
     * 计算两个日期时间之间的分钟差
     *
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 分钟差的绝对值，任一参数为 null 返回 0
     */
    public static long betweenMinutes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.MINUTES.between(start, end));
    }

    /**
     * 计算两个日期时间之间的秒数差
     *
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 秒数差的绝对值，任一参数为 null 返回 0
     */
    public static long betweenSeconds(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.SECONDS.between(start, end));
    }

    /**
     * 计算两个日期时间之间的月数差
     *
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 月数差的绝对值，任一参数为 null 返回 0
     */
    public static long betweenMonths(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.MONTHS.between(start, end));
    }

    /**
     * 计算两个日期时间之间的年数差
     *
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 年数差的绝对值，任一参数为 null 返回 0
     */
    public static long betweenYears(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.YEARS.between(start, end));
    }

    /**
     * 判断两个日期时间是否是同一天
     *
     * @param dateTime1 第一个日期时间
     * @param dateTime2 第二个日期时间
     * @return 如果是同一天返回 true，任一参数为 null 返回 false
     */
    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.toLocalDate().equals(dateTime2.toLocalDate());
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果是同一天返回 true，任一参数为 null 返回 false
     */
    public static boolean isSameDay(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.equals(date2);
    }

    /**
     * 判断日期时间是否是今天
     *
     * @param dateTime 日期时间
     * @return 如果是今天返回 true，dateTime 为 null 返回 false
     */
    public static boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.toLocalDate().equals(LocalDate.now());
    }

    /**
     * 判断日期是否是今天
     *
     * @param date 日期
     * @return 如果是今天返回 true，date 为 null 返回 false
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now());
    }

    /**
     * 判断日期时间是否是昨天
     *
     * @param dateTime 日期时间
     * @return 如果是昨天返回 true，dateTime 为 null 返回 false
     */
    public static boolean isYesterday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.toLocalDate().equals(LocalDate.now().minusDays(1));
    }

    /**
     * 判断日期时间是否是明天
     *
     * @param dateTime 日期时间
     * @return 如果是明天返回 true，dateTime 为 null 返回 false
     */
    public static boolean isTomorrow(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.toLocalDate().equals(LocalDate.now().plusDays(1));
    }

    /**
     * 判断日期时间所在年份是否是闰年
     *
     * @param dateTime 日期时间
     * @return 如果是闰年返回 true，dateTime 为 null 返回 false
     */
    public static boolean isLeapYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.toLocalDate().isLeapYear();
    }

    /**
     * 判断日期所在年份是否是闰年
     *
     * @param date 日期
     * @return 如果是闰年返回 true，date 为 null 返回 false
     */
    public static boolean isLeapYear(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isLeapYear();
    }

    /**
     * 判断指定年份是否是闰年
     *
     * @param year 年份
     * @return 如果是闰年返回 true
     */
    public static boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    /**
     * 获取日期时间的年份
     *
     * @param dateTime 日期时间
     * @return 年份，dateTime 为 null 返回 0
     */
    public static int getYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getYear();
    }

    /**
     * 获取日期的年份
     *
     * @param date 日期
     * @return 年份，date 为 null 返回 0
     */
    public static int getYear(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.getYear();
    }

    /**
     * 获取日期时间的月份值（1-12）
     *
     * @param dateTime 日期时间
     * @return 月份值，dateTime 为 null 返回 0
     */
    public static int getMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getMonthValue();
    }

    /**
     * 获取日期的月份值（1-12）
     *
     * @param date 日期
     * @return 月份值，date 为 null 返回 0
     */
    public static int getMonth(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.getMonthValue();
    }

    /**
     * 获取日期时间在当月的天数（1-31）
     *
     * @param dateTime 日期时间
     * @return 天数，dateTime 为 null 返回 0
     */
    public static int getDayOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getDayOfMonth();
    }

    /**
     * 获取日期在当月的天数（1-31）
     *
     * @param date 日期
     * @return 天数，date 为 null 返回 0
     */
    public static int getDayOfMonth(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.getDayOfMonth();
    }

    /**
     * 获取日期时间在当周的星期几（1=Monday, 7=Sunday）
     *
     * @param dateTime 日期时间
     * @return 星期几的值，dateTime 为 null 返回 0
     */
    public static int getDayOfWeek(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getDayOfWeek().getValue();
    }

    /**
     * 获取日期在当周的星期几（1=Monday, 7=Sunday）
     *
     * @param date 日期
     * @return 星期几的值，date 为 null 返回 0
     */
    public static int getDayOfWeek(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.getDayOfWeek().getValue();
    }

    /**
     * 获取日期时间在当年的第几天
     *
     * @param dateTime 日期时间
     * @return 当年第几天，dateTime 为 null 返回 0
     */
    public static int getDayOfYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getDayOfYear();
    }

    /**
     * 获取日期在当年的第几天
     *
     * @param date 日期
     * @return 当年第几天，date 为 null 返回 0
     */
    public static int getDayOfYear(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.getDayOfYear();
    }

    /**
     * 获取日期时间的小时数（0-23）
     *
     * @param dateTime 日期时间
     * @return 小时数，dateTime 为 null 返回 0
     */
    public static int getHour(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getHour();
    }

    /**
     * 获取时间的小时数（0-23）
     *
     * @param time 时间
     * @return 小时数，time 为 null 返回 0
     */
    public static int getHour(LocalTime time) {
        if (time == null) {
            return 0;
        }
        return time.getHour();
    }

    /**
     * 获取日期时间的分钟数（0-59）
     *
     * @param dateTime 日期时间
     * @return 分钟数，dateTime 为 null 返回 0
     */
    public static int getMinute(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getMinute();
    }

    /**
     * 获取时间的分钟数（0-59）
     *
     * @param time 时间
     * @return 分钟数，time 为 null 返回 0
     */
    public static int getMinute(LocalTime time) {
        if (time == null) {
            return 0;
        }
        return time.getMinute();
    }

    /**
     * 获取日期时间的秒数（0-59）
     *
     * @param dateTime 日期时间
     * @return 秒数，dateTime 为 null 返回 0
     */
    public static int getSecond(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getSecond();
    }

    /**
     * 获取时间的秒数（0-59）
     *
     * @param time 时间
     * @return 秒数，time 为 null 返回 0
     */
    public static int getSecond(LocalTime time) {
        if (time == null) {
            return 0;
        }
        return time.getSecond();
    }

    /**
     * 获取日期时间在当月的周数
     *
     * @param dateTime 日期时间
     * @return 当月第几周，dateTime 为 null 返回 0
     */
    public static int getWeekOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.get(WeekFields.ISO.weekOfMonth());
    }

    /**
     * 获取日期在当月的周数
     *
     * @param date 日期
     * @return 当月第几周，date 为 null 返回 0
     */
    public static int getWeekOfMonth(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.get(WeekFields.ISO.weekOfMonth());
    }

    /**
     * 获取日期时间在当年的周数
     *
     * @param dateTime 日期时间
     * @return 当年第几周，dateTime 为 null 返回 0
     */
    public static int getWeekOfYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.get(WeekFields.ISO.weekOfYear());
    }

    /**
     * 获取日期在当年的周数
     *
     * @param date 日期
     * @return 当年第几周，date 为 null 返回 0
     */
    public static int getWeekOfYear(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.get(WeekFields.ISO.weekOfYear());
    }

    /**
     * 获取日期时间的星期英文名称
     *
     * @param dateTime 日期时间
     * @return 星期英文名称（如 "Monday"），dateTime 为 null 返回空字符串
     */
    public static String getWeekName(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        int dayOfWeek = getDayOfWeek(dateTime);
        return dayOfWeek >= 1 && dayOfWeek <= 7 ? WEEK_NAMES[dayOfWeek - 1] : "";
    }

    /**
     * 获取日期的星期英文名称
     *
     * @param date 日期
     * @return 星期英文名称（如 "Monday"），date 为 null 返回空字符串
     */
    public static String getWeekName(LocalDate date) {
        if (date == null) {
            return "";
        }
        int dayOfWeek = getDayOfWeek(date);
        return dayOfWeek >= 1 && dayOfWeek <= 7 ? WEEK_NAMES[dayOfWeek - 1] : "";
    }

    /**
     * 获取日期时间的中文星期名称
     *
     * @param dateTime 日期时间
     * @return 中文星期名称（如 "星期一"），dateTime 为 null 返回空字符串
     */
    public static String getChineseWeekName(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        int dayOfWeek = getDayOfWeek(dateTime);
        return dayOfWeek >= 1 && dayOfWeek <= 7 ? CHINESE_WEEK_NAMES[dayOfWeek - 1] : "";
    }

    /**
     * 获取日期的中文星期名称
     *
     * @param date 日期
     * @return 中文星期名称（如 "星期一"），date 为 null 返回空字符串
     */
    public static String getChineseWeekName(LocalDate date) {
        if (date == null) {
            return "";
        }
        int dayOfWeek = getDayOfWeek(date);
        return dayOfWeek >= 1 && dayOfWeek <= 7 ? CHINESE_WEEK_NAMES[dayOfWeek - 1] : "";
    }

    /**
     * 获取日期时间所在月的天数
     *
     * @param dateTime 日期时间
     * @return 所在月的天数，dateTime 为 null 返回 0
     */
    public static int getDaysInMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.toLocalDate().lengthOfMonth();
    }

    /**
     * 获取日期所在月的天数
     *
     * @param date 日期
     * @return 所在月的天数，date 为 null 返回 0
     */
    public static int getDaysInMonth(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.lengthOfMonth();
    }

    /**
     * 获取指定年月的天数
     *
     * @param year  年份
     * @param month 月份（1-12）
     * @return 所在月的天数，参数无效返回 0
     */
    public static int getDaysInMonth(int year, int month) {
        try {
            return YearMonth.of(year, month).lengthOfMonth();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 比较两个日期时间
     *
     * @param dateTime1 第一个日期时间
     * @param dateTime2 第二个日期时间
     * @return 如果两个日期时间相等返回 0，第一个小于第二个返回负数，第一个大于第二个返回正数
     */
    public static int compare(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null && dateTime2 == null) {
            return 0;
        }
        if (dateTime1 == null) {
            return -1;
        }
        if (dateTime2 == null) {
            return 1;
        }
        return dateTime1.compareTo(dateTime2);
    }

    /**
     * 比较两个日期
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果两个日期相等返回 0，第一个小于第二个返回负数，第一个大于第二个返回正数
     */
    public static int compare(LocalDate date1, LocalDate date2) {
        if (date1 == null && date2 == null) {
            return 0;
        }
        if (date1 == null) {
            return -1;
        }
        if (date2 == null) {
            return 1;
        }
        return date1.compareTo(date2);
    }

    /**
     * 判断第一个日期时间是否在第二个之前
     *
     * @param dateTime1 第一个日期时间
     * @param dateTime2 第二个日期时间
     * @return 如果第一个在第二个之前返回 true，任一参数为 null 返回 false
     */
    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isBefore(dateTime2);
    }

    /**
     * 判断第一个日期是否在第二个之前
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果第一个在第二个之前返回 true，任一参数为 null 返回 false
     */
    public static boolean isBefore(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isBefore(date2);
    }

    /**
     * 判断第一个日期时间是否在第二个之后
     *
     * @param dateTime1 第一个日期时间
     * @param dateTime2 第二个日期时间
     * @return 如果第一个在第二个之后返回 true，任一参数为 null 返回 false
     */
    public static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isAfter(dateTime2);
    }

    /**
     * 判断第一个日期是否在第二个之后
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果第一个在第二个之后返回 true，任一参数为 null 返回 false
     */
    public static boolean isAfter(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isAfter(date2);
    }

    /**
     * 判断两个日期时间是否相等
     *
     * @param dateTime1 第一个日期时间
     * @param dateTime2 第二个日期时间
     * @return 如果相等返回 true，任一参数为 null 返回 false
     */
    public static boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isEqual(dateTime2);
    }

    /**
     * 判断两个日期是否相等
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果相等返回 true，任一参数为 null 返回 false
     */
    public static boolean isEqual(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isEqual(date2);
    }

    /**
     * 判断日期时间是否在指定范围内（包含两端）
     *
     * @param dateTime 要判断的日期时间
     * @param begin    范围开始日期时间
     * @param end      范围结束日期时间
     * @return 如果在范围内返回 true，任一参数为 null 返回 false
     */
    public static boolean isBetween(LocalDateTime dateTime, LocalDateTime begin, LocalDateTime end) {
        if (dateTime == null || begin == null || end == null) {
            return false;
        }
        return !dateTime.isBefore(begin) && !dateTime.isAfter(end);
    }

    /**
     * 判断日期是否在指定范围内（包含两端）
     *
     * @param date  要判断的日期
     * @param begin 范围开始日期
     * @param end  范围结束日期
     * @return 如果在范围内返回 true，任一参数为 null 返回 false
     */
    public static boolean isBetween(LocalDate date, LocalDate begin, LocalDate end) {
        if (date == null || begin == null || end == null) {
            return false;
        }
        return !date.isBefore(begin) && !date.isAfter(end);
    }

    /**
     * 格式化时间段（毫秒）为中文描述
     * <p>
     * 例如：3600000 毫秒返回 "1时"
     * </p>
     *
     * @param millis 毫秒数
     * @return 中文描述的时间段（如 "1天2时3分4秒"）
     */
    public static String formatDuration(long millis) {
        if (millis <= 0) {
            return "0秒";
        }
        long days = millis / (24 * 60 * 60 * 1000);
        long hours = (millis % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long minutes = (millis % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (millis % (60 * 1000)) / 1000;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分");
        }
        if (seconds > 0 || sb.length() == 0) {
            sb.append(seconds).append("秒");
        }
        return sb.toString();
    }

    /**
     * 验证字符串是否符合指定日期格式
     *
     * @param dateStr  日期字符串
     * @param pattern 日期格式模式
     * @return 如果符合格式返回 true，任一参数为 null 或空返回 false
     */
    public static boolean isValid(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty() || pattern == null) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            if (pattern.contains("HH") || pattern.contains("mm") || pattern.contains("ss")) {
                LocalDateTime.parse(dateStr, formatter);
            } else {
                LocalDate.parse(dateStr, formatter);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证字符串是否是有效的日期格式
     *
     * @param dateStr 日期字符串
     * @return 如果是有效日期返回 true
     */
    public static boolean isValidDate(String dateStr) {
        return parseDate(dateStr) != null;
    }

    /**
     * 验证字符串是否是有效的日期时间格式
     *
     * @param dateTimeStr 日期时间字符串
     * @return 如果是有效日期时间返回 true
     */
    public static boolean isValidDateTime(String dateTimeStr) {
        return parseToLocalDateTime(dateTimeStr) != null;
    }
}