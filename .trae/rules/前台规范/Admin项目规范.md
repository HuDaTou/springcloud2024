---
alwaysApply: false
globs:
  - "frontend/admin/**/*"
  - "!**/node_modules/**"
  - "!**/dist/**"
---
# Admin 管理后台项目规范

> **适用范围**：`frontend/admin/` 目录下的所有文件

## 项目技术栈

| 技术 | 说明 |
|------|------|
| Vue 3 + Vite | 核心框架 |
| Ant Design Vue 4 | UI 组件库 |
| Pinia | 状态管理 |
| Vue Router 4 | 路由管理 |
| UnoCSS + Tailwind | CSS 方案 |
| TypeScript | 类型安全 |

## 目录结构

```
admin/src/
├── api/                    # API 接口管理
│   ├── common/            # 通用接口（登录、用户、菜单）
│   ├── blog/              # 博客相关接口
│   ├── role/              # 角色相关接口
│   ├── log/               # 日志相关接口
│   └── user/              # 用户相关接口
├── assets/                # 静态资源
│   ├── images/            # 图片资源
│   └── styles/            # 全局样式
├── components/             # 公共组件
│   ├── base-loading/      # 基础加载组件
│   ├── icons/             # 图标组件
│   ├── layout/            # 布局组件
│   ├── modal/             # 弹窗组件
│   └── page-container/    # 页面容器
├── composables/            # 组合式函数
│   ├── access.ts          # 权限控制
│   ├── api.ts             # API 调用封装
│   ├── authorization.ts   # 授权管理
│   ├── loading.ts         # 加载状态
│   └── theme.ts           # 主题管理
├── config/                 # 配置文件
├── directive/              # 指令
├── enums/                  # 枚举定义
│   ├── http-enum.ts       # HTTP 枚举
│   └── loading-enum.ts    # 加载动画枚举
├── layouts/                # 布局组件
│   ├── basic-layout/      # 基础布局
│   └── multi-tab/         # 多标签页
├── locales/               # 国际化
│   └── lang/              # 语言文件
│       ├── global/        # 全局翻译
│       └── pages/         # 页面翻译
├── pages/                  # 页面组件
│   ├── account/           # 账户中心
│   ├── blog/              # 博客管理
│   ├── cms/               # 内容管理
│   ├── common/           # 通用页面
│   ├── exception/         # 异常页面
│   ├── system/            # 系统管理
│   └── welcome/           # 首页
├── router/                 # 路由配置
├── stores/                 # Pinia 状态管理
├── types/                  # 类型定义
└── utils/                  # 工具函数
```

## API 封装

```typescript
// utils/request.ts
export interface ResponseBody<T = any> {
  code: number
  data?: T
  msg: string
}

export function useGet<R = any, T = any>(
  url: string,
  params?: T,
  config?: RequestConfigExtra
): Promise<ResponseBody<R>> {
  return instancePromise({ url, params, method: RequestEnum.GET, ...config })
}

export function usePost<R = any, T = any>(
  url: string,
  data?: T,
  config?: RequestConfigExtra
): Promise<ResponseBody<R>> {
  return instancePromise({ url, data, method: RequestEnum.POST, ...config })
}

// api/user/index.ts
export async function userList() {
  return useGet('/user/list').catch(msg => message.warn(msg))
}

export async function userDetail(id: string) {
  return useGet(`/user/details/${id}`)
}

export async function userDelete(ids: string[]) {
  return useDelete('/user/delete', { ids })
}
```

## Store 定义

```typescript
// stores/user.ts
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo>()
  const token = ref<string>()

  const isLoggedIn = computed(() => !!token.value)
  const nickname = computed(() => userInfo.value?.nickname)

  async function fetchUserInfo() {
    const { data } = await getUserInfoApi()
    userInfo.value = data
  }

  function logout() {
    token.value = null
    userInfo.value = undefined
  }

  return {
    userInfo,
    token,
    isLoggedIn,
    nickname,
    fetchUserInfo,
    logout
  }
})
```

## 路由配置

```typescript
// router/static-routes.ts
export const staticRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/common/login.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/pages/exception/404.vue')
  }
]

// router/router-guard.ts
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()

  if (to.path === '/login') {
    next()
  } else {
    if (userStore.token) {
      next()
    } else {
      next({ path: '/login', query: { redirect: to.fullPath } })
    }
  }
})
```

## 组件使用

```vue
<script setup lang="ts">
import { message } from 'ant-design-vue'

// 使用 Ant Design Vue 组件
</script>

<template>
  <a-button type="primary" @click="handleClick">
    提交
  </a-button>
</template>
```
