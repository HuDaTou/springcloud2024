<script lang="ts" setup>
import { ref } from 'vue';

import { Page } from '@vben/common-ui';

import { Button, message, Modal, Popconfirm, Space } from 'ant-design-vue';

import { useVbenVxeGrid } from '#/adapter/vxe-table';
import {
  deletePermissionApi,
  getPermissionListApi,
  type PermissionApi,
} from '#/api/core/permission';
import PermissionFormModal from './modules/form.vue';

const [Grid, gridApi] = useVbenVxeGrid({
  formOptions: {
    schema: [
      {
        component: 'Input',
        fieldName: 'name',
        formFieldProps: { label: '权限描述' },
        componentProps: { placeholder: '请输入权限描述' },
      },
      {
        component: 'Input',
        fieldName: 'permissonCode',
        formFieldProps: { label: '权限字符' },
        componentProps: { placeholder: '请输入权限字符' },
      },
    ],
    submitOnChange: true,
  },
  gridOptions: {
    columns: [
      { type: 'checkbox', width: 50 },
      { field: 'id', title: 'ID', width: 120 },
      { field: 'name', title: '权限描述', minWidth: 150 },
      { field: 'permissonCode', title: '权限字符', minWidth: 180 },
      { field: 'category', title: '类别', width: 100 },
      { field: 'httpMethod', title: '方法', width: 70 },
      {
        field: 'action',
        title: '操作',
        width: 160,
        fixed: 'right',
        slots: { default: 'action' },
      },
    ],
    pagerConfig: { pageSize: 20 },
    proxyConfig: {
      ajax: {
        query: async (
          { page }: { page: { currentPage: number; pageSize: number } },
          formValues: Record<string, any> = {},
        ) => {
          const res = await getPermissionListApi();
          let allItems = Array.isArray(res) ? res : [];

          // 前端过滤（cloud-auth 无搜索接口）
          if (formValues.name) {
            const kw = String(formValues.name).toLowerCase();
            allItems = allItems.filter((item) =>
              item.name?.toLowerCase().includes(kw),
            );
          }
          if (formValues.permissonCode) {
            const kw = String(formValues.permissonCode).toLowerCase();
            allItems = allItems.filter((item) =>
              item.permissonCode?.toLowerCase().includes(kw),
            );
          }

          const start = (page.currentPage - 1) * page.pageSize;
          const items = allItems.slice(start, start + page.pageSize);
          return { items, total: allItems.length };
        },
      },
    },
    toolbarConfig: {
      refresh: true,
    },
  },
});

const formModalRef = ref<InstanceType<typeof PermissionFormModal>>();

async function handleDelete(id: string) {
  try {
    await deletePermissionApi(id);
    message.success('删除成功');
    gridApi.query();
  } catch {
    // error handled by interceptor
  }
}

function handleBatchDelete() {
  const records = gridApi?.grid?.getCheckboxRecords() ?? [];
  if (records.length === 0) {
    message.warning('请先选择要删除的权限');
    return;
  }
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除选中的 ${records.length} 个权限吗？`,
    onOk: async () => {
      for (const r of records as PermissionApi.PermissionVO[]) {
        try {
          await deletePermissionApi(r.id);
        } catch {
          // continue with others
        }
      }
      message.success('批量删除完成');
      gridApi.query();
    },
  });
}
</script>

<template>
  <Page auto-content-height>
    <Grid table-title="权限管理">
      <template #toolbar-tools>
        <Button type="primary" @click="formModalRef?.open()">
          新增权限
        </Button>
        <Button danger @click="handleBatchDelete()" style="margin-left: 8px">
          批量删除
        </Button>
      </template>

      <template #action="{ row }">
        <Space>
          <Button
            size="small"
            @click="formModalRef?.open({ id: (row as PermissionApi.PermissionVO).id })"
          >
            编辑
          </Button>
          <Popconfirm title="确定要删除该权限吗？" @confirm="handleDelete((row as PermissionApi.PermissionVO).id)">
            <Button size="small" danger>删除</Button>
          </Popconfirm>
        </Space>
      </template>
    </Grid>
  </Page>
  <PermissionFormModal ref="formModalRef" @success="gridApi.query()" />
</template>
