---
name: "后管前端Router路由层规范"
description: "适用于vue-vben-admin后管前端项目的Router路由层编写规范"
alwaysApply: false
globs:
  - "frontend/vue-vben-admin/**/router/**/*.ts"
priority: 4
---

# 后管前端Router路由层规范

## 一、强制要求（AI必须100%遵守，违反即错误）

1. **必须使用Vue Router 4**：路由管理必须使用Vue Router 4
2. **必须使用TypeScript**：所有路由配置文件必须使用TypeScript编写
3. **必须使用懒加载**：所有非核心路由必须使用动态import进行懒加载
4. **必须定义路由meta信息**：每个路由必须定义meta信息

## 二、推荐做法（AI优先采用，效果更好）

1. **按业务模块拆分路由**：将相关功能的路由放在同一个模块文件中
2. **使用嵌套路由**：使用children配置嵌套路由
3. **使用路由元信息**：利用meta信息控制页面权限、缓存、图标等

## 三、禁止行为（AI绝对不能生成）

1. **禁止硬编码路由路径**：路由路径禁止硬编码
2. **禁止重复定义路由名称**：路由名称必须全局唯一

## 四、目录结构

```
src/router/
├── index.ts                 # 路由实例创建和导出
├── guard.ts                 # 路由守卫配置
└── routes/                  # 路由配置
    ├── index.ts            # 路由统一导出
    ├── core.ts             # 核心路由（登录、404等）
    └── modules/            # 模块路由
        └── system.ts       # 系统管理路由
```

## 五、标准代码示例

### 路由实例创建（index.ts）
```typescript
import { createRouter, createWebHistory } from 'vue-router';
import { routes } from './routes';

const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_BASE),
  routes,
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

export { router };
```

### 系统管理路由（modules/system.ts）
```typescript
import type { RouteRecordRaw } from 'vue-router';
import { $t } from '#/locales';

const routes: RouteRecordRaw[] = [
  {
    path: '/system',
    name: 'System',
    meta: { title: $t('system.title'), icon: 'ion:settings-outline' },
    children: [
      {
        path: '/system/user',
        name: 'SystemUser',
        meta: { title: $t('system.user.title'), icon: 'mdi:user' },
        component: () => import('#/views/system/user/list.vue'),
      },
      {
        path: '/system/role',
        name: 'SystemRole',
        meta: { title: $t('system.role.title'), icon: 'mdi:account-group' },
        component: () => import('#/views/system/role/list.vue'),
      },
    ],
  },
];

export default routes;
```

### 核心路由（core.ts）
```typescript
import type { RouteRecordRaw } from 'vue-router';
import { $t } from '#/locales';

export const coreRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard/analytics',
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('#/views/_core/authentication/login.vue'),
    meta: { title: $t('authentication.login'), hideInMenu: true },
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('#/views/_core/fallback/not-found.vue'),
    meta: { title: $t('exception.notFound'), hideInMenu: true },
  },
];

export const coreRouteNames = coreRoutes.map(r => r.name as string);
```

### 路由统一导出（routes/index.ts）
```typescript
import { coreRoutes } from './core';
import systemRoutes from './modules/system';

export const staticRoutes = [...coreRoutes];
export const accessRoutes = [...systemRoutes];
export const routes = [...staticRoutes, ...accessRoutes];
```