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
    public static final String SUCCESS = "200";

    /** 请求错误状态码 */
    public static final String BAD_REQUEST = "400";

    /** 未授权状态码 */
    public static final String UNAUTHORIZED = "401";

    /** 禁止访问状态码 */
    public static final String FORBIDDEN = "403";

    /** 资源未找到状态码 */
    public static final String NOT_FOUND = "404";

    /** 请求方法不支持状态码 */
    public static final String METHOD_NOT_ALLOWED = "405";

    /** 请求超时状态码 */
    public static final String REQUEST_TIMEOUT = "408";

    /** 服务器内部错误状态码 */
    public static final String INTERNAL_SERVER_ERROR = "500";

    /** 服务不可用状态码 */
    public static final String SERVICE_UNAVAILABLE = "503";

    /** 网关超时状态码 */
    public static final String GATEWAY_TIMEOUT = "504";

    // ========== 业务自定义状态码 ==========

    /** 操作失败 */
    public static final String OPERATION_FAILED = "999";

    /** 数学运算异常 */
    public static final String MATH_OPERATION_ERROR = "375";

    /** 服务降级保护 */
    public static final String SERVICE_DEGRADATION = "201";

    /** 热点参数限流 */
    public static final String HOT_PARAM_RATE_LIMIT = "202";

    /** 系统规则不满足 */
    public static final String SYSTEM_RULE_NOT_SATISFIED = "203";

    /** 授权规则不通过 */
    public static final String AUTH_RULE_NOT_PASSED = "204";

    // ========== 客户端认证相关状态码 ==========

    /** 客户端认证失败 */
    public static final String CLIENT_AUTH_FAILED = "1001";

    /** 用户名或密码错误 */
    public static final String USERNAME_OR_PASSWORD_ERROR = "1002";

    /** 不支持的认证模式 */
    public static final String UNSUPPORTED_GRANT_TYPE = "1003";

    /** 业务逻辑异常 */
    public static final String BUSINESS_ERROR = "1004";

    /** 验证码错误 */
    public static final String VERIFY_CODE_ERROR = "1005";

    /** 用户名或邮箱已存在 */
    public static final String USERNAME_OR_EMAIL_EXIST = "1006";

    /** 其他故障 */
    public static final String OTHER_ERROR = "1008";

    /** 会话数量已达上限 */
    public static final String SESSION_LIMIT = "1009";

    /** 未删除子菜单 */
    public static final String CHILD_MENU_NOT_DELETED = "1010";

    /** 账号被封禁 */
    public static final String ACCOUNT_BANNED = "1012";

    /** 服务器监控错误 */
    public static final String SERVER_MONITOR_ERROR = "1013";

    /** 服务器SSE推送错误 */
    public static final String SERVER_SSE_PUSH_ERROR = "1014";

    /** 服务器SSE监控数据采集失败 */
    public static final String SERVER_SSE_MONITOR_DATA_COLLECT_ERROR = "1015";

    /** 服务器SSE连接超时 */
    public static final String SERVER_SSE_CONNECTION_TIMEOUT = "1016";

    /** 未识别到type类型 */
    public static final String TYPE_NOT_RECOGNIZED = "1017";

    /** 访问令牌不合法 */
    public static final String INVALID_TOKEN = "2001";

    /** 没有权限访问该资源 */
    public static final String NO_ACCESS_PERMISSION = "2003";

    // ========== 文件相关状态码 ==========

    /** 文件错误 */
    public static final String FILE_ERROR = "4001";

    /** 文件类型错误 */
    public static final String FILE_TYPE_ERROR = "4002";

    /** 文件大小错误 */
    public static final String FILE_SIZE_ERROR = "4003";

    /** 文件上传错误 */
    public static final String FILE_UPLOAD_ERROR = "4004";

    // ========== 视频相关状态码 ==========

    /** 视频文件错误 */
    public static final String VIDEO_ERROR = "5001";

    /** 视频文件类型错误 */
    public static final String VIDEO_TYPE_ERROR = "5002";

    /** 视频文件大小错误 */
    public static final String VIDEO_SIZE_ERROR = "5003";

    /** 视频文件上传错误 */
    public static final String VIDEO_UPLOAD_ERROR = "5004";

    /** 视频文件删除错误 */
    public static final String VIDEO_DELETE_ERROR = "5005";

    // ========== 图片相关状态码 ==========

    /** 图片文件错误 */
    public static final String IMAGE_ERROR = "6001";

    /** 图片文件类型错误 */
    public static final String IMAGE_TYPE_ERROR = "6002";

    /** 图片文件大小错误 */
    public static final String IMAGE_SIZE_ERROR = "6003";

    /** 图片文件上传错误 */
    public static final String IMAGE_UPLOAD_ERROR = "6004";

    /** 图片文件删除错误 */
    public static final String IMAGE_DELETE_ERROR = "6005";

    private StatusConst() {
    }
}
