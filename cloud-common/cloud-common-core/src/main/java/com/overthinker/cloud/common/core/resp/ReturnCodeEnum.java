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
    RC999(StatusConst.OPERATION_FAILED, "操作XXX失败"),
    /**
     * 操作成功
     **/
    RC200(StatusConst.SUCCESS, "success"),
    /**
     * 服务降级
     **/
    RC201(StatusConst.SERVICE_DEGRADATION, "服务开启降级保护,请稍后再试!"),
    /**
     * 热点参数限流
     **/
    RC202(StatusConst.HOT_PARAM_RATE_LIMIT, "热点参数限流,请稍后再试!"),
    /**
     * 系统规则不满足
     **/
    RC203(StatusConst.SYSTEM_RULE_NOT_SATISFIED, "系统规则不满足要求,请稍后再试!"),
    /**
     * 授权规则不通过
     **/
    RC204(StatusConst.AUTH_RULE_NOT_PASSED, "授权规则不通过,请稍后再试!"),
    /**
     * access_denied
     **/
    RC403(StatusConst.FORBIDDEN, "无访问权限,请联系管理员授予权限"),
    /**
     * access_denied
     **/
    RC401(StatusConst.UNAUTHORIZED, "匿名用户访问无权限资源时的异常"),
    RC404(StatusConst.NOT_FOUND, "404页面找不到的异常"),
    /**
     * 服务异常
     **/
    RC500(StatusConst.INTERNAL_SERVER_ERROR, "系统异常，请稍后重试"),
    RC375(StatusConst.MATH_OPERATION_ERROR, "数学运算异常，请稍后重试"),

    INVALID_TOKEN(StatusConst.INVALID_TOKEN, "访问令牌不合法"),
    ACCESS_DENIED(StatusConst.NO_ACCESS_PERMISSION, "没有权限访问该资源"),
    CLIENT_AUTHENTICATION_FAILED(StatusConst.CLIENT_AUTH_FAILED, "客户端认证失败"),
    USERNAME_OR_PASSWORD_ERROR(StatusConst.USERNAME_OR_PASSWORD_ERROR, "用户名或密码错误"),
    BUSINESS_ERROR(StatusConst.BUSINESS_ERROR, "业务逻辑异常"),
    UNSUPPORTED_GRANT_TYPE(StatusConst.UNSUPPORTED_GRANT_TYPE, "不支持的认证模式"),
    CAPTCHA_CHECK_ERROR(StatusConst.VERIFY_CODE_ERROR, "验证码验证错误"),

    /**
     * 请求成功
     */
    SUCCESS(StatusConst.SUCCESS, "success"),
    /**
     * 请求失败
     */
    FAILURE(StatusConst.INTERNAL_SERVER_ERROR, "failure"),

    /**
     * 未登录提示
     */
    NOT_LOGIN(StatusConst.USERNAME_OR_PASSWORD_ERROR, "请先登录"),
    /**
     * 没有权限
     */
    NO_PERMISSION(StatusConst.FORBIDDEN, "没有权限"),
    /**
     * 请求频繁
     */
    REQUEST_FREQUENTLY(StatusConst.BUSINESS_ERROR, "请求频繁"),
    /**
     * 验证码错误
     */
    VERIFY_CODE_ERROR(StatusConst.VERIFY_CODE_ERROR, "验证码错误"),
    /**
     * 用户名或邮箱已存在
     */
    USERNAME_OR_EMAIL_EXIST(StatusConst.USERNAME_OR_EMAIL_EXIST, "用户名或邮箱已存在"),
    /**
     * 参数错误提示
     */
    PARAM_ERROR(StatusConst.BAD_REQUEST, "参数错误"),
    /**
     * 其他故障
     */
    OTHER_ERROR(StatusConst.OTHER_ERROR, "其他故障"),
    /**
     * 会话数量已达上限
     */
    SESSION_LIMIT(StatusConst.SESSION_LIMIT, "会话数量已达上限"),
    /**
     * 未删除子菜单
     */
    NO_DELETE_CHILD_MENU(StatusConst.CHILD_MENU_NOT_DELETED, "请先删除子菜单"),

    /**
     * 账号被封禁
     */
    BLACK_LIST_ERROR(StatusConst.ACCOUNT_BANNED, "账号被封禁"),


    SERVER_ERROR(StatusConst.SERVER_MONITOR_ERROR, "服务器监控错误"),
    SERVER_SSE_PUSH_ERROR(StatusConst.SERVER_SSE_PUSH_ERROR, "服务器SSE推送错误"),
    SERVER_SSE_MONITORING_DATA_COLLECTION_FAILED(StatusConst.SERVER_SSE_MONITOR_DATA_COLLECT_ERROR, "服务器SSE监控数据采集失败"),
    SERVER_SSE_CONNECTION_TIMEOUT(StatusConst.SERVER_SSE_CONNECTION_TIMEOUT, "服务器SSE连接超时"),


    // ========== 文件错误 ==========
    FILE_ERROR(StatusConst.FILE_ERROR, "文件错误"),
    FILE_TYPE_ERROR(StatusConst.FILE_TYPE_ERROR, "文件类型错误"),
    FILE_SIZE_ERROR(StatusConst.FILE_SIZE_ERROR, "文件大小错误"),
    FILE_UPLOAD_ERROR(StatusConst.FILE_UPLOAD_ERROR, "文件上传错误"),

    // ========== 视频错误 ==========
    FILE_VIDEO_ERROR(StatusConst.VIDEO_ERROR, "视频文件错误"),
    FILE_VIDEO_TYPE_ERROR(StatusConst.VIDEO_TYPE_ERROR, "视频文件类型错误"),
    FILE_VIDEO_SIZE_ERROR(StatusConst.VIDEO_SIZE_ERROR, "视频文件大小错误"),
    FILE_VIDEO_UPLOAD_ERROR(StatusConst.VIDEO_UPLOAD_ERROR, "视频文件上传错误"),
    FILE_VIDEO_DELETE_ERROR(StatusConst.VIDEO_DELETE_ERROR, "视频文件删除错误"),

    // ========== 图片错误 ==========
    FILE_IMAGE_ERROR(StatusConst.IMAGE_ERROR, "图片文件错误"),
    FILE_IMAGE_TYPE_ERROR(StatusConst.IMAGE_TYPE_ERROR, "图片文件类型错误"),
    FILE_IMAGE_SIZE_ERROR(StatusConst.IMAGE_SIZE_ERROR, "图片文件大小错误"),
    FILE_IMAGE_UPLOAD_ERROR(StatusConst.IMAGE_UPLOAD_ERROR, "图片文件上传错误"),
    FILE_IMAGE_DELETE_ERROR(StatusConst.IMAGE_DELETE_ERROR, "图片文件删除错误"),

    /**
     * 未识别到type类型
     */
    TYPE_NOT_RECOGNIZED(StatusConst.TYPE_NOT_RECOGNIZED, "未识别到type类型"),

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
