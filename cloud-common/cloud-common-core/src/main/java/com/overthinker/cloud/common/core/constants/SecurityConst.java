package com.overthinker.cloud.common.core.constants;

/**
 * 安全相关常量类
 * 定义JWT、加密、权限等安全相关的常量
 *
 * @author overthinker
 */
public class SecurityConst {

    // ========== JWT相关常量 ==========

    /** JWT密钥属性名 */
    public static final String JWT_SECRET_KEY = "jwt.secret.key";

    /** JWT过期时间属性名 */
    public static final String JWT_EXPIRE_TIME = "jwt.expire.time";

    /** JWT Token前缀 */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    /** JWT Token请求头名称 */
    public static final String JWT_TOKEN_HEADER = "Authorization";

    /** JWT刷新Token请求头名称 */
    public static final String JWT_REFRESH_TOKEN_HEADER = "X-Refresh-Token";

    /** JWT签发者 */
    public static final String JWT_ISSUER = "cloud-system";

    /** JWT主题 */
    public static final String JWT_SUBJECT = "user-authentication";

    // ========== 加密相关常量 ==========

    /** AES加密算法 */
    public static final String AES_ALGORITHM = "AES";

    /** RSA加密算法 */
    public static final String RSA_ALGORITHM = "RSA";

    /** MD5算法 */
    public static final String MD5_ALGORITHM = "MD5";

    /** SHA-256算法 */
    public static final String SHA256_ALGORITHM = "SHA-256";

    /** AES密钥长度 */
    public static final int AES_KEY_SIZE = 128;

    /** RSA密钥长度 */
    public static final int RSA_KEY_SIZE = 2048;

    /** AES默认盐值 */
    public static final String AES_SALT = "cloud-common-salt";

    // ========== 权限相关常量 ==========

    /** 超级管理员角色标识 */
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

    /** 普通管理员角色标识 */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /** 普通用户角色标识 */
    public static final String ROLE_USER = "ROLE_USER";

    /** 访客角色标识 */
    public static final String ROLE_GUEST = "ROLE_GUEST";

    /** 匿名用户标识 */
    public static final String ANONYMOUS_USER = "anonymousUser";

    /** 登录用户标识 */
    public static final String LOGIN_USER = "loginUser";

    /** 管理员权限标识 */
    public static final String PERMISSION_ADMIN = "admin";

    /** 查询权限标识 */
    public static final String PERMISSION_QUERY = "query";

    /** 新增权限标识 */
    public static final String PERMISSION_ADD = "add";

    /** 修改权限标识 */
    public static final String PERMISSION_UPDATE = "update";

    /** 删除权限标识 */
    public static final String PERMISSION_DELETE = "delete";

    /** 导出权限标识 */
    public static final String PERMISSION_EXPORT = "export";

    /** 导入权限标识 */
    public static final String PERMISSION_IMPORT = "import";

    // ========== 请求头相关常量 ==========

    /** Token请求头 */
    public static final String TOKEN_HEADER = "Token";

    /** 用户ID请求头 */
    public static final String USER_ID_HEADER = "X-User-Id";

    /** 请求来源请求头 */
    public static final String REQUEST_SOURCE_HEADER = "X-Request-Source";

    /** 请求唯一ID请求头 */
    public static final String REQUEST_ID_HEADER = "X-Request-Id";

    private SecurityConst() {
    }
}
