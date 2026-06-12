---
alwaysApply: false
globs: frontend/vue-vben-admin/**/layouts/**/*.vue
---
# 后管前端Layouts布局层规范

> **适用范围**：`frontend/vue-vben-admin/` 目录下所有 `layouts/**/*.vue` 文件

## 一、强制要求（AI必须100%遵守，违反即错误）

1. **必须使用Vue 3 Composition API**：所有布局组件必须使用<script setup>语法
2. **必须使用TypeScript**：所有布局组件必须使用TypeScript编写
3. **必须使用Ant Design Vue布局组件**：布局必须使用Ant Design Vue的Layout组件

## 二、推荐做法（AI优先采用，效果更好）

1. **使用插槽传递内容**：使用slot传递动态内容
2. **使用响应式设计**：布局必须支持响应式
3. **使用组件组合**：将复杂布局拆分为多个小组件

## 三、禁止行为（AI绝对不能生成）

1. **禁止硬编码布局结构**：禁止在布局中硬编码页面内容
2. **禁止使用内联样式**：禁止使用内联样式定义布局

## 四、目录结构

```
src/layouts/
├── BasicLayout.vue         # 基础布局（侧边栏+顶部导航+内容区）
├── BlankLayout.vue         # 空白布局（登录页等）
└── components/             # 布局子组件
    ├── Sidebar.vue        # 侧边栏组件
    ├── Header.vue         # 顶部导航组件
    └── Footer.vue         # 底部组件
```

## 五、标准代码示例

### 基础布局组件
```vue
<script lang="ts" setup>
import { Layout } from 'antdv-next';
import Sidebar from './components/Sidebar.vue';
import Header from './components/Header.vue';
import Footer from './components/Footer.vue';

const { Sider, Content } = Layout;
</script>

<template>
  <Layout class="app-layout">
    <Sider width="200px" collapsible>
      <Sidebar />
    </Sider>
    <Layout>
      <Header />
      <Content class="app-content">
        <router-view />
      </Content>
      <Footer />
    </Layout>
  </Layout>
</template>

<style lang="less" scoped>
.app-layout {
  min-height: 100vh;
}

.app-content {
  padding: 24px;
  background: #f5f5f5;
}
</style>
```

### 侧边栏组件
```vue
<script lang="ts" setup>
import { useRouter } from 'vue-router';
import { Menu } from 'antdv-next';

const router = useRouter();

function handleMenuClick(item: { key: string }) {
  router.push(item.key);
}
</script>

<template>
  <div class="sidebar">
    <div class="logo">管理系统</div>
    <Menu mode="inline" @click="handleMenuClick">
      <Menu.Item key="/dashboard">仪表盘</Menu.Item>
      <Menu.Item key="/system/user">用户管理</Menu.Item>
      <Menu.Item key="/system/role">角色管理</Menu.Item>
    </Menu>
  </div>
</template>

<style lang="less" scoped>
.sidebar {
  height: 100%;
  padding-top: 20px;
}

.logo {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}
</style>
```

### 顶部导航组件
```vue
<script lang="ts" setup>
import { useUserStore } from '#/store';

const userStore = useUserStore();
</script>

<template>
  <Layout.Header class="header">
    <div class="header-left">
      <a @click="userStore.toggleSidebar">菜单</a>
    </div>
    <div class="header-right">
      <span>{{ userStore.userInfo?.name }}</span>
      <button @click="userStore.logout">退出</button>
    </div>
  </Layout.Header>
</template>

<style lang="less" scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
}

.header-left,
.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}
</style>
```