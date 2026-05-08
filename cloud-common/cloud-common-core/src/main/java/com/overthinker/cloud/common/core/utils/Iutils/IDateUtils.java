package com.overthinker.cloud.common.core.utils.Iutils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface IDateUtils {

    String PATTERN_yyyyMMdd = "yyyyMMdd";
    String PATTERN_yyyy_MM_dd = "yyyy-MM-dd";
    String PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    String PATTERN_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    String PATTERN_HH_mm_ss = "HH:mm:ss";
    String PATTERN_yyyyMM = "yyyyMM";
    String PATTERN_yyyy_MM = "yyyy-MM";

    LocalDateTime now();

    LocalDate today();

    LocalTime nowTime();

    long currentTimeMillis();

    String format(LocalDateTime dateTime);

    String format(LocalDateTime dateTime, String pattern);

    String formatDate(LocalDate date);

    String formatDate(LocalDate date, String pattern);

    String formatTime(LocalTime time);

    String formatTime(LocalTime time, String pattern);

    String formatNow();

    LocalDateTime parse(String dateStr);

    LocalDateTime parse(String dateStr, String pattern);

    LocalDate parseDate(String dateStr);

    LocalDate parseDate(String dateStr, String pattern);

    LocalTime parseTime(String timeStr);

    LocalTime parseTime(String timeStr, String pattern);

    LocalDateTime parseToLocalDateTime(String dateStr);

    LocalDateTime parseToLocalDateTime(String dateStr, String pattern);

    LocalDate parseToLocalDate(String dateStr);

    LocalDate parseToLocalDate(String dateStr, String pattern);

    LocalDateTime plusDays(LocalDateTime dateTime, long days);

    LocalDateTime plusHours(LocalDateTime dateTime, long hours);

    LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes);

    LocalDateTime plusSeconds(LocalDateTime dateTime, long seconds);

    LocalDateTime plusMonths(LocalDateTime dateTime, long months);

    LocalDateTime plusYears(LocalDateTime dateTime, long years);

    LocalDateTime minusDays(LocalDateTime dateTime, long days);

    LocalDateTime minusHours(LocalDateTime dateTime, long hours);

    LocalDateTime minusMinutes(LocalDateTime dateTime, long minutes);

    LocalDateTime minusSeconds(LocalDateTime dateTime, long seconds);

    LocalDateTime minusMonths(LocalDateTime dateTime, long months);

    LocalDateTime minusYears(LocalDateTime dateTime, long years);

    LocalDateTime yesterday();

    LocalDateTime tomorrow();

    LocalDateTime beginOfDay(LocalDateTime dateTime);

    LocalDateTime endOfDay(LocalDateTime dateTime);

    LocalDateTime beginOfWeek(LocalDateTime dateTime);

    LocalDateTime endOfWeek(LocalDateTime dateTime);

    LocalDateTime beginOfMonth(LocalDateTime dateTime);

    LocalDateTime endOfMonth(LocalDateTime dateTime);

    LocalDateTime beginOfQuarter(LocalDateTime dateTime);

    LocalDateTime endOfQuarter(LocalDateTime dateTime);

    LocalDateTime beginOfYear(LocalDateTime dateTime);

    LocalDateTime endOfYear(LocalDateTime dateTime);

    long betweenDays(LocalDateTime start, LocalDateTime end);

    long betweenHours(LocalDateTime start, LocalDateTime end);

    long betweenMinutes(LocalDateTime start, LocalDateTime end);

    long betweenSeconds(LocalDateTime start, LocalDateTime end);

    long betweenMonths(LocalDateTime start, LocalDateTime end);

    long betweenYears(LocalDateTime start, LocalDateTime end);

    boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2);

    boolean isSameDay(LocalDate date1, LocalDate date2);

    boolean isToday(LocalDateTime dateTime);

    boolean isToday(LocalDate date);

    boolean isYesterday(LocalDateTime dateTime);

    boolean isTomorrow(LocalDateTime dateTime);

    boolean isLeapYear(LocalDateTime dateTime);

    boolean isLeapYear(LocalDate date);

    boolean isLeapYear(int year);

    int getYear(LocalDateTime dateTime);

    int getYear(LocalDate date);

    int getMonth(LocalDateTime dateTime);

    int getMonth(LocalDate date);

    int getDayOfMonth(LocalDateTime dateTime);

    int getDayOfMonth(LocalDate date);

    int getDayOfWeek(LocalDateTime dateTime);

    int getDayOfWeek(LocalDate date);

    int getDayOfYear(LocalDateTime dateTime);

    int getDayOfYear(LocalDate date);

    int getHour(LocalDateTime dateTime);

    int getHour(LocalTime time);

    int getMinute(LocalDateTime dateTime);

    int getMinute(LocalTime time);

    int getSecond(LocalDateTime dateTime);

    int getSecond(LocalTime time);

    int getWeekOfMonth(LocalDateTime dateTime);

    int getWeekOfMonth(LocalDate date);

    int getWeekOfYear(LocalDateTime dateTime);

    int getWeekOfYear(LocalDate date);

    String getWeekName(LocalDateTime dateTime);

    String getWeekName(LocalDate date);

    String getChineseWeekName(LocalDateTime dateTime);

    String getChineseWeekName(LocalDate date);

    int getDaysInMonth(LocalDateTime dateTime);

    int getDaysInMonth(LocalDate date);

    int getDaysInMonth(int year, int month);

    int compare(LocalDateTime dateTime1, LocalDateTime dateTime2);

    int compare(LocalDate date1, LocalDate date2);

    boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2);

    boolean isBefore(LocalDate date1, LocalDate date2);

    boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2);

    boolean isAfter(LocalDate date1, LocalDate date2);

    boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2);

    boolean isEqual(LocalDate date1, LocalDate date2);

    boolean isBetween(LocalDateTime dateTime, LocalDateTime begin, LocalDateTime end);

    boolean isBetween(LocalDate date, LocalDate begin, LocalDate end);

    String formatDuration(long millis);

    boolean isValid(String dateStr, String pattern);

    boolean isValidDate(String dateStr);

    boolean isValidDateTime(String dateTimeStr);
}