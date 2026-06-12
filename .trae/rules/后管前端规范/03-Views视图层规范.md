---
alwaysApply: false
globs: frontend/vue-vben-admin/**/views/**/*.vue
---
# 后管前端Views视图层规范

> **适用范围**：`frontend/vue-vben-admin/` 目录下所有 `views/**/*.vue` 文件

## 一、强制要求（AI必须100%遵守，违反即错误）

1. **必须使用<script setup>语法**：所有Vue组件必须使用Composition API
2. **必须使用TypeScript**：所有组件必须使用TypeScript
3. **必须使用国际化**：所有用户可见文本必须使用`$t()`函数
4. **必须使用Page组件**：所有页面必须使用Page组件作为容器
5. **必须使用VXE Table**：表格页面必须使用VXE Table组件
6. **必须使用useVbenForm**：表单必须使用useVbenForm封装

## 二、推荐做法（AI优先采用，效果更好）

1. **按功能模块拆分组件**：将复杂页面拆分为多个小组件
2. **使用组合式函数**：将可复用逻辑提取到composables中
3. **使用异步组件**：大型组件使用defineAsyncComponent懒加载

## 三、禁止行为（AI绝对不能生成）

1. **禁止使用Options API**：禁止使用Vue 2的Options API语法
2. **禁止硬编码文本**：所有用户可见文本禁止直接硬编码
3. **禁止在template中写复杂逻辑**：模板中的复杂逻辑必须提取

## 四、目录结构

```
src/views/
├── _core/                   # 核心页面（登录、404等）
├── system/                  # 系统管理
│   ├── user/                # 用户管理
│   │   ├── list.vue         # 用户列表
│   │   ├── data.ts          # 配置文件
│   │   └── modules/         # 子组件
└── dashboard/               # 仪表盘
```

## 五、标准代码示例

### 列表页面（list.vue）
```vue
<script lang="ts" setup>
import { ref } from 'vue';
import { Page, useVbenDrawer } from '@vben/common-ui';
import { Button, Plus } from 'antdv-next';

import { useVbenVxeGrid } from '#/adapter/vxe-table';
import { getUserList, deleteUser } from '#/api';
import { $t } from '#/locales';

import { useColumns } from './data';
import Form from './modules/form.vue';

const [FormDrawer, formDrawerApi] = useVbenDrawer({
  connectedComponent: Form,
  destroyOnClose: true,
});

const [Grid, gridApi] = useVbenVxeGrid({
  gridOptions: {
    columns: useColumns(),
    proxyConfig: {
      ajax: { query: async ({ page }) => getUserList({ page: page.currentPage }) },
    },
  },
});

function onCreate() { formDrawerApi.setData({}).open(); }
function onEdit(row: any) { formDrawerApi.setData(row).open(); }
function onDelete(row: any) { deleteUser(row.id).then(() => gridApi.query()); }
</script>

<template>
  <Page>
    <FormDrawer @success="gridApi.query" />
    <Grid>
      <template #toolbar-tools>
        <Button type="primary" @click="onCreate">
          <Plus /> {{ $t('common.create') }}
        </Button>
      </template>
    </Grid>
  </Page>
</template>
```

### 配置文件（data.ts）
```typescript
export function useColumns() {
  return [
    { field: 'name', title: $t('system.user.name') },
    { field: 'status', title: $t('system.user.status'), cellRender: { name: 'CellTag' } },
    { 
      field: 'operation', 
      title: $t('common.operation'), 
      cellRender: { name: 'CellOperation', attrs: { onClick: onActionClick } } 
    },
  ];
}
```

### 表单弹窗组件（modules/form.vue）
```vue
<script lang="ts" setup>
import { useVbenForm, useVbenDrawer } from '@vben/common-ui';

import { createUser, updateUser } from '#/api';

const [Form, formApi] = useVbenForm({ schema: useFormSchema() });

const [Drawer, drawerApi] = useVbenDrawer({
  async onConfirm() {
    const { valid } = await formApi.validate();
    if (!valid) return;
    const values = await formApi.getValues();
    await (id ? updateUser(id, values) : createUser(values));
    drawerApi.close();
  },
});
</script>

<template>
  <Drawer :title="id ? '编辑' : '创建'">
    <Form />
  </Drawer>
</template>
```