package com.overthinker.cloud.common.core.constants;

/**
 * 通用数字常量类
 * 提供 0-10 的基础常数以及业务常用数字，减少代码中的"魔法值"
 *
 * @author overthinker
 */
public class NumberConst {

    // --- 基础数字常量 ---
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";
    public static final String FOUR = "4";
    public static final String FIVE = "5";
    public static final String SIX = "6";
    public static final String SEVEN = "7";
    public static final String EIGHT = "8";
    public static final String NINE = "9";
    public static final String TEN = "10";

    // --- 常用业务数值 ---
    /** 默认成功标识 */
    public static final String SUCCESS_CODE = "200";
    /** 默认错误标识 */
    public static final String ERROR_CODE = "500";
    /** 百分比基数 */
    public static final String HUNDRED = "100";
    /** 千 (常用于金额转换) */
    public static final String THOUSAND = "1000";

    // --- 构造私有化 ---
    private NumberConst() {
    }
}
