<script lang="ts" setup>
import { ref } from 'vue';

import { Page } from '@vben/common-ui';

import { Button, message, Modal, Popconfirm, Space, Tag } from 'ant-design-vue';

import { useVbenVxeGrid } from '#/adapter/vxe-table';
import {
  deleteUserApi,
  getUserListApi,
  searchUserApi,
  updateUserStatusApi,
} from '#/api/core/user';
import type { UserApi } from '#/api/core/user';
import UserFormModal from './modules/form.vue';

const [Grid, gridApi] = useVbenVxeGrid({
  formOptions: {
    schema: [
      {
        component: 'Input',
        fieldName: 'username',
        formFieldProps: { label: '用户名' },
        componentProps: { placeholder: '请输入用户名' },
      },
      {
        component: 'Input',
        fieldName: 'email',
        formFieldProps: { label: '邮箱' },
        componentProps: { placeholder: '请输入邮箱' },
      },
      {
        component: 'Select',
        fieldName: 'isDisable',
        formFieldProps: { label: '状态' },
        componentProps: {
          options: [
            { label: '全部', value: undefined },
            { label: '正常', value: 0 },
            { label: '禁用', value: 1 },
          ],
          placeholder: '选择状态',
        },
      },
    ],
  },
  gridOptions: {
    columns: [
      { type: 'checkbox', width: 50 },
      { field: 'id', title: 'ID', width: 70, sortable: true },
      {
        field: 'avatar',
        title: '头像',
        width: 70,
        cellRender: { name: 'CellImage', props: { width: 32 } },
      },
      { field: 'username', title: '用户名', minWidth: 120 },
      { field: 'email', title: '邮箱', minWidth: 160 },
      {
        field: 'registerType',
        title: '注册方式',
        width: 100,
        slots: { default: 'registerType' },
      },
      {
        field: 'isDisable',
        title: '状态',
        width: 80,
        slots: { default: 'isDisable' },
      },
      {
        field: 'createTime',
        title: '创建时间',
        width: 170,
      },
      {
        field: 'action',
        title: '操作',
        width: 220,
        fixed: 'right',
        slots: { default: 'action' },
      },
    ],
    pagerConfig: { pageSize: 20 },
    proxyConfig: {
      ajax: {
        query: async ({ page }: { page: { currentPage: number; pageSize: number } }, formValues: Record<string, any> = {}) => {
          const params: UserApi.UserSearchParams = {};
          if (formValues.username) params.username = formValues.username;
          if (formValues.email) params.email = formValues.email;
          if (formValues.isDisable !== undefined && formValues.isDisable !== null)
            params.isDisable = formValues.isDisable;

          const hasSearchParams =
            params.username || params.email || params.isDisable !== undefined;
          const res = hasSearchParams
            ? await searchUserApi(params)
            : await getUserListApi();

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

const formModalRef = ref<InstanceType<typeof UserFormModal>>();

function getRegisterTypeLabel(type: number) {
  const map: Record<number, string> = {
    1: '邮箱',
    2: 'Gitee',
    3: 'GitHub',
  };
  return map[type] ?? '未知';
}

async function handleDelete(ids: number[]) {
  try {
    await deleteUserApi({ Ids: ids });
    message.success('删除成功');
    gridApi.query();
  } catch {
    // error handled by interceptor
  }
}

function handleBatchDelete() {
  const records = gridApi?.grid?.getCheckboxRecords() ?? [];
  if (records.length === 0) {
    message.warning('请先选择要删除的用户');
    return;
  }
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除选中的 ${records.length} 个用户吗？`,
    onOk: () => handleDelete(records.map((r: any) => r.id)),
  });
}

async function handleStatusChange(record: UserApi.UserListVO) {
  const newStatus = record.isDisable ? 0 : 1;
  const action = newStatus === 1 ? '禁用' : '启用';
  Modal.confirm({
    title: `确认${action}`,
    content: `确定要${action}用户「${record.username}」吗？`,
    onOk: async () => {
      try {
        await updateUserStatusApi({ id: record.id, status: newStatus });
        message.success(`${action}成功`);
        gridApi.query();
      } catch {
        // error handled by interceptor
      }
    },
  });
}

</script>

<template>
  <Page auto-content-height>
    <Grid table-title="用户管理">
      <template #toolbar-tools>
        <Button type="primary" @click="formModalRef?.open()">
          新增用户
        </Button>
        <Button danger @click="handleBatchDelete()" style="margin-left: 8px">
          批量删除
        </Button>
      </template>

      <template #registerType="{ row }">
        <Tag>{{ getRegisterTypeLabel(row.registerType) }}</Tag>
      </template>

      <template #isDisable="{ row }">
        <Tag :color="row.isDisable ? 'red' : 'green'">
          {{ row.isDisable ? '禁用' : '正常' }}
        </Tag>
      </template>

      <template #action="{ row }">
        <Space>
          <Button size="small" @click="handleStatusChange(row as UserApi.UserListVO)">
            {{ row.isDisable ? '启用' : '禁用' }}
          </Button>
          <Popconfirm
            title="确定要删除该用户吗？"
            @confirm="handleDelete([row.id])"
          >
            <Button size="small" danger>删除</Button>
          </Popconfirm>
        </Space>
      </template>
    </Grid>
  </Page>
  <UserFormModal ref="formModalRef" @success="gridApi.query()" />
</template>
