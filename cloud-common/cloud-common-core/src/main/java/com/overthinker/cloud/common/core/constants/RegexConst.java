package com.overthinker.cloud.common.core.constants;

/**
 * 常用正则表达式常量类
 * 提供常用的正则表达式模式，用于输入验证和数据校验
 *
 * @author overthinker
 */
public class RegexConst {

    /** 手机号正则（中国大陆） */
    public static final String PHONE = "^1[3-9]\\d{9}$";

    /** 电子邮箱正则 */
    public static final String EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /** 身份证号正则（中国大陆，18位） */
    public static final String ID_CARD = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$";

    /** URL正则 */
    public static final String URL = "^https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";

    /** IP地址正则（IPv4） */
    public static final String IP_V4 = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

    /** 邮政编码正则（中国大陆） */
    public static final String POSTAL_CODE = "^[1-9]\\d{5}$";

    /** 用户名正则（字母开头，4-16位） */
    public static final String USERNAME = "^[a-zA-Z][a-zA-Z0-9_]{3,15}$";

    /** 密码强度正则（至少包含大小写字母和数字，8-20位） */
    public static final String PASSWORD_STRONG = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&_]{8,20}$";

    /** 密码正则（6-20位字母或数字） */
    public static final String PASSWORD = "^[a-zA-Z0-9]{6,20}$";

    /** 中文姓名正则 */
    public static final String CHINESE_NAME = "^[\\u4e00-\\u9fa5]{2,10}$";

    /** 银行卡号正则（16-19位数字） */
    public static final String BANK_CARD = "^[0-9]{16,19}$";

    /** 整数正则 */
    public static final String INTEGER = "^-?\\d+$";

    /** 正整数正则 */
    public static final String POSITIVE_INTEGER = "^\\d+$";

    /** 负整数正则 */
    public static final String NEGATIVE_INTEGER = "^-\\d+$";

    /** 浮点数正则 */
    public static final String DECIMAL = "^-?\\d+(\\.\\d+)?$";

    /** 正浮点数正则 */
    public static final String POSITIVE_DECIMAL = "^\\d+(\\.\\d+)?$";

    /** 昵称正则（字母、数字、汉字、下划线，2-20位） */
    public static final String NICKNAME = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,20}$";

    /** 车牌号正则 */
    public static final String CAR_LICENSE = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4,5}[A-Z0-9挂学警港澳]$";

    private RegexConst() {
    }
}
