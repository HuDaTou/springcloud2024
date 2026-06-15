<script lang="ts" setup>
import type {
  OnActionClickParams,
  VxeTableGridOptions,
} from '#/adapter/vxe-table';
import type { MenuApi } from '#/api/core/menu';

import { Page, useVbenDrawer } from '@vben/common-ui';
import { Plus } from '@vben/icons';

import { Button, message, Modal, Tag } from 'ant-design-vue';

import { useVbenVxeGrid } from '#/adapter/vxe-table';
import { deleteMenuApi, getMenuListApi } from '#/api/core/menu';

import { useColumns, useGridFormSchema } from './data';
import Form from './modules/form.vue';

const [FormDrawer, formDrawerApi] = useVbenDrawer({
  connectedComponent: Form,
  destroyOnClose: true,
});

const [Grid, gridApi] = useVbenVxeGrid({
  formOptions: {
    schema: useGridFormSchema(),
    submitOnChange: true,
  },
  gridOptions: {
    columns: useColumns(onActionClick),
    height: 'auto',
    keepSource: true,
    pagerConfig: {
      enabled: false,
    },
    proxyConfig: {
      ajax: {
        query: async (_params: any, formValues: Record<string, any> = {}) => {
          const res = await getMenuListApi();
          const allItems = (Array.isArray(res) ? res : []) as MenuApi.MenuListVO[];

          // 前端过滤
          let filtered = allItems;
          if (formValues.title) {
            const keyword = String(formValues.title).toLowerCase();
            filtered = filtered.filter((item) =>
              item.title?.toLowerCase().includes(keyword),
            );
          }
          if (
            formValues.isDisable !== undefined &&
            formValues.isDisable !== null &&
            formValues.isDisable !== ''
          ) {
            filtered = filtered.filter(
              (item) => item.isDisable === (formValues.isDisable === 1),
            );
          }

          // 构建树结构
          const tree = buildTree(filtered);
          return { items: tree, total: tree.length };
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
      zoom: true,
    },
    treeConfig: {
      parentField: 'parentId',
      rowField: 'id',
      transform: false,
    },
  } as VxeTableGridOptions<MenuApi.MenuListVO>,
});

/**
 * 将扁平数组构建为树形结构
 */
function buildTree(items: MenuApi.MenuListVO[]): MenuApi.MenuListVO[] {
  const map = new Map<string | null | undefined, MenuApi.MenuListVO>();
  const roots: MenuApi.MenuListVO[] = [];

  for (const item of items) {
    map.set(item.id, { ...item, children: [] });
  }

  for (const item of items) {
    const node = map.get(item.id)!;
    const parentId = item.parentId;
    if (parentId !== null && parentId !== undefined && parentId !== '' && map.has(parentId)) {
      const parent = map.get(parentId)!;
      if (!parent.children) parent.children = [];
      parent.children.push(node);
    } else {
      roots.push(node);
    }
  }

  return roots;
}

/**
 * 操作列点击回调
 */
function onActionClick({ code, row }: OnActionClickParams<MenuApi.MenuListVO>) {
  switch (code) {
    case 'append': {
      onAppend(row);
      break;
    }
    case 'edit': {
      onEdit(row);
      break;
    }
    case 'delete': {
      onDelete(row);
      break;
    }
  }
}

/** 刷新列表 */
function onRefresh() {
  gridApi.query();
}

/** 新增顶级菜单 */
function onCreate() {
  formDrawerApi.setData({}).open();
}

/** 编辑菜单 */
function onEdit(row: MenuApi.MenuListVO) {
  formDrawerApi.setData(row).open();
}

/** 添加子菜单 */
function onAppend(row: MenuApi.MenuListVO) {
  formDrawerApi.setData({ parentId: row.id } as any).open();
}

/** 删除菜单 */
async function onDelete(row: MenuApi.MenuListVO) {
  try {
    await deleteMenuApi(row.id);
    message.success('删除成功');
    onRefresh();
  } catch {
    // error handled by interceptor
  }
}

/** 批量删除 */
function onBatchDelete() {
  const records = gridApi?.grid?.getCheckboxRecords() ?? [];
  if (records.length === 0) {
    message.warning('请先选择要删除的菜单');
    return;
  }
  Modal.confirm({
    content: `确定要删除选中的 ${records.length} 个菜单吗？（删除父菜单会同时删除子菜单）`,
    title: '确认删除',
    onOk: async () => {
      try {
        for (const record of records) {
          await deleteMenuApi((record as MenuApi.MenuListVO).id);
        }
        message.success('批量删除完成');
        onRefresh();
      } catch {
        // error handled by interceptor
      }
    },
  });
}
</script>

<template>
  <Page auto-content-height>
    <FormDrawer @success="onRefresh" />
    <Grid table-title="菜单管理">
      <template #toolbar-tools>
        <Button type="primary" @click="onCreate">
          <Plus class="size-5" />
          新增菜单
        </Button>
        <Button class="ml-2" danger @click="onBatchDelete">
          批量删除
        </Button>
      </template>

      <template #isDisable="{ row }">
        <Tag :color="row.isDisable ? 'red' : 'green'">
          {{ row.isDisable ? '禁用' : '正常' }}
        </Tag>
      </template>
    </Grid>
  </Page>
</template>
