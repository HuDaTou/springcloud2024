---
alwaysApply: false
globs: frontend/vue-vben-admin/**/views/**/*.vue
---
# 后管前端Views视图层规范

> **适用范围**：`frontend/vue-vben-admin/` 目录下所有 `views/**/*.vue` 文件

## 一、强制要求（AI必须100%遵守，违反即错误）

1. **必须使用`<script lang="ts" setup>`语法**：所有Vue组件必须使用Composition API + TypeScript
2. **必须使用国际化**：所有用户可见文本必须使用`$t()`函数
3. **必须使用Page组件**：所有页面必须使用 `<Page auto-content-height>` 作为容器
4. **必须使用 `useVbenVxeGrid`**：表格页面必须使用 `useVbenVxeGrid`，返回 `[Grid, gridApi]`
5. **必须使用 `useVbenForm`**：表单必须使用 `useVbenForm`，返回 `[Form, formApi]`
6. **必须使用 `useVbenDrawer`**：弹窗表单必须使用 `useVbenDrawer`，返回 `[Drawer, drawerApi]`
7. **列定义和表单schema必须放在 `data.ts`**：从 `list.vue` 中分离到 `data.ts`

## 二、推荐做法（AI优先采用，效果更好）

1. **按功能模块拆分组件**：将复杂页面拆分为多个小组件
2. **使用组合式函数**：将可复用逻辑提取到composables中
3. **使用 `as VxeTableGridOptions<T>`**：为表格配置添加类型断言以获取完整的列类型推断

## 三、禁止行为（AI绝对不能生成）

1. **禁止使用Options API**：禁止使用Vue 2的Options API语法
2. **禁止硬编码文本**：所有用户可见文本禁止直接硬编码
3. **禁止在template中写复杂逻辑**：模板中的复杂逻辑必须提取到 `<script setup>` 中
4. **禁止使用 `export async function` 直接导出**：API函数统一放在 `api/` 目录

## 四、目录结构

```
src/views/
├── _core/                   # 核心页面（登录、fallback等）
│   ├── authentication/      # 登录相关
│   │   ├── login.vue
│   │   ├── register.vue
│   │   └── ...
│   └── fallback/            # 错误页面
│       ├── not-found.vue
│       ├── forbidden.vue
│       └── ...
├── system/                  # 系统管理
│   ├── user/                # 用户管理
│   │   ├── list.vue         # 列表页（Grid + 搜索表单 + 操作）
│   │   ├── data.ts          # 列定义(useColumns)、表单schema(useFormSchema)、搜索表单schema(useGridFormSchema)
│   │   └── modules/         # 子组件
│   │       └── form.vue     # 新增/编辑弹窗表单
│   ├── role/
│   ├── dept/
│   └── menu/
└── dashboard/               # 仪表盘
```

## 五、标准代码示例

### 列表页面 (list.vue)
```vue
<script lang="ts" setup>
import type { OnActionClickParams, VxeTableGridOptions } from '#/adapter/vxe-table';
import type { SystemUserApi } from '#/api';

import { ref } from 'vue';

import { Page, useVbenDrawer } from '@vben/common-ui';
import { Plus } from '@vben/icons';

import { Button, message, Modal } from 'antdv-next';

import { useVbenVxeGrid } from '#/adapter/vxe-table';
import { deleteUser, getUserList, updateUser } from '#/api';
import { $t } from '#/locales';

import { useColumns, useGridFormSchema } from './data';
import Form from './modules/form.vue';

const [FormDrawer, formDrawerApi] = useVbenDrawer({
  connectedComponent: Form,
  destroyOnClose: true,
});

const [Grid, gridApi] = useVbenVxeGrid({
  formOptions: {
    fieldMappingTime: [['createTime', ['startTime', 'endTime']]],
    schema: useGridFormSchema(),
    submitOnChange: true,
  },
  gridOptions: {
    columns: useColumns(onActionClick, onStatusChange),
    height: 'auto',
    keepSource: true,
    proxyConfig: {
      ajax: {
        query: async ({ page }, formValues) => {
          return await getUserList({
            page: page.currentPage,
            pageSize: page.pageSize,
            ...formValues,
          });
        },
      },
    },
    rowConfig: {
      keyField: 'id',
    },
    toolbarConfig: {
      custom: true,
      export: false,
      refresh: true,
      search: true,
      zoom: true,
    },
  } as VxeTableGridOptions<SystemUserApi.SystemUser>,
});

// 操作列点击回调
function onActionClick(e: OnActionClickParams<SystemUserApi.SystemUser>) {
  switch (e.code) {
    case 'delete': onDelete(e.row); break;
    case 'edit': onEdit(e.row); break;
  }
}

// 状态切换回调
async function onStatusChange(newStatus: number, row: SystemUserApi.SystemUser) {
  try {
    await new Promise((resolve, reject) => {
      Modal.confirm({
        content: `确认切换「${row.name}」的状态？`,
        onOk: () => resolve(true),
        onCancel: () => reject(new Error('已取消')),
        title: '切换状态',
      });
    });
    await updateUser(row.id, { status: newStatus });
    return true;
  } catch {
    return false;
  }
}

// 编辑
function onEdit(row: SystemUserApi.SystemUser) {
  formDrawerApi.setData(row).open();
}

// 删除
function onDelete(row: SystemUserApi.SystemUser) {
  deleteUser(row.id).then(() => gridApi.query());
}

// 刷新
function onRefresh() {
  gridApi.query();
}

// 新增
function onCreate() {
  formDrawerApi.setData({}).open();
}
</script>

<template>
  <Page auto-content-height>
    <FormDrawer @success="onRefresh" />
    <Grid :table-title="$t('system.user.list')">
      <template #toolbar-tools>
        <Button type="primary" @click="onCreate">
          <Plus class="size-5" />
          {{ $t('ui.actionTitle.create', [$t('system.user.name')]) }}
        </Button>
      </template>
    </Grid>
  </Page>
</template>
```

### 配置文件 (data.ts)
```typescript
import type { VbenFormSchema } from '#/adapter/form';
import type { OnActionClickFn, VxeTableGridColumns } from '#/adapter/vxe-table';
import type { SystemUserApi } from '#/api';

import { $t } from '#/locales';

/** 新增/编辑表单schema */
export function useFormSchema(): VbenFormSchema[] {
  return [
    {
      component: 'Input',
      fieldName: 'name',
      label: $t('system.user.name'),
      rules: 'required',
    },
    {
      component: 'RadioGroup',
      componentProps: {
        buttonStyle: 'solid',
        options: [
          { label: $t('common.enabled'), value: 1 },
          { label: $t('common.disabled'), value: 0 },
        ],
        optionType: 'button',
      },
      defaultValue: 1,
      fieldName: 'status',
      label: $t('system.user.status'),
    },
    {
      component: 'Textarea',
      fieldName: 'remark',
      label: $t('system.user.remark'),
    },
  ];
}

/** 搜索表单schema */
export function useGridFormSchema(): VbenFormSchema[] {
  return [
    {
      component: 'Input',
      fieldName: 'name',
      label: $t('system.user.name'),
    },
    {
      component: 'Select',
      componentProps: {
        allowClear: true,
        options: [
          { label: $t('common.enabled'), value: 1 },
          { label: $t('common.disabled'), value: 0 },
        ],
      },
      fieldName: 'status',
      label: $t('system.user.status'),
    },
    {
      component: 'RangePicker',
      fieldName: 'createTime',
      label: $t('system.user.createTime'),
    },
  ];
}

/** 表格列定义 */
export function useColumns<T = SystemUserApi.SystemUser>(
  onActionClick: OnActionClickFn<T>,
  onStatusChange?: (newStatus: any, row: T) => PromiseLike<boolean | undefined>,
): VxeTableGridColumns {
  return [
    { field: 'name', title: $t('system.user.name'), width: 200 },
    { field: 'id', title: $t('system.user.id'), width: 200 },
    {
      cellRender: {
        attrs: { beforeChange: onStatusChange },
        name: onStatusChange ? 'CellSwitch' : 'CellTag',
      },
      field: 'status',
      title: $t('system.user.status'),
      width: 100,
    },
    { field: 'remark', minWidth: 100, title: $t('system.user.remark') },
    { field: 'createTime', title: $t('system.user.createTime'), width: 200 },
    {
      align: 'center',
      cellRender: {
        attrs: {
          nameField: 'name',
          nameTitle: $t('system.user.name'),
          onClick: onActionClick,
        },
        name: 'CellOperation',
      },
      field: 'operation',
      fixed: 'right',
      title: $t('system.user.operation'),
      width: 130,
    },
  ];
}
```

### 表单弹窗组件 (modules/form.vue)
```vue
<script lang="ts" setup>
import type { SystemUserApi } from '#/api/system/user';

import { computed, ref } from 'vue';

import { useVbenDrawer } from '@vben/common-ui';

import { useVbenForm } from '#/adapter/form';
import { createUser, updateUser } from '#/api/system/user';

import { useFormSchema } from '../data';

const emits = defineEmits(['success']);

const [Form, formApi] = useVbenForm({
  schema: useFormSchema(),
  showDefaultActions: false,
});

const id = ref();

const [Drawer, drawerApi] = useVbenDrawer({
  async onConfirm() {
    const { valid } = await formApi.validate();
    if (!valid) return;
    const values = await formApi.getValues();
    drawerApi.lock();
    (id.value ? updateUser(id.value, values) : createUser(values))
      .then(() => {
        emits('success');
        drawerApi.close();
      })
      .catch(() => { drawerApi.unlock(); });
  },
  async onOpenChange(isOpen) {
    if (isOpen) {
      const data = drawerApi.getData<SystemUserApi.SystemUser>();
      formApi.resetForm();
      if (data) {
        id.value = data.id;
      } else {
        id.value = undefined;
      }
      // 注意：需要在 nextTick 后 setValues
    }
  },
});

const formData = ref<SystemUserApi.SystemUser>();

const getDrawerTitle = computed(() => {
  return formData.value?.id
    ? `编辑${$t('system.user.name')}`
    : `新增${$t('system.user.name')}`;
});
</script>

<template>
  <Drawer :title="getDrawerTitle">
    <Form />
  </Drawer>
</template>
```
