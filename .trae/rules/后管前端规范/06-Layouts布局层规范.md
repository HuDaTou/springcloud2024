---
alwaysApply: false
globs: frontend/vue-vben-admin/**/layouts/**/*.vue
---
# 后管前端Layouts布局层规范

> **适用范围**：`frontend/vue-vben-admin/` 目录下所有 `layouts/**/*.vue` 文件

## 一、强制要求（AI必须100%遵守，违反即错误）

1. **必须使用Vue 3 Composition API**：所有布局组件必须使用 `<script lang="ts" setup>` 语法
2. **必须使用TypeScript**：所有布局组件必须使用TypeScript编写
3. **必须使用 `@vben/layouts` 提供的布局组件**：使用 `BasicLayout`、`AuthPageLayout`，不要手写布局
4. **必须在 `layouts/index.ts` 中导出**：使用异步组件工厂模式导出

## 二、推荐做法（AI优先采用，效果更好）

1. **使用插槽自定义内容**：通过 `#user-dropdown`、`#notification`、`#extra`、`#lock-screen` 等插槽定制
2. **使用响应式设计**：布局自动支持响应式
3. **使用 `@vben/layouts` 的内置组件**：`UserDropdown`、`Notification`、`LockScreen` 等

## 三、禁止行为（AI绝对不能生成）

1. **禁止手写布局结构**：禁止自己手写 Sidebar/Header/Content 布局，必须使用 `@vben/layouts` 的 `BasicLayout`
2. **禁止硬编码文本**：所有用户可见文本必须使用 `$t()`
3. **禁止使用内联样式**：使用 UnoCSS 或 scoped CSS

## 四、目录结构

```
src/layouts/
├── index.ts              # 布局统一导出（异步组件工厂）
├── basic.vue             # 基础布局（包装 @vben/layouts 的 BasicLayout）
└── auth.vue              # 认证布局（包装 @vben/layouts 的 AuthPageLayout）
```

## 五、标准代码示例

### 布局统一导出 (layouts/index.ts)
```typescript
const BasicLayout = () => import('./basic.vue');
const AuthPageLayout = () => import('./auth.vue');

const IFrameView = () => import('@vben/layouts').then((m) => m.IFrameView);

export { AuthPageLayout, BasicLayout, IFrameView };
```

### 基础布局 (layouts/basic.vue)
```vue
<script lang="ts" setup>
import { computed, watch } from 'vue';
import { useRouter } from 'vue-router';

import { AuthenticationLoginExpiredModal } from '@vben/common-ui';
import { VBEN_DOC_URL, VBEN_GITHUB_URL } from '@vben/constants';
import { useWatermark } from '@vben/hooks';
import { BookOpenText, CircleHelp, SvgGithubIcon } from '@vben/icons';
import {
  BasicLayout,
  LockScreen,
  Notification,
  UserDropdown,
} from '@vben/layouts';
import { preferences, usePreferences } from '@vben/preferences';
import { useAccessStore, useUserStore } from '@vben/stores';
import { openWindow } from '@vben/utils';

import { $t } from '#/locales';
import { useAuthStore } from '#/store';
import LoginForm from '#/views/_core/authentication/login.vue';

const router = useRouter();
const userStore = useUserStore();
const authStore = useAuthStore();
const accessStore = useAccessStore();
const { destroyWatermark, updateWatermark } = useWatermark();
const { isDark } = usePreferences();

// 用户头像
const avatar = computed(() => {
  return userStore.userInfo?.avatar ?? preferences.app.defaultAvatar;
});

// 用户下拉菜单
const menus = computed(() => [
  {
    handler: () => router.push({ name: 'Profile' }),
    icon: 'lucide:user',
    text: $t('page.auth.profile'),
  },
  {
    handler: () => openWindow(VBEN_DOC_URL, { target: '_blank' }),
    icon: BookOpenText,
    text: $t('ui.widgets.document'),
  },
  {
    handler: () => openWindow(VBEN_GITHUB_URL, { target: '_blank' }),
    icon: SvgGithubIcon,
    text: 'GitHub',
  },
]);

async function handleLogout() {
  await authStore.logout(false);
}

function handleClickLogo() {}

// 水印处理
watch(
  () => ({
    enable: preferences.app.watermark,
    content: preferences.app.watermarkContent,
    isDark: isDark.value,
  }),
  async ({ enable, content, isDark: isDarkValue }) => {
    if (enable) {
      await updateWatermark({
        content: content || `${userStore.userInfo?.username} - ${userStore.userInfo?.realName}`,
        // ...
      });
    } else {
      destroyWatermark();
    }
  },
  { immediate: true },
);
</script>

<template>
  <BasicLayout @clear-preferences-and-logout="handleLogout" @click-logo="handleClickLogo">
    <template #user-dropdown>
      <UserDropdown
        :avatar
        :menus
        :text="userStore.userInfo?.realName"
        @logout="handleLogout"
        @clear-preferences-and-logout="handleLogout"
      />
    </template>
    <template #extra>
      <AuthenticationLoginExpiredModal
        v-model:open="accessStore.loginExpired"
        :avatar
      >
        <LoginForm />
      </AuthenticationLoginExpiredModal>
    </template>
    <template #lock-screen>
      <LockScreen :avatar @to-login="handleLogout" />
    </template>
  </BasicLayout>
</template>
```

### 认证布局 (layouts/auth.vue)
```vue
<script lang="ts" setup>
import { computed } from 'vue';

import { AuthPageLayout } from '@vben/layouts';
import { preferences } from '@vben/preferences';

import { $t } from '#/locales';

const appName = computed(() => preferences.app.name);
const logo = computed(() => preferences.logo.source);
const clickLogo = () => {};
</script>

<template>
  <AuthPageLayout
    :app-name="appName"
    :logo="logo"
    :page-description="$t('authentication.pageDesc')"
    :page-title="$t('authentication.pageTitle')"
    :click-logo="clickLogo"
  />
</template>
```
