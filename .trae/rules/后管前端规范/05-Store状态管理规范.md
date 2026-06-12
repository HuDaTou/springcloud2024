---
alwaysApply: false
globs: frontend/vue-vben-admin/**/store/**/*.ts
---
# 后管前端Store状态管理规范

> **适用范围**：`frontend/vue-vben-admin/` 目录下所有 `store/**/*.ts` 文件

## 一、强制要求（AI必须100%遵守，违反即错误）

1. **必须使用Pinia**：状态管理必须使用Pinia，禁止使用Vuex
2. **必须使用defineStore**：必须使用Pinia的defineStore定义store
3. **必须使用TypeScript**：所有store必须使用TypeScript编写
4. **必须定义state类型**：必须为state定义明确的TypeScript类型
5. **必须使用actions处理异步**：所有异步操作必须放在actions中

## 二、推荐做法（AI优先采用，效果更好）

1. **按功能模块拆分store**：将相关状态放在同一个store中
2. **使用持久化存储**：使用pinia-plugin-persistedstate持久化必要状态
3. **使用组合式函数**：将可复用逻辑提取到composables中

## 三、禁止行为（AI绝对不能生成）

1. **禁止直接修改state**：禁止在组件中直接修改state
2. **禁止在getters中执行异步操作**：getters必须是纯函数

## 四、目录结构

```
src/store/
├── index.ts               # store统一导出
├── useAppStore.ts         # 应用全局状态
├── useUserStore.ts        # 用户状态
└── modules/               # 业务模块store
```

## 五、标准代码示例

### 用户状态管理
```typescript
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo | null>(null);
  const token = ref<string>('');

  const isLogin = computed(() => !!token.value);

  function setToken(val: string) {
    token.value = val;
  }

  function setUserInfo(val: UserInfo) {
    userInfo.value = val;
  }

  async function login(params: LoginParams) {
    const result = await loginApi(params);
    setToken(result.accessToken);
    return result;
  }

  function logout() {
    token.value = '';
    userInfo.value = null;
  }

  return {
    userInfo,
    token,
    isLogin,
    setToken,
    setUserInfo,
    login,
    logout,
  };
}, {
  persist: { paths: ['token'] },
});

interface UserInfo {
  id: string;
  username: string;
  name: string;
}

interface LoginParams {
  username: string;
  password: string;
}
```

### 应用状态管理
```typescript
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useAppStore = defineStore('app', () => {
  const theme = ref<'light' | 'dark'>('light');
  const sidebarCollapsed = ref(false);
  const locale = ref('zh-CN');

  function toggleTheme() {
    theme.value = theme.value === 'light' ? 'dark' : 'light';
  }

  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value;
  }

  function setLocale(val: string) {
    locale.value = val;
  }

  return {
    theme,
    sidebarCollapsed,
    locale,
    toggleTheme,
    toggleSidebar,
    setLocale,
  };
}, {
  persist: true,
});
```

### Store统一导出
```typescript
export { useUserStore } from './useUserStore';
export { useAppStore } from './useAppStore';
```