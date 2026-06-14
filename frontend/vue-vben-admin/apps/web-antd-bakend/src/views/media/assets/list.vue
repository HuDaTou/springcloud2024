<script lang="ts" setup>
import { ref } from 'vue';

import { Page } from '@vben/common-ui';

import { Button, message, Modal, Popconfirm, Space, Tag } from 'ant-design-vue';

import { useVbenVxeGrid } from '#/adapter/vxe-table';
import {
  batchDeleteAssetApi,
  deleteAssetApi,
  getAssetListApi,
  type AssetVO,
} from '#/api/core/media-asset';
import { useColumns, useGridFormSchema } from './data';
import AssetFormModal from './modules/form.vue';

/** 状态颜色映射 */
const statusColorMap: Record<string, string> = {
  FAILED: 'red',
  PROCESSED: 'cyan',
  PROCESSING: 'orange',
  UPLOADED: 'green',
  UPLOADING: 'blue',
};

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
          const res = await getAssetListApi(1, 9999);
          let allItems = res.records ?? [];

          if (formValues.fileName) {
            const kw = String(formValues.fileName).toLowerCase();
            allItems = allItems.filter((item) =>
              item.fileName?.toLowerCase().includes(kw),
            );
          }
          if (formValues.status) {
            allItems = allItems.filter(
              (item) => item.status === formValues.status,
            );
          }
          if (formValues.fileType) {
            const kw = String(formValues.fileType).toLowerCase();
            allItems = allItems.filter((item) =>
              item.fileType?.toLowerCase().includes(kw),
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

const formModalRef = ref<InstanceType<typeof AssetFormModal>>();

async function handleDelete(id: string) {
  try {
    await deleteAssetApi(id);
    message.success('删除成功');
    gridApi.query();
  } catch {
    // handled by interceptor
  }
}

async function handleBatchDelete() {
  const records = gridApi?.grid?.getCheckboxRecords() ?? [];
  if (records.length === 0) {
    message.warning('请先选择要删除的媒体资产');
    return;
  }
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除选中的 ${records.length} 个媒体资产吗？`,
    onOk: async () => {
      try {
        await batchDeleteAssetApi(
          (records as AssetVO[]).map((r) => r.id),
        );
        message.success('批量删除成功');
        gridApi.query();
      } catch {
        // handled by interceptor
      }
    },
  });
}
</script>

<template>
  <Page auto-content-height>
    <Grid table-title="媒体资产管理">
      <template #toolbar-tools>
        <Button type="primary" @click="formModalRef?.open()">
          新增资产
        </Button>
        <Button danger style="margin-left: 8px" @click="handleBatchDelete()">
          批量删除
        </Button>
      </template>

      <template #status="{ row }">
        <Tag :color="statusColorMap[(row as AssetVO).status] ?? 'default'">
          {{ (row as AssetVO).statusDesc || (row as AssetVO).status }}
        </Tag>
      </template>

      <template #action="{ row }">
        <Space>
          <Button
            size="small"
            @click="formModalRef?.open({ id: (row as AssetVO).id })"
          >
            编辑
          </Button>
          <Popconfirm
            title="确定要删除该资产吗？"
            @confirm="handleDelete((row as AssetVO).id)"
          >
            <Button size="small" danger>删除</Button>
          </Popconfirm>
        </Space>
      </template>
    </Grid>
  </Page>
  <AssetFormModal ref="formModalRef" @success="gridApi.query()" />
</template>
