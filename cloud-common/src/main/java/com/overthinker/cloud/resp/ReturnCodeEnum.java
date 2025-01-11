package com.overthinker.cloud.resp;


import lombok.Getter;

@Getter
public enum ReturnCodeEnum {
//    举值
    /**操作失败**/
    RC999(999,"操作XXX失败"),
    /**操作成功**/
    RC200(200,"success"),
    /**服务降级**/
    RC201(201,"服务开启降级保护,请稍后再试!"),
    /**热点参数限流**/
    RC202(202,"热点参数限流,请稍后再试!"),
    /**系统规则不满足**/
    RC203(203,"系统规则不满足要求,请稍后再试!"),
    /**授权规则不通过**/
    RC204(204,"授权规则不通过,请稍后再试!"),
    /**access_denied**/
    RC403(403,"无访问权限,请联系管理员授予权限"),
    /**access_denied**/
    RC401(401,"匿名用户访问无权限资源时的异常"),
    RC404(404,"404页面找不到的异常"),
    /**服务异常**/
    RC500(500,"系统异常，请稍后重试"),
    RC375(375,"数学运算异常，请稍后重试"),

    INVALID_TOKEN(2001,"访问令牌不合法"),
    ACCESS_DENIED(2003,"没有权限访问该资源"),
    CLIENT_AUTHENTICATION_FAILED(1001,"客户端认证失败"),
    USERNAME_OR_PASSWORD_ERROR(1002,"用户名或密码错误"),
    BUSINESS_ERROR(1004,"业务逻辑异常"),
    UNSUPPORTED_GRANT_TYPE(1003, "不支持的认证模式"),
    CAPTCHA_CHECK_ERROR(999,"验证码验证错误"),

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
     * 文件上传错误
     */
    FILE_UPLOAD_ERROR(1011, "文件上传错误"),
    /**
     * 账号被封禁
     */
    BLACK_LIST_ERROR(1012, "账号被封禁");

//    如何定义一个通用的枚举类，对于枚举编写
//    举值-构造-遍历
    private final Integer code;
    private final String message;

//    遍历
    ReturnCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
////    遍历
////    传统版遍历
//    public static ReturnCodeEnum getReturnCodeEnum(Integer code){
//        for (ReturnCodeEnum element : ReturnCodeEnum.values()) {
//            if (element.getCode().equalsIgnoreCase(code)){
//                return element;
//            }
//        }
//        return null;
//    }
////    stream流式计算版
//    public static ReturnCodeEnum getReturnCodeEnumByCode(Integer code){
//        return Arrays.stream(ReturnCodeEnum.values()).filter(element -> element.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
//    }


}
