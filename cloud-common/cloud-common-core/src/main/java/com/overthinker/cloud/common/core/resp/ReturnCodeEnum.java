package com.overthinker.cloud.common.core.resp;


import com.overthinker.cloud.common.core.constants.StatusConst;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReturnCodeEnum {

    // ========== 操作失败/成功 ==========
    /**
     * 操作失败
     **/
    OPERATION_FAILED(StatusConst.RC_999, "操作XXX失败"),
    /**
     * 操作成功
     **/
    SUCCESS(StatusConst.RC_200, "success"),
    /**
     * 服务降级
     **/
    SERVICE_DEGRADATION(StatusConst.RC_201, "服务开启降级保护,请稍后再试!"),
    /**
     * 热点参数限流
     **/
    HOT_PARAM_RATE_LIMIT(StatusConst.RC_202, "热点参数限流,请稍后再试!"),
    /**
     * 系统规则不满足
     **/
    SYSTEM_RULE_NOT_SATISFIED(StatusConst.RC_203, "系统规则不满足要求,请稍后再试!"),
    /**
     * 授权规则不通过
     **/
    AUTH_RULE_NOT_PASSED(StatusConst.RC_204, "授权规则不通过,请稍后再试!"),
    /**
     * access_denied
     **/
    FORBIDDEN(StatusConst.RC_403, "无访问权限,请联系管理员授予权限"),
    /**
     * access_denied
     **/
    UNAUTHORIZED(StatusConst.RC_401, "匿名用户访问无权限资源时的异常"),
    NOT_FOUND(StatusConst.RC_404, "404页面找不到的异常"),
    /**
     * 服务异常
     **/
    INTERNAL_SERVER_ERROR(StatusConst.RC_500, "系统异常，请稍后重试"),
    MATH_OPERATION_ERROR(StatusConst.RC_375, "数学运算异常，请稍后重试"),

    INVALID_TOKEN(StatusConst.RC_2001, "访问令牌不合法"),
    ACCESS_DENIED(StatusConst.RC_2003, "没有权限访问该资源"),
    CLIENT_AUTHENTICATION_FAILED(StatusConst.RC_1001, "客户端认证失败"),
    USERNAME_OR_PASSWORD_ERROR(StatusConst.RC_1002, "用户名或密码错误"),
    BUSINESS_ERROR(StatusConst.RC_1004, "业务逻辑异常"),
    UNSUPPORTED_GRANT_TYPE(StatusConst.RC_1003, "不支持的认证模式"),
    CAPTCHA_CHECK_ERROR(StatusConst.RC_1005, "验证码验证错误"),

    /**
     * 请求失败
     */
    FAILURE(StatusConst.RC_500, "failure"),

    /**
     * 未登录提示
     */
    NOT_LOGIN(StatusConst.RC_1002, "请先登录"),
    /**
     * 没有权限
     */
    NO_PERMISSION(StatusConst.RC_403, "没有权限"),
    /**
     * 请求频繁
     */
    REQUEST_FREQUENTLY(StatusConst.RC_1004, "请求频繁"),
    /**
     * 验证码错误
     */
    VERIFY_CODE_ERROR(StatusConst.RC_1005, "验证码错误"),
    /**
     * 用户名或邮箱已存在
     */
    USERNAME_OR_EMAIL_EXIST(StatusConst.RC_1006, "用户名或邮箱已存在"),
    /**
     * 参数错误提示
     */
    PARAM_ERROR(StatusConst.RC_400, "参数错误"),
    /**
     * 其他故障
     */
    OTHER_ERROR(StatusConst.RC_1008, "其他故障"),
    /**
     * 会话数量已达上限
     */
    SESSION_LIMIT(StatusConst.RC_1009, "会话数量已达上限"),
    /**
     * 未删除子菜单
     */
    NO_DELETE_CHILD_MENU(StatusConst.RC_1010, "请先删除子菜单"),

    /**
     * 账号被封禁
     */
    BLACK_LIST_ERROR(StatusConst.RC_1012, "账号被封禁"),


    SERVER_ERROR(StatusConst.RC_1013, "服务器监控错误"),
    SERVER_SSE_PUSH_ERROR(StatusConst.RC_1014, "服务器SSE推送错误"),
    SERVER_SSE_MONITORING_DATA_COLLECTION_FAILED(StatusConst.RC_1015, "服务器SSE监控数据采集失败"),
    SERVER_SSE_CONNECTION_TIMEOUT(StatusConst.RC_1016, "服务器SSE连接超时"),


    // ========== 文件错误 ==========
    FILE_ERROR(StatusConst.RC_4001, "文件错误"),
    FILE_TYPE_ERROR(StatusConst.RC_4002, "文件类型错误"),
    FILE_SIZE_ERROR(StatusConst.RC_4003, "文件大小错误"),
    FILE_UPLOAD_ERROR(StatusConst.RC_4004, "文件上传错误"),

    // ========== 视频错误 ==========
    FILE_VIDEO_ERROR(StatusConst.RC_5001, "视频文件错误"),
    FILE_VIDEO_TYPE_ERROR(StatusConst.RC_5002, "视频文件类型错误"),
    FILE_VIDEO_SIZE_ERROR(StatusConst.RC_5003, "视频文件大小错误"),
    FILE_VIDEO_UPLOAD_ERROR(StatusConst.RC_5004, "视频文件上传错误"),
    FILE_VIDEO_DELETE_ERROR(StatusConst.RC_5005, "视频文件删除错误"),

    // ========== 图片错误 ==========
    FILE_IMAGE_ERROR(StatusConst.RC_6001, "图片文件错误"),
    FILE_IMAGE_TYPE_ERROR(StatusConst.RC_6002, "图片文件类型错误"),
    FILE_IMAGE_SIZE_ERROR(StatusConst.RC_6003, "图片文件大小错误"),
    FILE_IMAGE_UPLOAD_ERROR(StatusConst.RC_6004, "图片文件上传错误"),
    FILE_IMAGE_DELETE_ERROR(StatusConst.RC_6005, "图片文件删除错误"),

    /**
     * 未识别到type类型
     */
    TYPE_NOT_RECOGNIZED(StatusConst.RC_1017, "未识别到type类型"),

    ;

    private final String code;
    private final String msg;

    ReturnCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ReturnCodeEnum getReturnCodeEnumByCode(String code) {
        return Arrays.stream(ReturnCodeEnum.values()).filter(element -> element.getCode().equals(code)).findFirst().orElse(null);
    }

    public static ReturnCodeEnum getReturnCodeEnumBymsg(String msg) {
        return Arrays.stream(ReturnCodeEnum.values())
                .filter(element -> element.getMsg().equalsIgnoreCase(msg))
                .findFirst()
                .orElse(null);
    }


}
