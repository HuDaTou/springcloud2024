---
alwaysApply: false
globs: frontend/vue-vben-admin/**/store/**/*.ts
---
# 后管前端Store状态管理规范

> **适用范围**：`frontend/vue-vben-admin/` 目录下所有 `store/**/*.ts` 文件

## 一、强制要求（AI必须100%遵守，违反即错误）

1. **必须使用Pinia**：状态管理必须使用Pinia
2. **必须使用defineStore**：必须使用Pinia的 `defineStore` 定义store
3. **必须使用TypeScript**：所有store必须使用TypeScript编写
4. **必须使用 Composition API 风格**：使用 `defineStore('name', () => { ... })` 语法
5. **业务store放在 `#/store`**：认证等业务逻辑store放在 `src/store/` 下
6. **框架store从 `@vben/stores` 导入**：`useAccessStore`、`useUserStore` 等框架级store从 `@vben/stores` 导入

## 二、推荐做法（AI优先采用，效果更好）

1. **按功能模块拆分store**：将相关状态放在同一个store中
2. **使用 `@vben/stores` 的 `resetAllStores`**：登出时重置所有store
3. **异步操作用 try/catch/finally**：登录等异步操作使用 try/catch/finally 控制 loading 状态

## 三、禁止行为（AI绝对不能生成）

1. **禁止直接修改state**：禁止在组件中直接修改store的state
2. **禁止在getters中执行异步操作**：getters必须是纯函数
3. **禁止使用Vuex**：禁止使用Vuex或其他状态管理库

## 四、目录结构

```
src/
├── store/                   # 业务store
│   ├── index.ts             # store导出 (export * from './auth')
│   └── auth.ts              # 认证store（登录/登出/用户信息）
└── （框架store来自 @vben/stores）
    ├── useAccessStore       # 访问权限store（token、权限码、菜单）
    └── useUserStore         # 用户信息store
```

## 五、标准代码示例

### 认证Store (store/auth.ts)
```typescript
import type { Recordable, UserInfo } from '@vben/types';

import { ref } from 'vue';
import { useRouter } from 'vue-router';

import { LOGIN_PATH } from '@vben/constants';
import { preferences } from '@vben/preferences';
import { resetAllStores, useAccessStore, useUserStore } from '@vben/stores';

import { notification } from 'antdv-next';
import { defineStore } from 'pinia';

import { getAccessCodesApi, getUserInfoApi, loginApi, logoutApi } from '#/api';
import { $t } from '#/locales';

export const useAuthStore = defineStore('auth', () => {
  const accessStore = useAccessStore();
  const userStore = useUserStore();
  const router = useRouter();

  const loginLoading = ref(false);

  /**
   * 异步处理登录操作
   * @param params 登录表单数据
   * @param onSuccess 成功之后的回调函数
   */
  async function authLogin(
    params: Recordable<any>,
    onSuccess?: () => Promise<void> | void,
  ) {
    let userInfo: null | UserInfo = null;
    try {
      loginLoading.value = true;
      const { accessToken } = await loginApi(params);

      if (accessToken) {
        accessStore.setAccessToken(accessToken);

        // 并行获取用户信息和权限码
        const [fetchUserInfoResult, accessCodes] = await Promise.all([
          fetchUserInfo(),
          getAccessCodesApi(),
        ]);

        userInfo = fetchUserInfoResult;

        userStore.setUserInfo(userInfo);
        accessStore.setAccessCodes(accessCodes);

        if (accessStore.loginExpired) {
          accessStore.setLoginExpired(false);
        } else {
          onSuccess
            ? await onSuccess?.()
            : await router.push(userInfo.homePath || preferences.app.defaultHomePath);
        }

        if (userInfo?.realName) {
          notification.success({
            description: `${$t('authentication.loginSuccessDesc')}:${userInfo?.realName}`,
            duration: 3,
            title: $t('authentication.loginSuccess'),
          });
        }
      }
    } finally {
      loginLoading.value = false;
    }

    return { userInfo };
  }

  /** 登出 */
  async function logout(redirect: boolean = true) {
    try {
      await logoutApi();
    } catch {
      // 不做任何处理
    }

    resetAllStores();
    accessStore.setLoginExpired(false);

    await router.replace({
      path: LOGIN_PATH,
      query: redirect
        ? { redirect: encodeURIComponent(router.currentRoute.value.fullPath) }
        : {},
    });
  }

  /** 获取用户信息 */
  async function fetchUserInfo() {
    const userInfo = await getUserInfoApi();
    userStore.setUserInfo(userInfo);
    return userInfo;
  }

  function $reset() {
    loginLoading.value = false;
  }

  return {
    $reset,
    authLogin,
    fetchUserInfo,
    loginLoading,
    logout,
  };
});
```

### Store统一导出 (store/index.ts)
```typescript
export * from './auth';
```
