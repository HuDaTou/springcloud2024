<script lang="ts" setup>
import { ref } from 'vue';

import { Page } from '@vben/common-ui';

import { Button, message, Modal, Popconfirm, Space, Tag } from 'ant-design-vue';

import { useVbenVxeGrid } from '#/adapter/vxe-table';
import {
  deleteRoleApi,
  getRoleListApi,
  type RoleApi,
} from '#/api/core/role';
import RoleFormModal from './modules/form.vue';
import RolePermModal from './modules/permission.vue';

const [Grid, gridApi] = useVbenVxeGrid({
  gridOptions: {
    columns: [
      { type: 'checkbox', width: 50 },
      { field: 'id', title: 'ID', width: 70, sortable: true },
      { field: 'roleName', title: '角色名称', minWidth: 120 },
      { field: 'roleKey', title: '角色标识', minWidth: 140 },
      {
        field: 'status',
        title: '状态',
        width: 80,
        slots: { default: 'status' },
      },
      { field: 'orderNum', title: '排序', width: 70 },
      { field: 'remark', title: '备注', minWidth: 140 },
      { field: 'createTime', title: '创建时间', width: 170 },
      {
        field: 'action',
        title: '操作',
        width: 240,
        fixed: 'right',
        slots: { default: 'action' },
      },
    ],
    pagerConfig: { pageSize: 20 },
    proxyConfig: {
      ajax: {
        query: async ({
          page,
        }: {
          page: { currentPage: number; pageSize: number };
        }) => {
          const res = await getRoleListApi();
          const allItems = Array.isArray(res) ? res : [];
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

const formModalRef = ref<InstanceType<typeof RoleFormModal>>();
const permModalRef = ref<InstanceType<typeof RolePermModal>>();

async function handleDelete(id: string) {
  try {
    await deleteRoleApi(id);
    message.success('删除成功');
    gridApi.query();
  } catch {
    // error handled by interceptor
  }
}

function handleBatchDelete() {
  const records = gridApi?.grid?.getCheckboxRecords() ?? [];
  if (records.length === 0) {
    message.warning('请先选择要删除的角色');
    return;
  }
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除选中的 ${records.length} 个角色吗？`,
    onOk: async () => {
      for (const r of records as RoleApi.RoleVO[]) {
        try {
          await deleteRoleApi(r.id);
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
    <Grid table-title="角色管理">
      <template #toolbar-tools>
        <Button type="primary" @click="formModalRef?.open()">
          新增角色
        </Button>
        <Button danger @click="handleBatchDelete()" style="margin-left: 8px">
          批量删除
        </Button>
      </template>

      <template #status="{ row }">
        <Tag :color="row.status === 0 ? 'green' : 'red'">
          {{ row.status === 0 ? '正常' : '禁用' }}
        </Tag>
      </template>

      <template #action="{ row }">
        <Space>
          <Button
            size="small"
            type="link"
            @click="permModalRef?.open({ roleId: (row as RoleApi.RoleVO).id })"
          >
            分配权限
          </Button>
          <Button
            size="small"
            @click="formModalRef?.open(row as RoleApi.RoleVO)"
          >
            编辑
          </Button>
          <Popconfirm
            title="确定要删除该角色吗？"
            @confirm="handleDelete((row as RoleApi.RoleVO).id)"
          >
            <Button size="small" danger>删除</Button>
          </Popconfirm>
        </Space>
      </template>
    </Grid>
  </Page>
  <RoleFormModal ref="formModalRef" @success="gridApi.query()" />
  <RolePermModal ref="permModalRef" />
</template>
