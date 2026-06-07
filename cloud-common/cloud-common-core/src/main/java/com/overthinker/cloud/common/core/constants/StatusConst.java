package com.overthinker.cloud.common.core.constants;

/**
 * 状态码常量类
 * 定义系统中常用的状态码
 *
 * @author overthinker
 */
public class StatusConst {

    // ========== 操作结果状态码 ==========

    /** 成功状态码 */
    public static final String RC_200 = "200";

    /** 请求错误状态码 */
    public static final String RC_400 = "400";

    /** 未授权状态码 */
    public static final String RC_401 = "401";

    /** 禁止访问状态码 */
    public static final String RC_403 = "403";

    /** 资源未找到状态码 */
    public static final String RC_404 = "404";

    /** 请求方法不支持状态码 */
    public static final String RC_405 = "405";

    /** 请求超时状态码 */
    public static final String RC_408 = "408";

    /** 服务器内部错误状态码 */
    public static final String RC_500 = "500";

    /** 服务不可用状态码 */
    public static final String RC_503 = "503";

    /** 网关超时状态码 */
    public static final String RC_504 = "504";

    // ========== 业务自定义状态码 ==========

    /** 操作失败 */
    public static final String RC_999 = "999";

    /** 数学运算异常 */
    public static final String RC_375 = "375";

    /** 服务降级保护 */
    public static final String RC_201 = "201";

    /** 热点参数限流 */
    public static final String RC_202 = "202";

    /** 系统规则不满足 */
    public static final String RC_203 = "203";

    /** 授权规则不通过 */
    public static final String RC_204 = "204";

    // ========== 客户端认证相关状态码 ==========

    /** 客户端认证失败 */
    public static final String RC_1001 = "1001";

    /** 用户名或密码错误 */
    public static final String RC_1002 = "1002";

    /** 不支持的认证模式 */
    public static final String RC_1003 = "1003";

    /** 业务逻辑异常 */
    public static final String RC_1004 = "1004";

    /** 验证码错误 */
    public static final String RC_1005 = "1005";

    /** 用户名或邮箱已存在 */
    public static final String RC_1006 = "1006";

    /** 其他故障 */
    public static final String RC_1008 = "1008";

    /** 会话数量已达上限 */
    public static final String RC_1009 = "1009";

    /** 未删除子菜单 */
    public static final String RC_1010 = "1010";

    /** 账号被封禁 */
    public static final String RC_1012 = "1012";

    /** 服务器监控错误 */
    public static final String RC_1013 = "1013";

    /** 服务器SSE推送错误 */
    public static final String RC_1014 = "1014";

    /** 服务器SSE监控数据采集失败 */
    public static final String RC_1015 = "1015";

    /** 服务器SSE连接超时 */
    public static final String RC_1016 = "1016";

    /** 未识别到type类型 */
    public static final String RC_1017 = "1017";

    /** 访问令牌不合法 */
    public static final String RC_2001 = "2001";

    /** 没有权限访问该资源 */
    public static final String RC_2003 = "2003";

    // ========== 文件相关状态码 ==========

    /** 文件错误 */
    public static final String RC_4001 = "4001";

    /** 文件类型错误 */
    public static final String RC_4002 = "4002";

    /** 文件大小错误 */
    public static final String RC_4003 = "4003";

    /** 文件上传错误 */
    public static final String RC_4004 = "4004";

    // ========== 视频相关状态码 ==========

    /** 视频文件错误 */
    public static final String RC_5001 = "5001";

    /** 视频文件类型错误 */
    public static final String RC_5002 = "5002";

    /** 视频文件大小错误 */
    public static final String RC_5003 = "5003";

    /** 视频文件上传错误 */
    public static final String RC_5004 = "5004";

    /** 视频文件删除错误 */
    public static final String RC_5005 = "5005";

    // ========== 图片相关状态码 ==========

    /** 图片文件错误 */
    public static final String RC_6001 = "6001";

    /** 图片文件类型错误 */
    public static final String RC_6002 = "6002";

    /** 图片文件大小错误 */
    public static final String RC_6003 = "6003";

    /** 图片文件上传错误 */
    public static final String RC_6004 = "6004";

    /** 图片文件删除错误 */
    public static final String RC_6005 = "6005";

    private StatusConst() {
    }
}
