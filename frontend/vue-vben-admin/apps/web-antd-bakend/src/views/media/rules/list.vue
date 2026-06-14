<script lang="ts" setup>
import { ref } from 'vue';

import { Page } from '@vben/common-ui';

import { Button, message, Modal, Popconfirm, Space, Tag } from 'ant-design-vue';

import { useVbenVxeGrid } from '#/adapter/vxe-table';
import {
  deleteRuleApi,
  getRulesListApi,
  type RulesVO,
} from '#/api/core/file-upload-rules';
import { useColumns, useGridFormSchema } from './data';
import RulesFormModal from './modules/form.vue';

const [Grid, gridApi] = useVbenVxeGrid({
  formOptions: {
    schema: useGridFormSchema(),
    submitOnChange: true,
  },
  gridOptions: {
    columns: useColumns(),
    pagerConfig: { pageSize: 10 },
    proxyConfig: {
      ajax: {
        query: async (
          { page }: { page: { currentPage: number; pageSize: number } },
          formValues: Record<string, any> = {},
        ) => {
          // 全量获取后前端过滤与分页（后端无搜索参数）
          const res = await getRulesListApi(1, 9999);
          let allItems = res.records ?? [];

          if (formValues.ruleName) {
            const kw = String(formValues.ruleName).toLowerCase();
            allItems = allItems.filter((item) =>
              item.ruleName?.toLowerCase().includes(kw),
            );
          }
          if (formValues.isActive !== undefined && formValues.isActive !== null) {
            allItems = allItems.filter(
              (item) => item.isActive === formValues.isActive,
            );
          }

          const start = (page.currentPage - 1) * page.pageSize;
          const items = allItems.slice(start, start + page.pageSize);
          return { items, total: allItems.length };
        },
      },
    },
    toolbarConfig: { refresh: true },
  },
});

const formModalRef = ref<InstanceType<typeof RulesFormModal>>();

async function handleDelete(id: string) {
  try {
    await deleteRuleApi(id);
    message.success('删除成功');
    gridApi.query();
  } catch {
    // handled by interceptor
  }
}

function handleBatchDelete() {
  const records = gridApi?.grid?.getCheckboxRecords() ?? [];
  if (records.length === 0) {
    message.warning('请先选择要删除的规则');
    return;
  }
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除选中的 ${records.length} 条规则吗？`,
    onOk: async () => {
      for (const r of records as RulesVO[]) {
        try {
          await deleteRuleApi(r.id);
        } catch {
          // continue
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
    <Grid table-title="文件上传规则管理">
      <template #toolbar-tools>
        <Button type="primary" @click="formModalRef?.open()">
          新增规则
        </Button>
        <Button danger style="margin-left: 8px" @click="handleBatchDelete()">
          批量删除
        </Button>
      </template>

      <template #isActive="{ row }">
        <Tag :color="(row as RulesVO).isActive ? 'green' : 'red'">
          {{ (row as RulesVO).isActive ? '启用' : '禁用' }}
        </Tag>
      </template>

      <template #action="{ row }">
        <Space>
          <Button
            size="small"
            @click="formModalRef?.open({ id: (row as RulesVO).id })"
          >
            编辑
          </Button>
          <Popconfirm
            title="确定要删除该规则吗？"
            @confirm="handleDelete((row as RulesVO).id)"
          >
            <Button size="small" danger>删除</Button>
          </Popconfirm>
        </Space>
      </template>
    </Grid>
  </Page>
  <RulesFormModal ref="formModalRef" @success="gridApi.query()" />
</template>
