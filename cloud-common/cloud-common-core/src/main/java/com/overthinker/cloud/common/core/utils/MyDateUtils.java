package com.overthinker.cloud.common.core.utils;

import com.overthinker.cloud.common.core.utils.Iutils.IDateUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

public class MyDateUtils implements IDateUtils {

    private static final MyDateUtils INSTANCE = new MyDateUtils();

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_yyyy_MM_dd_HH_mm_ss);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_yyyy_MM_dd);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_HH_mm_ss);
    private static final DateTimeFormatter yyyyMMdd_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_yyyyMMdd);

    private static final String[] WEEK_NAMES = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static final String[] CHINESE_WEEK_NAMES = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    private MyDateUtils() {
    }

    public static MyDateUtils getInstance() {
        return INSTANCE;
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }

    @Override
    public LocalTime nowTime() {
        return LocalTime.now();
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DEFAULT_FORMATTER);
    }

    @Override
    public String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || pattern == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTER);
    }

    @Override
    public String formatDate(LocalDate date, String pattern) {
        if (date == null || pattern == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public String formatTime(LocalTime time) {
        if (time == null) {
            return "";
        }
        return time.format(TIME_FORMATTER);
    }

    @Override
    public String formatTime(LocalTime time, String pattern) {
        if (time == null || pattern == null) {
            return "";
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public String formatNow() {
        return LocalDateTime.now().format(DEFAULT_FORMATTER);
    }

    @Override
    public LocalDateTime parse(String dateStr) {
        return parseToLocalDateTime(dateStr);
    }

    @Override
    public LocalDateTime parse(String dateStr, String pattern) {
        return parseToLocalDateTime(dateStr, pattern);
    }

    @Override
    public LocalDate parseDate(String dateStr) {
        return parseToLocalDate(dateStr);
    }

    @Override
    public LocalDate parseDate(String dateStr, String pattern) {
        return parseToLocalDate(dateStr, pattern);
    }

    @Override
    public LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public LocalTime parseTime(String timeStr, String pattern) {
        if (timeStr == null || timeStr.isEmpty() || pattern == null) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public LocalDateTime parseToLocalDateTime(String dateStr) {
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

    @Override
    public LocalDateTime parseToLocalDateTime(String dateStr, String pattern) {
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

    @Override
    public LocalDate parseToLocalDate(String dateStr) {
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

    @Override
    public LocalDate parseToLocalDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty() || pattern == null) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusDays(days);
    }

    @Override
    public LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusHours(hours);
    }

    @Override
    public LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusMinutes(minutes);
    }

    @Override
    public LocalDateTime plusSeconds(LocalDateTime dateTime, long seconds) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusSeconds(seconds);
    }

    @Override
    public LocalDateTime plusMonths(LocalDateTime dateTime, long months) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusMonths(months);
    }

    @Override
    public LocalDateTime plusYears(LocalDateTime dateTime, long years) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusYears(years);
    }

    @Override
    public LocalDateTime minusDays(LocalDateTime dateTime, long days) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusDays(days);
    }

    @Override
    public LocalDateTime minusHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusHours(hours);
    }

    @Override
    public LocalDateTime minusMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusMinutes(minutes);
    }

    @Override
    public LocalDateTime minusSeconds(LocalDateTime dateTime, long seconds) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusSeconds(seconds);
    }

    @Override
    public LocalDateTime minusMonths(LocalDateTime dateTime, long months) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusMonths(months);
    }

    @Override
    public LocalDateTime minusYears(LocalDateTime dateTime, long years) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.minusYears(years);
    }

    @Override
    public LocalDateTime yesterday() {
        return LocalDateTime.now().minusDays(1);
    }

    @Override
    public LocalDateTime tomorrow() {
        return LocalDateTime.now().plusDays(1);
    }

    @Override
    public LocalDateTime beginOfDay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalDate().atStartOfDay();
    }

    @Override
    public LocalDateTime endOfDay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalDate().atTime(LocalTime.MAX);
    }

    @Override
    public LocalDateTime beginOfWeek(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
    }

    @Override
    public LocalDateTime endOfWeek(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toLocalDate().atTime(LocalTime.MAX);
    }

    @Override
    public LocalDateTime beginOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
    }

    @Override
    public LocalDateTime endOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().atTime(LocalTime.MAX);
    }

    @Override
    public LocalDateTime beginOfQuarter(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        int month = dateTime.getMonthValue();
        int quarterStartMonth = ((month - 1) / 3) * 3 + 1;
        return dateTime.withMonth(quarterStartMonth).withDayOfMonth(1).toLocalDate().atStartOfDay();
    }

    @Override
    public LocalDateTime endOfQuarter(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        int month = dateTime.getMonthValue();
        int quarterEndMonth = ((month - 1) / 3) * 3 + 3;
        return dateTime.withMonth(quarterEndMonth).with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().atTime(LocalTime.MAX);
    }

    @Override
    public LocalDateTime beginOfYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.firstDayOfYear()).toLocalDate().atStartOfDay();
    }

    @Override
    public LocalDateTime endOfYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.with(TemporalAdjusters.lastDayOfYear()).toLocalDate().atTime(LocalTime.MAX);
    }

    @Override
    public long betweenDays(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.DAYS.between(start, end));
    }

    @Override
    public long betweenHours(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.HOURS.between(start, end));
    }

    @Override
    public long betweenMinutes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.MINUTES.between(start, end));
    }

    @Override
    public long betweenSeconds(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.SECONDS.between(start, end));
    }

    @Override
    public long betweenMonths(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.MONTHS.between(start, end));
    }

    @Override
    public long betweenYears(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return Math.abs(ChronoUnit.YEARS.between(start, end));
    }

    @Override
    public boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.toLocalDate().equals(dateTime2.toLocalDate());
    }

    @Override
    public boolean isSameDay(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.equals(date2);
    }

    @Override
    public boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.toLocalDate().equals(LocalDate.now());
    }

    @Override
    public boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now());
    }

    @Override
    public boolean isYesterday(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.toLocalDate().equals(LocalDate.now().minusDays(1));
    }

    @Override
    public boolean isTomorrow(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.toLocalDate().equals(LocalDate.now().plusDays(1));
    }

    @Override
    public boolean isLeapYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.toLocalDate().isLeapYear();
    }

    @Override
    public boolean isLeapYear(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isLeapYear();
    }

    @Override
    public boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    @Override
    public int getYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getYear();
    }

    @Override
    public int getYear(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.getYear();
    }

    @Override
    public int getMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getMonthValue();
    }

    @Override
    public int getMonth(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.getMonthValue();
    }

    @Override
    public int getDayOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getDayOfMonth();
    }

    @Override
    public int getDayOfMonth(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.getDayOfMonth();
    }

    @Override
    public int getDayOfWeek(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getDayOfWeek().getValue();
    }

    @Override
    public int getDayOfWeek(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.getDayOfWeek().getValue();
    }

    @Override
    public int getDayOfYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getDayOfYear();
    }

    @Override
    public int getDayOfYear(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.getDayOfYear();
    }

    @Override
    public int getHour(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getHour();
    }

    @Override
    public int getHour(LocalTime time) {
        if (time == null) {
            return 0;
        }
        return time.getHour();
    }

    @Override
    public int getMinute(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getMinute();
    }

    @Override
    public int getMinute(LocalTime time) {
        if (time == null) {
            return 0;
        }
        return time.getMinute();
    }

    @Override
    public int getSecond(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.getSecond();
    }

    @Override
    public int getSecond(LocalTime time) {
        if (time == null) {
            return 0;
        }
        return time.getSecond();
    }

    @Override
    public int getWeekOfMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.get(WeekFields.ISO.weekOfMonth());
    }

    @Override
    public int getWeekOfMonth(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.get(WeekFields.ISO.weekOfMonth());
    }

    @Override
    public int getWeekOfYear(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.get(WeekFields.ISO.weekOfYear());
    }

    @Override
    public int getWeekOfYear(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.get(WeekFields.ISO.weekOfYear());
    }

    @Override
    public String getWeekName(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        int dayOfWeek = getDayOfWeek(dateTime);
        return dayOfWeek >= 1 && dayOfWeek <= 7 ? WEEK_NAMES[dayOfWeek - 1] : "";
    }

    @Override
    public String getWeekName(LocalDate date) {
        if (date == null) {
            return "";
        }
        int dayOfWeek = getDayOfWeek(date);
        return dayOfWeek >= 1 && dayOfWeek <= 7 ? WEEK_NAMES[dayOfWeek - 1] : "";
    }

    @Override
    public String getChineseWeekName(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        int dayOfWeek = getDayOfWeek(dateTime);
        return dayOfWeek >= 1 && dayOfWeek <= 7 ? CHINESE_WEEK_NAMES[dayOfWeek - 1] : "";
    }

    @Override
    public String getChineseWeekName(LocalDate date) {
        if (date == null) {
            return "";
        }
        int dayOfWeek = getDayOfWeek(date);
        return dayOfWeek >= 1 && dayOfWeek <= 7 ? CHINESE_WEEK_NAMES[dayOfWeek - 1] : "";
    }

    @Override
    public int getDaysInMonth(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.toLocalDate().lengthOfMonth();
    }

    @Override
    public int getDaysInMonth(LocalDate date) {
        if (date == null) {
            return 0;
        }
        return date.lengthOfMonth();
    }

    @Override
    public int getDaysInMonth(int year, int month) {
        try {
            return YearMonth.of(year, month).lengthOfMonth();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int compare(LocalDateTime dateTime1, LocalDateTime dateTime2) {
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

    @Override
    public int compare(LocalDate date1, LocalDate date2) {
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

    @Override
    public boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isBefore(dateTime2);
    }

    @Override
    public boolean isBefore(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isBefore(date2);
    }

    @Override
    public boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isAfter(dateTime2);
    }

    @Override
    public boolean isAfter(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isAfter(date2);
    }

    @Override
    public boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.isEqual(dateTime2);
    }

    @Override
    public boolean isEqual(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isEqual(date2);
    }

    @Override
    public boolean isBetween(LocalDateTime dateTime, LocalDateTime begin, LocalDateTime end) {
        if (dateTime == null || begin == null || end == null) {
            return false;
        }
        return !dateTime.isBefore(begin) && !dateTime.isAfter(end);
    }

    @Override
    public boolean isBetween(LocalDate date, LocalDate begin, LocalDate end) {
        if (date == null || begin == null || end == null) {
            return false;
        }
        return !date.isBefore(begin) && !date.isAfter(end);
    }

    @Override
    public String formatDuration(long millis) {
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

    @Override
    public boolean isValid(String dateStr, String pattern) {
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

    @Override
    public boolean isValidDate(String dateStr) {
        return parseDate(dateStr) != null;
    }

    @Override
    public boolean isValidDateTime(String dateTimeStr) {
        return parseToLocalDateTime(dateTimeStr) != null;
    }
}