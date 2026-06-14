<script lang="ts" setup>
import { ref } from 'vue';

import { useVbenModal } from '@vben/common-ui';
import { $t } from '@vben/locales';

import { message } from 'ant-design-vue';

import { useVbenForm } from '#/adapter/form';
import {
  createAssetApi,
  getAssetDetailApi,
  updateAssetApi,
  type AssetFormParams,
} from '#/api/core/media-asset';
import { useFormSchema } from '../data';

const emit = defineEmits<{ success: [] }>();
const isUpdate = ref(false);
let editingId: string | null = null;

const [Form, formApi] = useVbenForm({
  schema: useFormSchema(),
  commonConfig: { componentProps: { style: { width: '100%' } } },
  showDefaultActions: false,
});

const [Modal, modalApi] = useVbenModal({
  confirmText: $t('common.confirm'),
  destroyOnClose: true,
  onConfirm: handleSubmit,
  async onOpenChange(isOpen) {
    if (isOpen) {
      const data = modalApi.getData<{ id: string } | null>();
      editingId = data?.id ?? null;
      isUpdate.value = !!editingId;
      if (editingId) {
        await loadDetail(editingId);
      } else {
        formApi.resetForm();
      }
    }
  },
});

async function loadDetail(id: string) {
  try {
    const detail = await getAssetDetailApi(id);
    formApi.setValues({
      fileName: detail.fileName,
      objectName: detail.objectName,
      bucketName: detail.bucketName,
      fileType: detail.fileType,
      fileSize: detail.fileSize,
      fileMd5: '',
      uploaderId: detail.uploaderId,
      status: detail.status,
      thumbnailName: detail.thumbnailName ?? '',
      width: detail.width ?? undefined,
      height: detail.height ?? undefined,
      duration: detail.duration ?? undefined,
    });
  } catch {
    // handled by interceptor
  }
}

async function handleSubmit() {
  const { valid } = await formApi.validate();
  if (!valid) return;
  const values = (await formApi.getValues()) as AssetFormParams;
  try {
    if (editingId) {
      await updateAssetApi(editingId, values);
      message.success('更新成功');
    } else {
      await createAssetApi(values);
      message.success('新增成功');
    }
    modalApi.close();
    emit('success');
  } catch {
    // handled by interceptor
  }
}

defineExpose({
  open(data?: { id: string }) {
    modalApi.setData(data ?? {}).open();
  },
});
</script>

<template>
  <Modal :title="isUpdate ? '编辑媒体资产' : '新增媒体资产'">
    <Form />
  </Modal>
</template>
