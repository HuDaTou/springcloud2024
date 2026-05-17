# JWT 技术详解

## 目录

- [1. JWT 基础概念](#1-jwt-基礎概念)
- [2. JWT vs JWS vs JWE](#2-jwt-vs-jws-vs-jwe)
- [3. JWK 公钥发布](#3-jwk-公钥发布)
- [4. 项目中的实现方案](#4-项目中的实现方案)
- [5. 密钥分配原则](#5-密钥分配原则)
- [6. 常见问题](#6-常见问题)

---

## 1. JWT 基础概念

### 1.1 什么是 JWT？

**JWT（JSON Web Token）** 是一种开放标准（RFC 7519），用于在各方之间安全地传输信息作为 JSON 对象。

**JWT 的结构：**
```
Header.Payload.Signature
```

**示例：**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

### 1.2 JWT 的组成

| 部分 | 说明 | 示例 |
|------|------|------|
| **Header** | 头部，声明类型和算法 | `{"alg": "RS256", "typ": "JWT"}` |
| **Payload** | 载荷，包含声明（Claims） | `{"sub": "admin", "user_id": 1}` |
| **Signature** | 签名，防止篡改 | 使用私钥签名 |

### 1.3 Header（头部）详解

Header 通常包含两个字段：

| 字段 | 说明 | 取值 |
|------|------|------|
| `alg` | 签名算法 | HS256、RS256、ES256 等 |
| `typ` | Token 类型 | `JWT` |
| `kid` | 密钥 ID（可选） | 用于匹配公钥 |

**示例：**
```json
{
  "alg": "RS256",
  "typ": "JWT",
  "kid": "cloud-auth-key"
}
```

### 1.4 Payload（载荷）详解

Payload 包含 Claims（声明），分为三类：

#### 1.4.1 注册声明（Registered Claims）

RFC 7519 预定义的标准字段：

| 字段 | 全称 | 说明 | 是否必需 |
|------|------|------|----------|
| `iss` | Issuer | 签发者 | 否 |
| `sub` | Subject | 主题（通常是用户 ID） | 否 |
| `aud` | Audience | 受众 | 否 |
| `exp` | Expiration Time | 过期时间（Unix 时间戳） | 否 |
| `nbf` | Not Before | 生效时间 | 否 |
| `iat` | Issued At | 签发时间 | 否 |
| `jti` | JWT ID | Token 唯一标识 | 否 |

**示例：**
```json
{
  "iss": "http://localhost:9123",
  "sub": "admin",
  "exp": 1715587600,
  "iat": 1715585800
}
```

#### 1.4.2 公共声明（Public Claims）

自定义但需要避免冲突的字段：

| 字段 | 说明 |
|------|------|
| `name` | 姓名 |
| `email` | 邮箱 |
| `picture` | 头像 |
| `locale` | 语言环境 |
| `zoneinfo` | 时区 |

**示例：**
```json
{
  "name": "张三",
  "email": "zhangsan@example.com",
  "picture": "https://example.com/avatar.jpg"
}
```

#### 1.4.3 私有声明（Private Claims）

项目自定义的字段：

| 字段 | 说明 |
|------|------|
| `user_id` | 用户 ID |
| `username` | 用户名 |
| `authorities` | 权限列表 |
| `dept_id` | 部门 ID |
| `tenant_id` | 租户 ID |

**示例（项目中的用法）：**
```json
{
  "user_id": 1,
  "username": "admin",
  "authorities": ["ROLE_ADMIN", "ROLE_USER"]
}
```

#### 1.4.4 完整 Payload 示例（项目实际）

```json
{
  "iss": "http://localhost:9123",
  "sub": "admin",
  "exp": 1715587600,
  "iat": 1715585800,
  "user_id": 1,
  "username": "admin",
  "authorities": ["ROLE_ADMIN", "ROLE_USER"]
}
```

### 1.5 Signature（签名）详解

#### 1.5.1 签名的作用

| 作用 | 说明 |
|------|------|
| **防篡改** | Token 内容被修改后，签名验证会失败 |
| **验证来源** | 确认 Token 是由可信的服务签发的 |
| **完整性保护** | 确保 Token 在传输过程中未被修改 |

#### 1.5.2 签名算法对比

| 算法 | 类型 | 说明 | 安全性 | 性能 |
|------|------|------|--------|------|
| **HS256** | 对称加密 | 使用同一密钥签名和验证 | ⭐⭐ | ⭐⭐⭐⭐ |
| **RS256** | 非对称加密 | 私钥签名，公钥验证 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| **ES256** | 椭圆曲线 | 私钥签名，公钥验证 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |

**推荐：** RS256（项目当前方案）

#### 1.5.3 RS256 签名过程详解

**步骤 1：准备待签名的内容**

```
Base64UrlEncode(Header) + "." + Base64UrlEncode(Payload)
```

**示例：**
```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfaWQiOjEsImlhdCI6MTcxNTU4NTgwMCwiZXhwIjoxNzE1NTg3NjAwfQ
```

**步骤 2：使用私钥进行签名**

```
Signature = RSASHA256(待签名内容, 私钥)
```

**步骤 3：对签名进行 Base64URL 编码**

```
Base64UrlEncode(Signature)
```

**完整过程图解：**
```
┌─────────────────────────────────────────────────────────────┐
│  第一步：构建待签名内容                                      │
│                                                              │
│  Header (JSON)     ──→ Base64UrlEncode ──→ "eyJhbGci..."    │
│  Payload (JSON)    ──→ Base64UrlEncode ──→ "eyJzdWIi..."    │
│                                                              │
│  待签名内容 = "eyJhbGci..." + "." + "eyJzdWIi..."           │
└────────────────────────────────────┬─────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────┐
│  第二步：使用私钥签名                                        │
│                                                              │
│  RSASHA256(待签名内容, 私钥) ──→ 二进制签名数据              │
└────────────────────────────────────┬─────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────┐
│  第三步：对签名进行 Base64URL 编码                           │
│                                                              │
│  Base64UrlEncode(二进制签名数据) ──→ "SflKxwRJSMeKKF..."    │
└────────────────────────────────────┬─────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────┐
│  第四步：组合成完整的 JWS Token                              │
│                                                              │
│  Token = Header + "." + Payload + "." + Signature           │
└─────────────────────────────────────────────────────────────┘
```

**代码示例（RS256 签名）：**
```java
// 1. 准备 Header
Map<String, Object> headers = new HashMap<>();
headers.put("alg", "RS256");
headers.put("typ", "JWT");

// 2. 准备 Payload
Map<String, Object> payload = new HashMap<>();
payload.put("sub", "admin");
payload.put("user_id", 1);
payload.put("iat", Instant.now().getEpochSecond());
payload.put("exp", Instant.now().plus(30, ChronoUnit.MINUTES).getEpochSecond());

// 3. 编码
String headerBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(
    objectMapper.writeValueAsString(headers).getBytes()
);
String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(
    objectMapper.writeValueAsString(payload).getBytes()
);
String contentToSign = headerBase64 + "." + payloadBase64;

// 4. 签名
Signature signature = Signature.getInstance("SHA256withRSA");
signature.initSign(privateKey);
signature.update(contentToSign.getBytes());
byte[] signatureBytes = signature.sign();
String signatureBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

// 5. 组合 Token
String token = headerBase64 + "." + payloadBase64 + "." + signatureBase64;
```

#### 1.5.4 RS256 验证过程详解

**步骤 1：解析 Token 并提取各部分**

```
拆分 Token 为 Header、Payload、Signature
```

**步骤 2：构建待验证内容**

```
待验证内容 = Base64UrlEncode(Header) + "." + Base64UrlEncode(Payload)
```

**步骤 3：使用公钥验证签名**

```
验证签名 = RSASHA256(待验证内容, 公钥)
```

**步骤 4：比较签名**

```
如果验证的签名与 Token 中的签名一致，验证通过
否则，Token 被篡改，验证失败
```

**完整过程图解：**
```
┌─────────────────────────────────────────────────────────────┐
│  第一步：解析 Token                                          │
│                                                              │
│  Token = "header.payload.signature"                          │
│  ↓ 拆分                                                     │
│  Header = "eyJhbGci..."                                     │
│  Payload = "eyJzdWIi..."                                    │
│  Signature = "SflKxwRJSMeKKF..."                             │
└────────────────────────────────────┬─────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────┐
│  第二步：构建待验证内容                                      │
│                                                              │
│  待验证内容 = Header + "." + Payload                         │
└────────────────────────────────────┬─────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────┐
│  第三步：从 /oauth2/jwks 获取公钥                            │
│                                                              │
│  JWK Set ──→ 提取公钥 (n, e) ──→ 构建 RSA Public Key         │
└────────────────────────────────────┬─────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────┐
│  第四步：使用公钥验证签名                                    │
│                                                              │
│  1. 对 Signature 进行 Base64URL 解码，得到二进制签名         │
│  2. 使用公钥验证签名：RSASHA256Verify(待验证内容, 签名, 公钥) │
└────────────────────────────────────┬─────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────┐
│  第五步：验证结果                                            │
│                                                              │
│  ✅ 签名一致 ──→ Token 有效，继续处理                       │
│  ❌ 签名不一致 ──→ Token 被篡改，返回 401                    │
└─────────────────────────────────────────────────────────────┘
```

**代码示例（RS256 验证）：**
```java
// 1. 拆分 Token
String[] parts = token.split("\\.");
String headerBase64 = parts[0];
String payloadBase64 = parts[1];
String signatureBase64 = parts[2];

// 2. 构建待验证内容
String contentToVerify = headerBase64 + "." + payloadBase64;

// 3. 解码签名
byte[] signatureBytes = Base64.getUrlDecoder().decode(signatureBase64);

// 4. 验证签名
Signature signature = Signature.getInstance("SHA256withRSA");
signature.initVerify(publicKey);
signature.update(contentToVerify.getBytes());
boolean isValid = signature.verify(signatureBytes);

// 5. 验证结果
if (isValid) {
    // Token 有效
    Map<String, Object> payload = objectMapper.readValue(
        Base64.getUrlDecoder().decode(payloadBase64),
        Map.class
    );
} else {
    // Token 被篡改
    throw new UnauthorizedException("Invalid token signature");
}
```

#### 1.5.5 Base64URL vs Base64

| 特性 | Base64 | Base64URL |
|------|--------|-----------|
| `+` | `+` | `-` |
| `/` | `/` | `_` |
| 填充字符 | `=` | 可选 |
| 用途 | 通用编码 | URL 安全编码 |

**为什么用 Base64URL？**
- ✅ Token 需要通过 URL 和 HTTP Header 传输
- ✅ Base64 的 `+` 和 `/` 在 URL 中是特殊字符
- ✅ Base64URL 更安全

---

## 2. JWT vs JWS vs JWE

### 2.1 三者的区别

| 类型 | 全称 | 特点 | 适用场景 |
|------|------|------|----------|
| **JWT** | JSON Web Token | 统称，可以是 JWS 或 JWE | - |
| **JWS** | JSON Web Signature | **签名**，内容明文可读，防篡改 | 大多数场景（推荐） |
| **JWE** | JSON Web Encryption | **加密**，内容不可读，防篡改+防泄露 | 高安全场景 |

### 2.2 JWS（签名）详解

**特点：**
- ✅ 内容明文可读（Base64 编码）
- ✅ 防篡改（私钥签名，公钥验证）
- ✅ 性能好
- ✅ 易调试

**流程：**
```
┌─────────────────────────────────────────────────────────┐
│  授权服务器（Auth）                                      │
│                                                          │
│  1. 创建 Payload: {"sub": "admin", "user_id": 1}        │
│  2. 用私钥签名 ──→ JWS Token                            │
│  3. 发布公钥到 /oauth2/jwks                             │
└──────────────────────────┬──────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│  资源服务器（Gateway/微服务）                            │
│                                                          │
│  1. 从 /oauth2/jwks 获取公钥                            │
│  2. 用公钥验证签名 ──→ 确认 Token 未被篡改              │
│  3. 读取 Payload 内容                                   │
└─────────────────────────────────────────────────────────┘
```

**JWS Token 示例（解码后）：**
```json
// Header
{
  "alg": "RS256",
  "typ": "JWT",
  "kid": "cloud-auth-key"
}

// Payload（明文可读）
{
  "sub": "admin",
  "user_id": 1,
  "username": "admin",
  "authorities": ["ROLE_ADMIN", "ROLE_USER"],
  "exp": 1715587600,
  "iat": 1715585800
}
```

### 2.3 JWE（加密）详解

**特点：**
- ✅ 内容加密不可读
- ✅ 防篡改+防泄露
- ❌ 性能较差
- ❌ 调试困难

**流程：**
```
┌─────────────────────────────────────────────────────────┐
│  授权服务器（Auth）                                      │
│                                                          │
│  1. 创建 Payload: {"sub": "admin", "password": "xxx"}   │
│  2. 用公钥加密 ──→ JWE Token                            │
│  3. 保存私钥（不公开！）                                 │
└──────────────────────────┬──────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│  资源服务器（Gateway/微服务）                            │
│                                                          │
│  1. 用私钥解密 ──→ 获取 Payload 内容                    │
│  2. 验证内容有效性                                      │
└─────────────────────────────────────────────────────────┘
```

**⚠️ 注意：JWE 需要资源服务器持有私钥，私钥不能公开！**

---

## 3. JWK 公钥发布

### 3.1 什么是 JWK？

**JWK（JSON Web Key）** 是一种 JSON 格式的密钥表示法，用于发布公钥。

**JWK Set 示例：**
```json
{
  "keys": [
    {
      "kty": "RSA",
      "alg": "RS256",
      "kid": "cloud-auth-key",
      "n": "0vx7agoebGcQSuuPgUxPwUjK...",
      "e": "AQAB"
    }
  ]
}
```

### 3.2 JWK 字段说明

| 字段 | 说明 |
|------|------|
| `kty` | 密钥类型（RSA、EC、oct） |
| `alg` | 算法（RS256、ES256、HS256） |
| `kid` | 密钥 ID（用于匹配） |
| `n` | RSA 模数（公钥） |
| `e` | RSA 指数（公钥） |
| `x5c` | X.509 证书链 |

### 3.3 项目中的 JWK 端点

**端点：** `GET /oauth2/jwks`

**响应：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "keys": [
      {
        "kty": "RSA",
        "alg": "RS256",
        "kid": "cloud-auth-jwe-key",
        "n": "...",
        "e": "AQAB"
      }
    ]
  }
}
```

---

## 4. 项目中的实现方案

### 4.1 当前架构

```
┌─────────────────────────────────────────────────────────┐
│                    客户端应用                            │
│  (Web/App/其他微服务)                                    │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP + Bearer Token
                     ▼
┌─────────────────────────────────────────────────────────┐
│                 cloud-gateway (网关)                     │
│  ┌─────────────────────────────────────────────────┐    │
│  │ 1. 拦截请求，提取 Token                          │    │
│  │ 2. 从 /oauth2/jwks 获取公钥                     │    │
│  │ 3. 验证 Token 签名                              │    │
│  │ 4. 路由到下游微服务                             │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────┬────────────────────────────────────┘
                     │
         ┌───────────┼───────────┐
         ▼           ▼           ▼
    ┌─────────┐ ┌─────────┐ ┌─────────┐
    │cloud-AI │ │cloud-xxx│ │ 其他服务│
    │验证Token│ │验证Token│ │验证Token│
    └─────────┘ └─────────┘ └─────────┘
```

### 4.2 Token 生成流程

```
用户登录成功
         │
         ▼
┌─────────────────────────────────────────────────────────┐
│  1. 创建 JWT Claims                                     │
│     - sub: 用户名                                       │
│     - user_id: 用户ID                                  │
│     - authorities: 权限列表                            │
│     - exp: 过期时间                                    │
└─────────────────┬───────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────┐
│  2. 用私钥签名 JWT                                      │
│     JwtGenerator + JwtEncoder                          │
└─────────────────┬───────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────┐
│  3. 返回 Access Token + Refresh Token                  │
└─────────────────────────────────────────────────────────┘
```

### 4.3 Token 验证流程

```
请求携带 Token
         │
         ▼
┌─────────────────────────────────────────────────────────┐
│  1. 提取 Authorization 头中的 Token                     │
└─────────────────┬───────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────┐
│  2. 从 /oauth2/jwks 获取公钥（缓存）                    │
└─────────────────┬───────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────┐
│  3. 用公钥验证签名                                      │
│     - 签名有效 ──→ 继续处理                            │
│     - 签名无效 ──→ 返回 401                            │
└─────────────────┬───────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────┐
│  4. 读取 Claims，获取用户信息和权限                     │
└─────────────────┬───────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────┐
│  5. 权限校验 (@PreAuthorize)                            │
│     - 有权限 ──→ 执行业务                              │
│     - 无权限 ──→ 返回 403                              │
└─────────────────────────────────────────────────────────┘
```

---

## 5. 密钥分配原则

### 5.1 JWS 模式（推荐）

| 密钥 | 存放位置 | 用途 | 是否公开 |
|------|----------|------|----------|
| **私钥** | Auth 服务内部 | 签名 JWT | ❌ 保密 |
| **公钥** | `/oauth2/jwks` 端点 | 验证签名 | ✅ 公开 |
| **Token** | 客户端 | 访问资源凭证 | ✅ 公开 |

**流程：**
```
Auth 服务：私钥签名 ──→ Token
Gateway/微服务：公钥验证 ──→ 确认 Token 有效
```

### 5.2 JWE 模式（不推荐当前场景）

| 密钥 | 存放位置 | 用途 | 是否公开 |
|------|----------|------|----------|
| **私钥** | Auth + 所有资源服务器 | 解密 JWT | ❌ 保密（但需要分发） |
| **公钥** | Auth 服务 | 加密 JWT | ✅ 公开 |
| **Token** | 客户端 | 访问资源凭证 | ✅ 公开 |

**问题：**
- ❌ 私钥需要分发给所有资源服务器
- ❌ 私钥泄露风险高
- ❌ 维护复杂

### 5.3 与 SSH 的对比

| 场景 | 私钥位置 | 公钥位置 | 流程 |
|------|----------|----------|------|
| **SSH 登录** | 客户端 | 服务器 | 客户端用私钥签名，服务器用公钥验证 |
| **JWT (JWS)** | Auth 服务 | 资源服务器 | Auth 用私钥签名，资源服务器用公钥验证 |
| **JWT (JWE)** | 资源服务器 | Auth 服务 | Auth 用公钥加密，资源服务器用私钥解密 |

---

## 6. 常见问题

### Q1: 为什么推荐 JWS 而不是 JWE？

**答：**
- JWS 更简单、性能更好、易调试
- JWT 的 Payload 通常不包含敏感信息（如密码）
- 只需要防篡改，不需要防泄露
- Spring Security 原生支持最好

### Q2: Token 中可以放敏感信息吗？

**答：**
- ❌ 不建议放密码、身份证号等敏感信息
- ✅ 可以放 `user_id`、`username`、`authorities` 等非敏感信息
- 如果必须放敏感信息，使用 JWE 加密

### Q3: 公钥发布安全吗？

**答：**
- ✅ 公钥可以公开，不会泄露私钥
- RSA 算法保证：无法从公钥推导私钥
- 类似 HTTPS 证书，公钥公开是安全的

### Q4: 如何防止 Token 被盗用？

**答：**
1. 使用 HTTPS 加密传输
2. 设置较短的过期时间（如 30 分钟）
3. 使用 Refresh Token 机制
4. Token 黑名单（主动失效）

### Q5: 多个微服务如何验证 Token？

**答：**
```
所有微服务：
1. 从 Auth 的 /oauth2/jwks 获取公钥（缓存）
2. 用公钥验证 Token 签名
3. 读取 Claims 获取用户信息
```

### Q6: Token 过期后如何处理？

**答：**
```
客户端：
1. 收到 401 响应
2. 使用 Refresh Token 获取新的 Access Token
3. 重试原请求

POST /oauth2/token
{
  "grant_type": "refresh_token",
  "refresh_token": "xxx"
}
```

---

## 7. 项目配置说明

### 7.1 Auth 服务配置

```yaml
# Token 配置
spring:
  security:
    oauth2:
      authorizationserver:
        issuer: http://localhost:9123

# Token 有效期
access-token:
  time-to-live: 30m    # Access Token 30分钟
refresh-token:
  time-to-live: 7d     # Refresh Token 7天
```

### 7.2 Gateway 配置

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:9123
          jwk-set-uri: http://localhost:9123/oauth2/jwks
```

### 7.3 微服务配置

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: http://localhost:9123/oauth2/jwks
```

---

## 8. 代码示例

### 8.1 生成 Token

```java
// JweAuthorizationServerConfig.java
@Bean
public OAuth2TokenGenerator<?> tokenGenerator() {
    JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder());
    return jwtGenerator;
}
```

### 8.2 发布公钥

```java
// JwksController.java
@GetMapping("/oauth2/jwks")
public ResultData<Map<String, Object>> getJwks() {
    return ResultData.success(jwkService.getJwkSet().toJSONObject());
}
```

### 8.3 验证 Token

```java
// ResourceServerConfig.java
@Bean
public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    http
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
}
```

### 8.4 使用 Token 信息

```java
// 获取当前用户ID
Long userId = SecurityUtils.getUserId();

// 获取当前用户名
String username = SecurityUtils.getUsername();

// 获取当前用户权限
List<String> authorities = SecurityUtils.getAuthorities();

// 权限校验
@PreAuthorize("hasAuthority('sys:user:list')")
public ResultData<List<User>> getUserList() { ... }
```

---

## 9. 总结

### 推荐方案：JWS + JWK

```
✅ Auth 服务：私钥签名 JWT，发布公钥到 /oauth2/jwks
✅ Gateway/微服务：从 /oauth2/jwks 获取公钥，验证签名
✅ 客户端：只持有 Token，不接触密钥
```

### 不推荐方案：JWE

```
❌ 需要分发私钥给所有资源服务器
❌ 私钥泄露风险高
❌ 维护复杂
❌ 当前场景不需要加密内容
```

---

**文档版本**：1.0  
**最后更新**：2024-05-16  
**维护者**：overH
