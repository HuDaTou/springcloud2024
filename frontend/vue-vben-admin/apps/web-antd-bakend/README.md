# Web-Antd-Backend 应用架构说明

> 本文档介绍 `web-antd-bakend` 应用的架构设计，不涉及具体业务实现。

## 一、技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.x | 核心框架，使用 Composition API |
| TypeScript | 5.x | 类型安全 |
| Vite | 5.x | 构建工具，快速开发体验 |
| Ant Design Vue | 4.x | UI 组件库 |
| VXE Table | 4.x | 高性能表格组件 |
| Pinia | 2.x | 状态管理 |
| Vue Router | 4.x | 路由管理 |
| Vue I18n | 9.x | 国际化支持 |
| UnoCSS | - | 原子化 CSS |

## 二、目录结构

```
src/
├── adapter/                 # 适配器层 - 封装第三方组件
│   ├── component/           # 组件适配器
│   │   └── index.ts         # 组件注册和类型定义
│   ├── form.ts              # 表单适配器 (useVbenForm)
│   └── vxe-table.ts         # 表格适配器 (useVbenVxeGrid)
│
├── api/                     # API 接口层 - 与后端通信
│   ├── request.ts           # 请求客户端配置（拦截器、Token处理）
│   ├── index.ts             # API 统一导出
│   └── core/                # 核心业务接口
│       ├── auth.ts          # 认证接口（登录、登出、刷新Token）
│       ├── user.ts          # 用户信息接口
│       ├── menu.ts          # 菜单接口
│       ├── role.ts          # 角色接口
│       ├── permission.ts    # 权限接口
│       ├── media-asset.ts   # 媒体资产管理接口
│       ├── media-upload.ts  # 媒体文件上传客户端
│       └── index.ts         # 核心接口导出
│
├── layouts/                 # 布局组件
│   ├── index.ts             # 布局统一导出
│   ├── basic.vue            # 基础布局（侧边栏 + 头部 + 内容区）
│   └── auth.vue             # 认证布局（登录页等）
│
├── locales/                 # 国际化
│   ├── index.ts             # i18n 配置
│   ├── README.md            # 国际化说明
│   └ langs/
│       ├── zh-CN/           # 中文语言包
│       │   ├── page.json    # 页面文本
│       │   └ demos.json     # 示例文本
│       └── en-US/           # 英文语言包
│
├── router/                  # 路由配置
│   ├── index.ts             # 路由实例创建
│   ├── guard.ts             # 路由守卫（权限验证）
│   ├── access.ts            # 权限访问控制
│   └ routes/
│       ├── index.ts         # 路由聚合
│       ├── core.ts          # 核心路由（登录、404等）
│       └ modules/           # 模块路由
│           ├── dashboard.ts # 仪表盘路由
│           ├── system.ts    # 系统管理路由
│           ├── demos.ts     # 示例路由
│           └ vben.ts        # Vben 相关路由
│
├── store/                   # 状态管理 (Pinia)
│   ├── index.ts             # Store 导出
│   └ auth.ts                # 认证状态（用户信息、Token）
│
├── views/                   # 页面组件
│   ├── _core/               # 核心页面（无需权限）
│   │   ├── authentication/  # 认证页面
│   │   │   ├── login.vue    # 登录页
│   │   │   ├── register.vue # 注册页
│   │   │   └ forget-password.vue # 忘记密码
│   │   ├── profile/         # 个人中心
│   │   ├── fallback/        # 错误页面（404、403、500）
│   │   └ about/             # 关于页面
│   │   └ README.md          # 核心页面说明
│   ├── dashboard/           # 仪表盘
│   │   ├── analytics/       # 分析仪表盘
│   │   └ workspace/         # 工作台
│   ├── system/              # 系统管理模块
│   │   ├── user/            # 用户管理
│   │   ├── role/            # 角色管理
│   │   ├── menu/            # 菜单管理
│   │   └ permission/        # 权限管理
│   ├── media/               # 媒体管理模块
│   │   ├── assets/          # 媒体资产管理
│   │   ├── rules/           # 上传规则管理
│   └ demos/                 # 示例页面
│
├── bootstrap.ts             # 应用启动配置
├── main.ts                  # 应用入口
├── app.vue                  # 根组件
└── preferences.ts           # 应用偏好设置
```

## 三、架构分层

### 3.1 适配器层 (Adapter)

适配器层用于封装第三方组件，提供统一的接口，降低组件替换成本。

```typescript
// adapter/form.ts - 表单适配器
import { useVbenForm } from '@vben/common-ui';

export function initSetupVbenForm() {
  // 配置表单默认行为
}

// adapter/vxe-table.ts - 表格适配器
import { useVbenVxeGrid } from '@vben/common-ui';

export function initSetupVxeTable() {
  // 配置表格默认行为
}
```

### 3.2 API 层

API 层负责与后端通信，统一管理请求。

```typescript
// api/request.ts - 请求客户端
export const requestClient = createRequestClient(apiURL, {
  responseReturn: 'data',
});

// 自动处理：
// - Bearer Token 认证
// - Token 过期刷新
// - 统一错误提示
// - 响应数据格式化
```

### 3.3 路由层 (Router)

路由层管理页面导航和权限控制。

```typescript
// router/guard.ts - 路由守卫
// 1. 检查 Token 是否有效
// 2. 获取用户信息和权限
// 3. 动态生成菜单
// 4. 验证页面访问权限
```

### 3.4 状态层 (Store)

使用 Pinia 管理全局状态。

```typescript
// store/auth.ts - 认证状态
export const useAuthStore = defineStore('auth', {
  state: () => ({
    userInfo: null,
    accessToken: null,
  }),
  actions: {
    login(),
    logout(),
    refreshToken(),
  },
});
```

### 3.5 视图层 (Views)

页面组件按模块组织，每个模块包含：

- `list.vue` - 列表页面
- `data.ts` - 表格列定义、表单配置
- `modules/form.vue` - 表单弹窗组件

## 四、核心流程

### 4.1 应用启动流程

```
main.ts
  ↓
initPreferences() - 初始化偏好设置
  ↓
bootstrap() - 启动应用
  ↓
  ├── initComponentAdapter() - 注册组件适配器
  ├── initSetupVbenForm() - 配置表单
  ├── initSetupVxeTable() - 配置表格
  ├── setupI18n() - 配置国际化
  ├── initStores() - 初始化状态管理
  └ registerAccessDirective() - 注册权限指令
  └ app.use(router) - 注册路由
  ↓
app.mount('#app') - 挂载应用
```

### 4.2 登录流程

```
用户输入账号密码
  ↓
调用 authApi.login()
  ↓
保存 accessToken 到 Pinia Store
  ↓
路由守卫检测到 Token
  ↓
调用 userApi.getUserInfo() 获取用户信息
  ↓
调用 menuApi.getMenuList() 获取菜单
  ↓
动态注册路由
  ↓
跳转到首页
```

### 4.3 API 请求流程

```
组件调用 API 函数
  ↓
requestClient 发起请求
  ↓
请求拦截器添加 Authorization: Bearer {token}
  ↓
发送请求到后端
  ↓
响应拦截器处理：
  ├── 成功 → 返回 data 字段
  ├── Token 过期 → 自动刷新
  ├── 错误 → 显示错误提示
```

## 五、设计原则

### 5.1 组件设计

- **Composition API**：使用 `<script setup>` 语法
- **类型安全**：所有 Props、事件都有 TypeScript 类型定义
- **单一职责**：每个组件只做一件事

### 5.2 API 设计

- **统一请求客户端**：所有请求通过 `requestClient`
- **namespace 组织类型**：相关类型放在同一个 namespace
- **语义化函数名**：`getXxxList`、`createXxx`、`updateXxx`、`deleteXxx`

### 5.3 路由设计

- **模块化路由**：每个业务模块独立路由文件
- **动态路由**：根据用户权限动态注册
- **路由守卫**：统一权限验证

### 5.4 状态设计

- **Pinia Store**：按功能模块划分 Store
- **持久化**：Token 等关键数据持久化到 localStorage
- **响应式**：状态变更自动触发视图更新

## 六、扩展指南

### 6.1 新增页面模块

1. 在 `views/` 下创建模块目录
2. 创建 `list.vue`、`data.ts`、`modules/form.vue`
3. 在 `router/routes/modules/` 创建路由配置
4. 在 `api/core/` 创建 API 接口文件

### 6.2 新增 API 接口

1. 在 `api/core/` 创建接口文件
2. 使用 namespace 定义类型
3. 使用 `requestClient` 发起请求
4. 在 `api/core/index.ts` 导出

### 6.3 新增状态管理

1. 在 `store/` 创建 Store 文件
2. 使用 `defineStore` 定义 Store
3. 在 `store/index.ts` 导出

## 七、与 Monorepo 的关系

本应用是 Vue Vben Admin Monorepo 的一个子应用：

```
vue-vben-admin/
├── packages/                # 共享包
│   ├── @vben/types          # 类型定义
│   ├── @vben/request        # 请求封装
│   ├── @vben/common-ui      # 通用 UI 组件
│   ├── @vben/stores         # 共享 Store
│   ├── @vben/hooks          # 共享 Hooks
│   └── @vben/preferences    # 偏好设置
│
├── apps/                    # 应用
│   ├── web-antd-bakend      # 本应用（后端管理）
│   ├── web-antd             # Ant Design 模板
│   ├── web-naive            # Naive UI 模板
│   ├── web-tdesign          # TDesign 模板
│   └ backend-mock           # Mock 后端
│   └ playground             # 示例应用
```

本应用依赖 packages 中的共享包，实现代码复用和统一规范。