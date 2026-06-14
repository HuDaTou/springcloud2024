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
4. **必须定义路由meta信息**：每个路由必须定义 `meta.icon` 和 `meta.title`
5. **模块路由使用 `import.meta.glob` 自动导入**：路由模块文件放到 `routes/modules/` 下自动发现
6. **Layout使用异步组件工厂**：`const BasicLayout = () => import('#/layouts/basic.vue')`

## 二、推荐做法（AI优先采用，效果更好）

1. **按业务模块拆分路由**：将相关功能的路由放在同一个模块文件中
2. **使用嵌套路由**：使用children配置嵌套路由
3. **使用路由元信息**：利用meta信息控制页面权限、缓存、图标、排序等

## 三、禁止行为（AI绝对不能生成）

1. **禁止硬编码路由路径**：避免在业务代码中硬编码路由路径
2. **禁止重复定义路由名称**：路由名称必须全局唯一

## 四、目录结构

```
src/router/
├── index.ts                 # 路由实例创建和导出
├── guard.ts                 # 路由守卫配置（通用守卫 + 权限守卫）
├── access.ts                # 权限访问控制（generateAccess）
└── routes/                  # 路由配置
    ├── index.ts             # 路由聚合（自动发现modules、coreRoutes、accessRoutes）
    ├── core.ts              # 核心路由（登录、fallback等Root/Authentication布局路由）
    └── modules/             # 模块路由（自动 import.meta.glob 发现）
        ├── system.ts        # 系统管理路由
        ├── dashboard.ts     # 仪表盘路由
        ├── demos.ts         # 演示路由
        └── examples.ts      # 示例路由
```

## 五、标准代码示例

### 路由实例创建 (index.ts)
```typescript
import { createRouter, createWebHashHistory, createWebHistory } from 'vue-router';

import { resetStaticRoutes } from '@vben/utils';

import { createRouterGuard } from './guard';
import { routes } from './routes';

const router = createRouter({
  history:
    import.meta.env.VITE_ROUTER_HISTORY === 'hash'
      ? createWebHashHistory(import.meta.env.VITE_BASE)
      : createWebHistory(import.meta.env.VITE_BASE),
  routes,
  scrollBehavior: (to, _from, savedPosition) => {
    if (savedPosition) {
      return savedPosition;
    }
    return to.hash ? { behavior: 'smooth', el: to.hash } : { left: 0, top: 0 };
  },
});

const resetRoutes = () => resetStaticRoutes(router, routes);

// 创建路由守卫
createRouterGuard(router);

export { resetRoutes, router };
```

### 路由聚合 (routes/index.ts)
```typescript
import type { RouteRecordRaw } from 'vue-router';

import { mergeRouteModules, traverseTreeValues } from '@vben/utils';

import { coreRoutes, fallbackNotFoundRoute } from './core';

// 自动发现模块路由
const dynamicRouteFiles = import.meta.glob('./modules/**/*.ts', {
  eager: true,
});

/** 动态路由 */
const dynamicRoutes: RouteRecordRaw[] = mergeRouteModules(dynamicRouteFiles);

/** 路由列表，由基本路由和404兜底路由组成（无需走权限验证） */
const routes: RouteRecordRaw[] = [
  ...coreRoutes,
  fallbackNotFoundRoute,
];

/** 基本路由列表，这些路由不需要进入权限拦截 */
const coreRouteNames = traverseTreeValues(coreRoutes, (route) => route.name);

/** 有权限校验的路由列表 */
const accessRoutes = [...dynamicRoutes];

export { accessRoutes, coreRouteNames, routes };
```

### 核心路由 (routes/core.ts)
```typescript
import type { RouteRecordRaw } from 'vue-router';

import { LOGIN_PATH } from '@vben/constants';
import { preferences } from '@vben/preferences';

import { $t } from '#/locales';

const BasicLayout = () => import('#/layouts/basic.vue');
const AuthPageLayout = () => import('#/layouts/auth.vue');

/** 全局404页面 */
const fallbackNotFoundRoute: RouteRecordRaw = {
  component: () => import('#/views/_core/fallback/not-found.vue'),
  meta: {
    hideInBreadcrumb: true,
    hideInMenu: true,
    hideInTab: true,
    title: '404',
  },
  name: 'FallbackNotFound',
  path: '/:path(.*)*',
};

/** 基本路由，这些路由是必须存在的 */
const coreRoutes: RouteRecordRaw[] = [
  {
    component: BasicLayout,
    meta: { hideInBreadcrumb: true, title: 'Root' },
    name: 'Root',
    path: '/',
    redirect: preferences.app.defaultHomePath,
    children: [],
  },
  {
    component: AuthPageLayout,
    meta: { hideInTab: true, title: 'Authentication' },
    name: 'Authentication',
    path: '/auth',
    redirect: LOGIN_PATH,
    children: [
      {
        name: 'Login',
        path: 'login',
        component: () => import('#/views/_core/authentication/login.vue'),
        meta: { title: $t('page.auth.login') },
      },
      {
        name: 'Register',
        path: 'register',
        component: () => import('#/views/_core/authentication/register.vue'),
        meta: { title: $t('page.auth.register') },
      },
    ],
  },
];

export { coreRoutes, fallbackNotFoundRoute };
```

### 系统管理路由 (routes/modules/system.ts)
```typescript
import type { RouteRecordRaw } from 'vue-router';

import { $t } from '#/locales';

const routes: RouteRecordRaw[] = [
  {
    meta: {
      icon: 'ion:settings-outline',
      order: 9997,
      title: $t('system.title'),
    },
    name: 'System',
    path: '/system',
    children: [
      {
        path: '/system/user',
        name: 'SystemUser',
        meta: {
          icon: 'mdi:user',
          title: $t('system.user.title'),
        },
        component: () => import('#/views/system/user/list.vue'),
      },
      {
        path: '/system/role',
        name: 'SystemRole',
        meta: {
          icon: 'mdi:account-group',
          title: $t('system.role.title'),
        },
        component: () => import('#/views/system/role/list.vue'),
      },
      {
        path: '/system/menu',
        name: 'SystemMenu',
        meta: {
          icon: 'mdi:menu',
          title: $t('system.menu.title'),
        },
        component: () => import('#/views/system/menu/list.vue'),
      },
      {
        path: '/system/dept',
        name: 'SystemDept',
        meta: {
          icon: 'charm:organisation',
          title: $t('system.dept.title'),
        },
        component: () => import('#/views/system/dept/list.vue'),
      },
    ],
  },
];

export default routes;
```

### 路由守卫 (guard.ts)
```typescript
import type { Router } from 'vue-router';

import { LOGIN_PATH } from '@vben/constants';
import { preferences } from '@vben/preferences';
import { useAccessStore, useUserStore } from '@vben/stores';
import { startProgress, stopProgress } from '@vben/utils';

import { accessRoutes, coreRouteNames } from '#/router/routes';
import { useAuthStore } from '#/store';

import { generateAccess } from './access';

/** 通用守卫：进度条 + 页面加载记录 */
function setupCommonGuard(router: Router) {
  const loadedPaths = new Set<string>();

  router.beforeEach((to) => {
    to.meta.loaded = loadedPaths.has(to.path);
    if (!to.meta.loaded && preferences.transition.progress) {
      startProgress();
    }
    return true;
  });

  router.afterEach((to) => {
    loadedPaths.add(to.path);
    if (preferences.transition.progress) {
      stopProgress();
    }
  });
}

/** 权限访问守卫：token检查 + 动态路由生成 */
function setupAccessGuard(router: Router) {
  router.beforeEach(async (to, from) => {
    const accessStore = useAccessStore();
    const userStore = useUserStore();
    const authStore = useAuthStore();

    // 基本路由直接放行
    if (coreRouteNames.includes(to.name as string)) {
      if (to.path === LOGIN_PATH && accessStore.accessToken) {
        return decodeURIComponent(
          (to.query?.redirect as string) ||
            userStore.userInfo?.homePath ||
            preferences.app.defaultHomePath,
        );
      }
      return true;
    }

    // 无token → 跳转登录
    if (!accessStore.accessToken) {
      if (to.meta.ignoreAccess) return true;
      return {
        path: LOGIN_PATH,
        query: to.fullPath === preferences.app.defaultHomePath
          ? {}
          : { redirect: encodeURIComponent(to.fullPath) },
        replace: true,
      };
    }

    // 已生成动态路由 → 放行
    if (accessStore.isAccessChecked) {
      return true;
    }

    // 生成动态路由
    const userInfo = userStore.userInfo || (await authStore.fetchUserInfo());
    const userRoles = userInfo.roles ?? [];

    const { accessibleMenus, accessibleRoutes } = await generateAccess({
      roles: userRoles,
      router,
      routes: accessRoutes,
    });

    accessStore.setAccessMenus(accessibleMenus);
    accessStore.setAccessRoutes(accessibleRoutes);
    accessStore.setIsAccessChecked(true);

    return {
      ...router.resolve(decodeURIComponent((from.query.redirect as string) || to.fullPath)),
      replace: true,
    };
  });
}

/** 创建路由守卫 */
function createRouterGuard(router: Router) {
  setupCommonGuard(router);
  setupAccessGuard(router);
}

export { createRouterGuard };
```

### 权限访问控制 (access.ts)
```typescript
import type { ComponentRecordType, GenerateMenuAndRoutesOptions } from '@vben/types';

import { generateAccessible } from '@vben/access';
import { preferences } from '@vben/preferences';

import { message } from 'antdv-next';

import { getAllMenusApi } from '#/api';
import { BasicLayout, IFrameView } from '#/layouts';
import { $t } from '#/locales';

const forbiddenComponent = () => import('#/views/_core/fallback/forbidden.vue');

async function generateAccess(options: GenerateMenuAndRoutesOptions) {
  const pageMap: ComponentRecordType = import.meta.glob('../views/**/*.vue');

  const layoutMap: ComponentRecordType = {
    BasicLayout,
    IFrameView,
  };

  return await generateAccessible(preferences.app.accessMode, {
    ...options,
    fetchMenuListAsync: async () => {
      message.loading({ content: `${$t('common.loadingMenu')}...`, duration: 1.5 });
      return await getAllMenusApi();
    },
    forbiddenComponent,
    layoutMap,
    pageMap,
  });
}

export { generateAccess };
```
