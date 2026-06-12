---
alwaysApply: false
globs:
  - "frontend/blog/**/*"
  - "!**/node_modules/**"
  - "!**/dist/**"
---
# Blog 博客前台项目规范

> **适用范围**：`frontend/blog/` 目录下的所有文件

## 项目技术栈

| 技术 | 说明 |
|------|------|
| Vue 3 + Vite | 核心框架 |
| Element Plus | UI 组件库 |
| Pinia | 状态管理 |
| Vue Router 4 | 路由管理 |
| Tailwind CSS + SCSS | CSS 方案 |
| TypeScript | 类型安全 |

## 目录结构

```
blog/src/
├── apis/                   # API 接口
│   ├── article/           # 文章接口
│   ├── home/              # 首页接口
│   ├── link/              # 友链接口
│   ├── like/              # 点赞接口
│   ├── music/             # 音乐接口
│   ├── tag/               # 标签接口
│   └── user/              # 用户接口
├── assets/                 # 静态资源
│   ├── cursor/            # 自定义鼠标
│   ├── icons/             # 图标资源
│   └── images/            # 图片资源
├── components/            # 公共组件
├── config/                 # 配置文件
├── const/                  # 常量定义
├── directives/             # 指令
├── router/                 # 路由配置
├── store/                  # Pinia 状态管理
│   └── modules/           # Store 模块
│       ├── loading.ts     # 加载状态
│       ├── music.ts       # 音乐播放
│       ├── pagination.ts # 分页状态
│       ├── user.ts        # 用户信息
│       └── website.ts     # 网站配置
├── styles/                 # 样式文件
│   ├── element_style.scss # Element Plus 覆盖
│   ├── mixin.scss         # 混入
│   ├── reset.scss         # 重置样式
│   ├── scrollBar.scss     # 滚动条样式
│   ├── theme.scss         # 主题样式
│   └── variable.scss      # 变量定义
├── types/                  # 类型定义
├── utils/                  # 工具函数
└── views/                  # 页面组件
    ├── About/             # 关于页面
    ├── Amusement/         # 娱乐功能
    ├── Article/           # 文章详情
    ├── Home/              # 首页
    ├── Link/              # 友链页面
    ├── Music/             # 音乐页面
    ├── Photo/             # 相册页面
    ├── Pigeonhole/        # 文章归档
    ├── Setting/           # 设置页面
    ├── Video/             # 视频页面
    └── Welcome/          # 欢迎页
```

## API 封装

```typescript
// utils/http.ts
const http: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API ?? '/',
  timeout: 60000
})

// apis/article/index.ts
export const getArticleDetail = (id: string | string[]) => {
  return http.request({
    url: `/article/detail/${id}`,
    method: 'get'
  })
}

export const addComment = (data: object) => {
  return http.request({
    url: '/comment/auth/add/comment',
    method: 'post',
    data
  })
}
```

## Store 定义

```typescript
// store/modules/user.ts
const useUserStore = defineStore('user', () => {
  const token = GET_TOKEN()
  const userInfo = shallowRef<UserInfo>()

  const getInfo = async () => {
    const res: any = await getUserInfo()
    if (res.code === 200) {
      userInfo.value = res.data
    }
  }

  return {
    token,
    userInfo,
    getInfo
  }
})

export default useUserStore
```

## 环境变量使用

```typescript
// .env 文件
VITE_APP_BASE_API=/api
VITE_APP_DOMAIN_NAME_FRONT=http://localhost:5173
VITE_MUSIC_FRONTEND_URL=

// 组件中使用
const env = import.meta.env
const domain = env.VITE_APP_DOMAIN_NAME_FRONT
```

## 页面组件示例

```vue
<script setup lang="ts">
import Main from "@/views/Home/Main/index.vue";
import Images from "@/views/Home/Images/index.vue";
import Brand from "@/views/Home/Brand/index.vue";
</script>

<template>
  <div class="home_container">
    <Images/>
    <Brand/>
    <div class="bg">
      <Main/>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.bg {
  transition: all 1s ease !important;
  background-color: var(--mao-background-color);
}

.home_container {
  width: 100%;
  @media screen and (max-width: 910px) {
    width: 100vw;
  }
}
</style>
```

## 样式变量

```scss
// styles/variable.scss
$primary-color: #409eff;
$border-radius: 4px;

// styles/mixin.scss
@mixin flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
}
```
