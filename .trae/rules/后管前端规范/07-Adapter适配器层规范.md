---
alwaysApply: false
globs: frontend/vue-vben-admin/**/adapter/**/*.ts
---
# 后管前端Adapter适配器层规范

> **适用范围**：`frontend/vue-vben-admin/` 目录下所有 `adapter/**/*.ts` 文件

## 一、强制要求

1. **必须使用TypeScript**
2. **适配器分三个文件**：`vxe-table.ts`（表格）、`form.ts`（表单）、`component/index.ts`（组件类型定义和注册）

## 二、推荐做法

1. `vxe-table.ts`：用 `setupVbenVxeTable` 全局配置，注册自定义渲染器（CellImage/CellTag/CellSwitch/CellOperation），导出 `useVbenVxeGrid`
2. `form.ts`：用 `setupVbenForm` 全局配置 `modelPropNameMap` 和校验规则（`required`/`selectRequired`），导出 `useVbenForm`
3. `component/index.ts`：定义 `ComponentType`（所有组件名的联合类型）和 `ComponentPropsMap`（组件→Props类型映射），用 `globalShareState.setComponents()` 注册，输入组件用 `withDefaultPlaceholder()` 包装自动添加 placeholder

## 三、禁止行为

1. **禁止直接使用第三方组件**：业务组件必须通过adapter
2. **禁止硬编码配置**

## 四、核心模式

### 表格适配器结构
```typescript
// adapter/vxe-table.ts
import { setupVbenVxeTable, useVbenVxeGrid as useGrid } from '@vben/plugins/vxe-table';
import { Button, Image, Popconfirm, Switch, Tag } from 'antdv-next';

// 1. 全局配置 + 注册渲染器
setupVbenVxeTable({
  configVxeTable: (vxeUI) => {
    vxeUI.setConfig({ grid: { align: 'center', border: false, /* ... */ } });
    vxeUI.renderer.add('CellTag', { /* ... */ });
    vxeUI.renderer.add('CellSwitch', { /* ... */ });
    vxeUI.renderer.add('CellOperation', { /* ... */ });
  },
});

// 2. 导出封装
export const useVbenVxeGrid = <T extends Record<string, any>>(
  ...rest: Parameters<typeof useGrid<T, ComponentType, ComponentPropsMap>>
) => useGrid<T, ComponentType, ComponentPropsMap>(...rest);
```

### 表单适配器结构
```typescript
// adapter/form.ts
import { setupVbenForm, useVbenForm as useForm, z } from '@vben/common-ui';

// 全局配置：modelPropNameMap + 校验规则
async function initSetupVbenForm() {
  setupVbenForm<ComponentType>({
    config: {
      baseModelPropName: 'value',
      modelPropNameMap: { Checkbox: 'checked', Radio: 'checked', Switch: 'checked', Upload: 'fileList' },
    },
    defineRules: {
      required: (value, _params, ctx) => { /* $t('ui.formRules.required', [ctx.label]) */ },
      selectRequired: (value, _params, ctx) => { /* $t('ui.formRules.selectRequired', [ctx.label]) */ },
    },
  });
}

const useVbenForm = useForm<ComponentType, ComponentPropsMap>;
export { initSetupVbenForm, useVbenForm, z };
export type VbenFormSchema = FormSchema<ComponentType, ComponentPropsMap>;
```

### 组件适配器结构
```typescript
// adapter/component/index.ts
// 1. 定义 ComponentType（所有可用组件名称的联合类型）
// 2. 定义 ComponentPropsMap（组件名 → Props类型的映射）
// 3. 用 defineAsyncComponent 导入组件
// 4. 用 withDefaultPlaceholder 包装输入组件（自动添加 placeholder）
// 5. 用 globalShareState.setComponents() 注册所有组件
// 6. 导出 initComponentAdapter 在 bootstrap.ts 中调用
```
