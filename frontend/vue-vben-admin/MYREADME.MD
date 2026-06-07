# Vben Admin 会话总结

## 一、项目结构

### 1. 整体架构

```
vue-vben-admin/
├── apps/                    ← 应用层（你的地盘，自由改）
│   ├── web-antd/           ← Ant Design Vue 版本
│   ├── web-antd-bakend/    ← 你当前使用的版本
│   ├── web-ele/            ← Element Plus 版本
│   ├── web-naive/          ← Naive UI 版本
│   └── backend-mock/       ← Vben 默认 Mock 后端（Node.js）
│
├── packages/                ← 共享包（谨慎改）
│   ├── effects/
│   │   └── request/         ← 请求库（含拦截器）
│   ├── @core/               ← 框架核心组件
│   ├── types/               ← 类型定义
│   ├── constants/           ← 常量
│   └── ...
│
├── scripts/                 ← 自定义 CLI 工具
│   ├── vsh/                 ← vsh 命令（代码检查等）
│   └── turbo-run/           ← Turbo 包装器
│
├── package.json
├── pnpm-workspace.yaml      ← Pnpm Workspace 配置
└── turbo.json               ← Turborepo 配置
```

---

## 二、核心概念

### 1. Pnpm Monorepo

**是什么？** 多个相关的包放在同一个 Git 仓库里管理。

**配置文件：** `pnpm-workspace.yaml`

```yaml
packages:
  - 'apps/*'
  - 'packages/*'
  - 'scripts/*'
```

**好处：**
- 包之间用 `workspace:*` 引用，无需发布到 npm
- 依赖安装更高效（软链接 + 内容寻址）
- 共享代码方便

---

### 2. Turborepo

**是什么？** Monorepo 的任务编排工具。

**配置文件：** `turbo.json`

**作用：**
- 自动分析依赖关系，按正确顺序执行任务
- 无依赖的任务并行执行
- 构建缓存（相同输入跳过重复构建）
- 远程缓存（CI/CD 共享）

**常用命令：**
```bash
turbo build           # 并行构建所有包
turbo run typecheck   # 并行执行类型检查
turbo dev --filter=web-antd-bakend  # 只构建特定包
```

---

### 3. Changeset

**是什么？** Monorepo 的版本管理和发布工具。

**作用：**
- 记录哪些包需要发布
- 自动计算版本号（semver）
- 生成 CHANGELOG
- 发布到 npm

**工作流程：**
```bash
pnpm changeset        # 添加变更记录
pnpm version          # 更新版本号和 CHANGELOG
pnpm publish -r       # 发布
```

---

### 4. 自定义 CLI 工具（scripts/）

**是什么？** Vben 封装的 Node.js 命令行工具。

**vsh 命令：**
- `vsh lint` - 代码检查、格式化
- `vsh check-circular` - 检查循环依赖
- `vsh check-dep` - 检查未使用依赖
- `vsh publint` - 检查 package.json

**技术栈：**
- `cac` - 轻量级 CLI 框架
- `TypeScript` - 编译后运行
- `consola` - Node.js 日志库

---

## 三、请求与认证流程

### 1. 请求客户端（两个）

**位置：** `apps/web-antd-bakend/src/api/request.ts`

| 客户端 | 用途 | 响应处理 |
|--------|------|----------|
| `requestClient` | 普通 API 调用 | 自动 unwrap `data` 字段 |
| `baseRequestClient` | 登录/刷新 Token | 返回完整响应（不 unwrap） |

**创建方式：**
```typescript
export const requestClient = createRequestClient(apiURL, {
  responseReturn: 'data',  // 自动返回 data 字段
});

export const baseRequestClient = createRequestClient(apiURL, {
  responseReturn: 'raw',   // 返回完整响应
});
```

---

### 2. 拦截器链

```
请求发出
    ↓
请求拦截器（添加 Token、语言等）
    ↓
HTTP 请求
    ↓
响应拦截器
    ├── defaultResponseInterceptor  ← 处理业务 code (200/401/500)
    ├── authenticateResponseInterceptor ← 检测 HTTP 401 → 登录过期
    └── errorMessageResponseInterceptor ← 弹出错误消息
```

---

### 3. 401 状态码处理

**位置：** `packages/effects/request/src/request-client/preset-interceptors.ts`

**流程：**
```
HTTP 401 响应
    ↓
authenticateResponseInterceptor
    ↓
doReAuthenticate()
    ↓
清除 Token + 退出登录 + 跳转登录页
```

**注意：** 登录失败时不要返回 HTTP 401，否则会被误判为"登录过期"。

**解决方案：** 返回 HTTP 200，用业务 code 区分。

---

### 4. 退出登录逻辑

**位置：** `apps/web-antd-bakend/src/store/auth.ts`

```typescript
async function logout(redirect: boolean = true) {
  // 1. 调用后端退出登录接口
  await logoutApi();
  // 2. 清除 Token
  accessStore.setAccessToken(null);
  // 3. 清除用户信息
  userStore.setUserInfo(null);
  // 4. 跳转到登录页
  await router.push('/login');
}
```

---

## 四、项目模板：backend-mock

### 是什么？

Vben Admin 自带的**默认 Mock 后端**，用 Nitropack + H3 实现。

**位置：** `apps/backend-mock/`

### 技术栈

| 技术 | 说明 |
|------|------|
| Nitropack | Nuxt 团队的服务端框架 |
| H3 | HTTP 框架（类似 Express/Koa） |
| jsonwebtoken | JWT 认证 |
| @faker-js/faker | 生成模拟数据 |

### 目录结构

```
apps/backend-mock/
├── api/              ← 路由（文件即路由）
│   ├── auth/
│   │   ├── login.post.ts     → POST /auth/login
│   │   ├── logout.post.ts    → POST /auth/logout
│   │   └── refresh.post.ts   → POST /auth/refresh
│   ├── user/
│   │   └── info.ts           → GET /user/info
│   ├── menu/
│   │   └── all.ts            → GET /menu/all
│   └── system/
│       ├── user/list.ts      → GET /system/user/list
│       ├── role/list.ts      → GET /system/role/list
│       └── menu/list.ts      → GET /system/menu/list
├── middleware/
│   └── 1.api.ts      ← 中间件（认证等）
├── utils/
│   ├── jwt-utils.ts  ← JWT 工具
│   ├── response.ts   ← 响应封装
│   └── mock-data.ts  ← 模拟数据
└── nitro.config.ts   ← Nitro 配置
```

### 响应格式

```typescript
// 成功
{ code: 0, data: {...} }

// 错误
{ code: -1, message: "..." }
```

### 和你的 Java 后端对比

| 对比项 | backend-mock | 你的 Java 后端 |
|--------|--------------|----------------|
| 技术栈 | Nitropack + H3 | Spring Boot |
| 数据库 | 内存/Mock | PostgreSQL |
| 成功 code | `0` | `"200"` |
| 错误 code | `-1` | `"1002"` 等 |

**用途：** 作为 Java 后端开发的参考模板。

---

## 五、修改原则

### 1. 能改的地方（apps/）

**位置：** `apps/你的应用/src/`

| 文件 | 可以改什么 |
|------|-----------|
| `api/request.ts` | 请求基类、拦截器、错误处理 |
| `api/core/*.ts` | API 接口定义、字段映射 |
| `store/*.ts` | 状态管理、认证逻辑 |
| `.env.*` | 环境变量、API 地址 |
| `vite.config.ts` | Vite 配置、代理 |

### 2. 谨慎改的地方（packages/）

**位置：** `packages/`

| 包名 | 改前考虑 |
|------|---------|
| `effects/request` | 所有应用共用，改了影响全局 |
| `@core` | 框架核心，可能导致升级困难 |
| `types` | 改了需要同步所有使用的地方 |

### 3. 改 packages 的正确姿势

```
❌ 不要：直接硬编码改死
✅ 要：增加配置项，让应用层可以控制
```

**例子：**
```typescript
// ❌ 不推荐
if (response?.status !== 401) {
  throw error;
}

// ✅ 推荐
export interface AuthenticateConfig {
  authStatusCodes?: number[];
  excludePaths?: string[];
}
```

---

## 六、本次修改记录

### 1. 前端修改

| 文件 | 修改内容 |
|------|----------|
| `apps/web-antd-bakend/src/api/request.ts` | `successCode` 从 `0` 改为 `"200"` |
| `apps/web-antd-bakend/src/api/core/auth.ts` | 登录接口改为 FormData，路径加 `/cloud-auth` 前缀 |
| `apps/web-antd-bakend/src/api/core/user.ts` | 路径改为 `/cloud-auth/user/auth/info`，字段映射 |

### 2. 后端修改

| 文件 | 修改内容 |
|------|----------|
| `cloud-common/.../StatusConst.java` | 常量名改为 `RC_xxx` 格式 |
| `cloud-common/.../ReturnCodeEnum.java` | 枚举名改为英文语义化 |
| `cloud-auth/.../LoginFailureHandler.java` | 使用 `BusinessException`，HTTP 状态码改 200 |
| `cloud-auth/.../UserServiceImpl.java` | 将禁用/封禁改为 `DisabledException`/`LockedException` |

---

## 七、常见问题

### Q1: 登录失败弹出"登录过期"怎么回事？

**原因：** 后端返回 HTTP 401，被 `authenticateResponseInterceptor` 检测到。

**解决：** 返回 HTTP 200，用业务 code 区分。

### Q2: 请求 404，网关找不到服务？

**原因：** 网关动态路由需要服务名前缀。

**解决：** 请求路径加服务名前缀，如 `/cloud-auth/auth/login`。

### Q3: TypeScript 类型错误？

**原因：** 后端返回字段和前端期望不一致。

**解决：** 在 API 层做字段映射，参考 `getUserInfoApi()`。

### Q4: 怎么同时启动前端和后端？

```bash
# 前端（5666 端口）
cd frontend/vue-vben-admin
pnpm dev:antd-bakend

# 后端（9000 端口）
cd cloud-auth
mvn spring-boot:run
```

---

## 八、参考资源

| 资源 | 位置 |
|------|------|
| 官方文档 | https://www.vben.pro/ |
| 请求拦截器 | `packages/effects/request/src/request-client/` |
| Mock 后端模板 | `apps/backend-mock/` |
| 应用配置 | `apps/web-antd-bakend/src/api/request.ts` |
