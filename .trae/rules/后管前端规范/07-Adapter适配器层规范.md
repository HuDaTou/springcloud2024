---
alwaysApply: false
globs: frontend/vue-vben-admin/**/adapter/**/*.ts
---
# 后管前端Adapter适配器层规范

> **适用范围**：`frontend/vue-vben-admin/` 目录下所有 `adapter/**/*.ts` 文件

## 一、强制要求（AI必须100%遵守，违反即错误）

1. **必须使用TypeScript**：所有适配器必须使用TypeScript编写
2. **必须封装第三方组件**：必须对第三方组件进行封装
3. **必须提供统一接口**：适配器必须提供统一的API接口

## 二、推荐做法（AI优先采用，效果更好）

1. **使用组合式函数**：使用useXXX形式的组合式函数封装组件
2. **使用泛型**：为适配器添加泛型支持
3. **提供默认配置**：为适配器提供合理的默认配置

## 三、禁止行为（AI绝对不能生成）

1. **禁止直接使用第三方组件**：禁止在业务组件中直接使用第三方组件
2. **禁止硬编码配置**：禁止在适配器中硬编码配置

## 四、目录结构

```
src/adapter/
├── index.ts                # 适配器统一导出
├── vxe-table/              # VXE Table适配器
├── form/                   # 表单适配器
└── component/              # 组件适配器
```

## 五、标准代码示例

### VXE Table适配器
```typescript
import { useVxeGrid } from '@vben/adapter-vxe-table';

export function useVbenVxeGrid<T = any>(options: GridOptions<T>) {
  const { Grid, api } = useVxeGrid({
    ...options,
    gridOptions: {
      border: true,
      showHeader: true,
      ...options.gridOptions,
    },
  });

  return { Grid, api };
}

interface GridOptions<T> {
  gridOptions?: Partial<VxeGridOptions<T>>;
}
```

### 表单适配器
```typescript
import { useForm } from '@vben/adapter-form';

export function useVbenForm<T = any>(options: FormOptions<T>) {
  const { Form, api } = useForm({
    ...options,
    formOptions: {
      labelWidth: '120px',
      layout: 'horizontal',
      ...options.formOptions,
    },
  });

  return { Form, api };
}

interface FormOptions<T> {
  schema: FormSchema[];
  formOptions?: Partial<FormConfig>;
}
```

### 组件适配器
```typescript
import { Button } from 'antdv-next';

export function VbenButton(props: ButtonProps) {
  return h(Button, {
    type: props.type || 'default',
    size: props.size || 'middle',
    ...props,
  });
}

interface ButtonProps {
  type?: 'primary' | 'default' | 'danger';
  size?: 'small' | 'middle' | 'large';
  onClick?: () => void;
}
```

### 适配器统一导出
```typescript
export { useVbenVxeGrid } from './vxe-table';
export { useVbenForm } from './form';
export { VbenButton } from './component';
```