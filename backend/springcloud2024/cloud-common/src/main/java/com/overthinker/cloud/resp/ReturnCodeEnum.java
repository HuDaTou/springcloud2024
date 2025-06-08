package com.overthinker.cloud.resp;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReturnCodeEnum {
//    举值
    /**
     * 操作失败
     **/
    RC999(999, "操作XXX失败"),
    /**
     * 操作成功
     **/
    RC200(200, "success"),
    /**
     * 服务降级
     **/
    RC201(201, "服务开启降级保护,请稍后再试!"),
    /**
     * 热点参数限流
     **/
    RC202(202, "热点参数限流,请稍后再试!"),
    /**
     * 系统规则不满足
     **/
    RC203(203, "系统规则不满足要求,请稍后再试!"),
    /**
     * 授权规则不通过
     **/
    RC204(204, "授权规则不通过,请稍后再试!"),
    /**
     * access_denied
     **/
    RC403(403, "无访问权限,请联系管理员授予权限"),
    /**
     * access_denied
     **/
    RC401(401, "匿名用户访问无权限资源时的异常"),
    RC404(404, "404页面找不到的异常"),
    /**
     * 服务异常
     **/
    RC500(500, "系统异常，请稍后重试"),
    RC375(375, "数学运算异常，请稍后重试"),

    INVALID_TOKEN(2001, "访问令牌不合法"),
    ACCESS_DENIED(2003, "没有权限访问该资源"),
    CLIENT_AUTHENTICATION_FAILED(1001, "客户端认证失败"),
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    BUSINESS_ERROR(1004, "业务逻辑异常"),
    UNSUPPORTED_GRANT_TYPE(1003, "不支持的认证模式"),
    CAPTCHA_CHECK_ERROR(999, "验证码验证错误"),

    /**
     * 请求成功
     */
    SUCCESS(200, "success"),
    /**
     * 请求失败
     */
    FAILURE(500, "failure"),

    /**
     * 未登录提示
     */
    NOT_LOGIN(1002, "请先登录"),
    /**
     * 没有权限
     */
    NO_PERMISSION(1003, "没有权限"),
    /**
     * 请求频繁
     */
    REQUEST_FREQUENTLY(1004, "请求频繁"),
    /**
     * 验证码错误
     */
    VERIFY_CODE_ERROR(1005, "验证码错误"),
    /**
     * 用户名或邮箱已存在
     */
    USERNAME_OR_EMAIL_EXIST(1006, "用户名或邮箱已存在"),
    /**
     * 参数错误提示
     */
    PARAM_ERROR(1007, "参数错误"),
    /**
     * 其他故障
     */
    OTHER_ERROR(1008, "其他故障"),
    /**
     * 会话数量已达上限
     */
    SESSION_LIMIT(1009, "会话数量已达上限"),
    /**
     * 未删除子菜单
     */
    NO_DELETE_CHILD_MENU(1010, "请先删除子菜单"),

    /**
     * 账号被封禁
     */
    BLACK_LIST_ERROR(1012, "账号被封禁"),


    SERVER_ERROR(1013, "服务器监控错误"),
    SERVER_SSE_PUSH_ERROR(1014, "服务器SSE推送错误"),
    SERVER_SSE_MONITORING_DATA_COLLECTION_FAILED(1015, "服务器SSE监控数据采集失败"),
    SERVER_SSE_CONNECTION_TIMEOUT(1016, "服务器SSE连接超时"),


    //    文件错误
    FILE_ERROR(1001, "文件错误"),
    FILE_TYPE_ERROR(1002, "文件类型错误"),
    FILE_SIZE_ERROR(1003, "文件大小错误"),
    FILE_UPLOAD_ERROR(1004, "文件上传错误"),
    //    视频错误
    FILE_VIDEO_ERROR(2001, "视频文件错误"),
    FILE_VIDEO_TYPE_ERROR(2002, "视频文件类型错误"),
    FILE_VIDEO_SIZE_ERROR(2003, "视频文件大小错误"),
    FILE_VIDEO_UPLOAD_ERROR(2004, "视频文件上传错误"),
    FILE_VIDEO_DELETE_ERROR(2005, "视频文件删除错误"),
    //    图片错误
    FILE_IMAGE_ERROR(3001, "图片文件错误"),
    FILE_IMAGE_TYPE_ERROR(3002, "图片文件类型错误"),
    FILE_IMAGE_SIZE_ERROR(3003, "图片文件大小错误"),
    FILE_IMAGE_UPLOAD_ERROR(3004, "图片文件上传错误"),
    FILE_IMAGE_DELETE_ERROR(3005, "图片文件删除错误"),

    ;

    //    如何定义一个通用的枚举类，对于枚举编写
//    举值-构造-遍历
    private final Integer code;
    private final String msg;

    //    遍历
    ReturnCodeEnum(Integer code, String meg) {
        this.code = code;
        this.msg = meg;
    }
////    遍历

    /// /    stream流式计算版
    public static ReturnCodeEnum getReturnCodeEnumByCode(Integer code) {
        return Arrays.stream(ReturnCodeEnum.values()).filter(element -> element.getCode().equals(code)).findFirst().orElse(null);
    }

    public static ReturnCodeEnum getReturnCodeEnumBymsg(String msg) {
        return Arrays.stream(ReturnCodeEnum.values())
                .filter(element -> element.getMsg().equalsIgnoreCase(msg))
                .findFirst()
                .orElse(null);
    }


}
