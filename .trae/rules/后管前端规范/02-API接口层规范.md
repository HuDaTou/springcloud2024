---
alwaysApply: false
globs: frontend/vue-vben-admin/**/api/**/*.ts
---
# 后管前端API接口层规范

> **适用范围**：`frontend/vue-vben-admin/` 目录下所有 `api/**/*.ts` 文件

## 一、强制要求（AI必须100%遵守，违反即错误）

1. **必须使用requestClient**：所有API请求必须使用requestClient或baseRequestClient
2. **必须使用namespace组织类型**：每个API文件必须使用namespace定义接口类型
3. **必须添加JSDoc注释**：每个函数必须添加JSDoc注释
4. **必须定义TypeScript类型**：所有接口参数和返回值必须定义明确的类型
5. **必须使用语义化函数名**：函数名必须清晰表达其功能
6. **必须导出类型和函数**：必须同时导出namespace类型和相关函数

## 二、推荐做法（AI优先采用，效果更好）

1. **按业务模块拆分API文件**：将相关功能的API放在同一个文件中
2. **使用Omit排除只读字段**：创建和更新接口使用Omit排除id等只读字段
3. **统一接口命名规范**：列表接口使用list，详情使用detail，创建使用create

## 三、禁止行为（AI绝对不能生成）

1. **禁止硬编码URL**：接口URL禁止硬编码
2. **禁止使用any类型**：禁止在类型定义中使用any
3. **禁止直接返回response**：禁止直接返回axios response对象

## 四、目录结构

```
src/api/
├── request.ts              # 请求客户端配置
├── index.ts                # API统一导出
├── core/                   # 核心接口（auth、user、menu）
└── system/                 # 系统管理接口（user、role、dept）
```

## 五、标准代码示例

### 请求客户端配置
```typescript
// src/api/request.ts
import { RequestClient } from '@vben/request';

export const requestClient = new RequestClient({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  responseReturn: 'data',
});

export const baseRequestClient = new RequestClient({ 
  baseURL: import.meta.env.VITE_APP_BASE_API 
});
```

### 系统用户API
```typescript
// src/api/system/user.ts
import { requestClient } from '#/api/request';

export namespace SystemUserApi {
  export interface SystemUser {
    id: string;
    username: string;
    name: string;
    status: 0 | 1;
  }
  
  export interface UserQueryParams {
    username?: string;
    status?: 0 | 1;
  }
}

export async function getUserList(params: SystemUserApi.UserQueryParams) {
  return requestClient.get<SystemUserApi.SystemUser[]>('/system/user/list', { params });
}

export async function getUserDetail(id: string) {
  return requestClient.get<SystemUserApi.SystemUser>(`/system/user/${id}`);
}

export async function createUser(data: Omit<SystemUserApi.SystemUser, 'id'>) {
  return requestClient.post('/system/user', data);
}

export async function updateUser(id: string, data: Omit<SystemUserApi.SystemUser, 'id'>) {
  return requestClient.put(`/system/user/${id}`, data);
}

export async function deleteUser(id: string) {
  return requestClient.delete(`/system/user/${id}`);
}
```

### 认证API
```typescript
// src/api/core/auth.ts
import { requestClient } from '#/api/request';

export namespace AuthApi {
  export interface LoginParams {
    username: string;
    password: string;
  }
  
  export interface LoginResult {
    accessToken: string;
  }
}

export async function loginApi(data: AuthApi.LoginParams) {
  return requestClient.post<AuthApi.LoginResult>('/auth/login', data);
}

export async function getUserInfoApi() {
  return requestClient.get('/auth/user-info');
}
```

### API统一导出
```typescript
// src/api/index.ts
export * as SystemUserApi from './system/user';
export * as AuthApi from './core/auth';
export { requestClient, baseRequestClient } from './request';
```