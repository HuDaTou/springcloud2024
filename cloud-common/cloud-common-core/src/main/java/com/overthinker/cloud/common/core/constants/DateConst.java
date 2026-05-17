package com.overthinker.cloud.common.core.constants;

/**
 * 日期时间格式常量类
 * 提供常用的日期时间格式常量，减少代码中的“魔法字符串”
 *
 * @author overthinker
 */
public class DateConst {

    /** 标准日期格式：yyyy-MM-dd */
    public static final String DATE = "yyyy-MM-dd";

    /** 标准时间格式：HH:mm:ss */
    public static final String TIME = "HH:mm:ss";

    /** 标准日期时间格式：yyyy-MM-dd HH:mm:ss */
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /** 标准日期时间格式（无连接符）：yyyyMMddHHmmss */
    public static final String DATE_TIME_COMPACT = "yyyyMMddHHmmss";

    /** 日期格式（无连接符）：yyyyMMdd */
    public static final String DATE_COMPACT = "yyyyMMdd";

    /** 月份格式：yyyy-MM */
    public static final String YEAR_MONTH = "yyyy-MM";

    /** 月份格式（无连接符）：yyyyMM */
    public static final String YEAR_MONTH_COMPACT = "yyyyMM";

    /** 小时分钟格式：HH:mm */
    public static final String HOUR_MINUTE = "HH:mm";

    /** 中文日期格式：yyyy年MM月dd日 */
    public static final String DATE_CN = "yyyy年MM月dd日";

    /** 中文日期时间格式：yyyy年MM月dd日 HH时mm分ss秒 */
    public static final String DATE_TIME_CN = "yyyy年MM月dd日 HH时mm分ss秒";

    /** ISO标准格式：yyyy-MM-dd'T'HH:mm:ss.SSS'Z' */
    public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /** 12小时制时间格式：hh:mm:ss */
    public static final String TIME_12H = "hh:mm:ss";

    /** 日期时间格式（毫秒）：yyyy-MM-dd HH:mm:ss.SSS */
    public static final String DATE_TIME_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";

    private DateConst() {
    }
}
