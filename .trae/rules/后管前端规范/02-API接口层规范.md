---
alwaysApply: false
globs: frontend/vue-vben-admin/**/api/**/*.ts
---
# 后管前端API接口层规范

> **适用范围**：`frontend/vue-vben-admin/` 目录下所有 `api/**/*.ts` 文件

## 一、强制要求（AI必须100%遵守，违反即错误）

1. **必须使用requestClient**：所有API请求必须使用 `requestClient` 或 `baseRequestClient`
2. **必须使用namespace组织类型**：每个API文件必须使用namespace定义接口类型
3. **必须添加JSDoc注释**：每个函数必须添加JSDoc注释
4. **必须定义TypeScript类型**：所有接口参数和返回值必须定义明确的类型
5. **必须使用语义化函数名**：函数名必须清晰表达其功能
6. **函数使用 `async function` 声明后统一 `export`**：先在文件内定义函数，在文件末尾用 `export { ... }` 统一导出

## 二、推荐做法（AI优先采用，效果更好）

1. **按业务模块拆分API文件**：将相关功能的API放在同一个文件中的同一个 namespace 下
2. **使用Omit排除只读字段**：创建和更新接口使用Omit排除id等只读字段
3. **统一接口命名规范**：列表接口使用 `getXxxList`，详情使用 `getXxxDetail`，创建使用 `createXxx`，更新使用 `updateXxx`，删除使用 `deleteXxx`

## 三、禁止行为（AI绝对不能生成）

1. **禁止硬编码URL**：接口URL禁止硬编码
2. **禁止使用any类型**：禁止在类型定义中使用any（特殊情况使用 `Recordable<any>`）
3. **禁止直接返回response**：禁止直接返回axios response对象

## 四、目录结构

```
src/api/
├── request.ts              # 请求客户端配置（含拦截器）
├── index.ts                # API统一导出（export * from...）
├── core/                   # 核心接口（auth、user、menu）
│   ├── index.ts            # export * from './auth' 等
│   ├── auth.ts             # 认证API（login、logout、refresh、codes）
│   ├── user.ts             # 用户信息API
│   ├── menu.ts             # 菜单API
│   └── timezone.ts         # 时区API
└── system/                 # 系统管理接口
    ├── index.ts            # export * from...
    ├── user.ts             # 用户管理API（CRUD）
    ├── role.ts
    ├── dept.ts
    └── menu.ts             # 系统菜单API
```

## 五、标准代码示例

### 请求客户端配置 (request.ts)
```typescript
import type { AxiosResponseHeaders, RequestClientOptions } from '@vben/request';

import { useAppConfig } from '@vben/hooks';
import { preferences } from '@vben/preferences';
import {
  authenticateResponseInterceptor,
  defaultResponseInterceptor,
  errorMessageResponseInterceptor,
  RequestClient,
} from '@vben/request';
import { useAccessStore } from '@vben/stores';

import { message } from 'antdv-next';

import { useAuthStore } from '#/store';
import { refreshTokenApi } from './core';

const { apiURL } = useAppConfig(import.meta.env, import.meta.env.PROD);

function createRequestClient(baseURL: string, options?: RequestClientOptions) {
  const client = new RequestClient({
    ...options,
    baseURL,
  });

  // 请求头处理，自动附加 Bearer Token
  client.addRequestInterceptor({
    fulfilled: async (config) => {
      const accessStore = useAccessStore();
      config.headers.Authorization = accessStore.accessToken
        ? `Bearer ${accessStore.accessToken}`
        : null;
      config.headers['Accept-Language'] = preferences.app.locale;
      return config;
    },
  });

  // 响应数据格式处理 (codeField: 'code', dataField: 'data', successCode: 0)
  client.addResponseInterceptor(
    defaultResponseInterceptor({
      codeField: 'code',
      dataField: 'data',
      successCode: 0,
    }),
  );

  // token过期自动刷新
  client.addResponseInterceptor(
    authenticateResponseInterceptor({
      client,
      doReAuthenticate: async () => { /* ... */ },
      doRefreshToken: async () => { /* ... */ },
      enableRefreshToken: preferences.app.enableRefreshToken,
      formatToken: (token) => (token ? `Bearer ${token}` : null),
    }),
  );

  // 通用错误处理
  client.addResponseInterceptor(
    errorMessageResponseInterceptor((msg: string, error) => {
      const responseData = error?.response?.data ?? {};
      const errorMessage = responseData?.error ?? responseData?.message ?? '';
      message.error(errorMessage || msg);
    }),
  );

  return client;
}

export const requestClient = createRequestClient(apiURL, {
  responseReturn: 'data',
});

export const baseRequestClient = new RequestClient({ baseURL: apiURL });
```

### 认证API (core/auth.ts)
```typescript
import { baseRequestClient, requestClient } from '#/api/request';

export namespace AuthApi {
  /** 登录接口参数 */
  export interface LoginParams {
    password?: string;
    username?: string;
  }

  /** 登录接口返回值 */
  export interface LoginResult {
    accessToken: string;
  }

  export interface RefreshTokenResult {
    data: string;
    status: number;
  }
}

/**
 * 登录
 */
async function loginApi(data: AuthApi.LoginParams) {
  return requestClient.post<AuthApi.LoginResult>('/auth/login', data, {
    withCredentials: true,
  });
}

/**
 * 刷新accessToken
 */
async function refreshTokenApi() {
  return baseRequestClient.post<AuthApi.RefreshTokenResult>('/auth/refresh', null, {
    withCredentials: true,
  });
}

/**
 * 退出登录
 */
async function logoutApi() {
  return baseRequestClient.post('/auth/logout', null, {
    withCredentials: true,
  });
}

/**
 * 获取用户权限码
 */
async function getAccessCodesApi() {
  return requestClient.get<string[]>('/auth/codes');
}

export { getAccessCodesApi, loginApi, logoutApi, refreshTokenApi };
```

### 系统用户API (system/user.ts)
```typescript
import type { Recordable } from '@vben/types';

import { requestClient } from '#/api/request';

export namespace SystemUserApi {
  export interface SystemUser {
    [key: string]: any;
    id: string;
    name: string;
    permissions: string[];
    remark?: string;
    status: 0 | 1;
  }
}

/**
 * 获取用户列表数据
 */
async function getUserList(params: Recordable<any>) {
  return requestClient.get<Array<SystemUserApi.SystemUser>>('/system/user/list', { params });
}

/**
 * 创建用户
 */
async function createUser(data: Omit<SystemUserApi.SystemUser, 'id'>) {
  return requestClient.post('/system/user', data);
}

/**
 * 更新用户
 */
async function updateUser(id: string, data: Omit<SystemUserApi.SystemUser, 'id'>) {
  return requestClient.put(`/system/user/${id}`, data);
}

/**
 * 删除用户
 */
async function deleteUser(id: string) {
  return requestClient.delete(`/system/user/${id}`);
}

export { createUser, deleteUser, getUserList, updateUser };
```

### 用户信息API (core/user.ts)
```typescript
import type { UserInfo } from '@vben/types';

import { requestClient } from '#/api/request';

/**
 * 获取用户信息
 */
async function getUserInfoApi() {
  return requestClient.get<UserInfo>('/user/info');
}

export { getUserInfoApi };
```

### API统一导出 (index.ts 和 core/index.ts)
```typescript
// src/api/index.ts
export * from './core';
export * from './system';

// 也可单独导出 requestClient
export { requestClient, baseRequestClient } from './request';
```

```typescript
// src/api/core/index.ts
export * from './auth';
export * from './menu';
export * from './timezone';
export * from './user';
```

```typescript
// src/api/system/index.ts
export * from './dept';
export * from './menu';
export * from './role';
export * from './user';
```
