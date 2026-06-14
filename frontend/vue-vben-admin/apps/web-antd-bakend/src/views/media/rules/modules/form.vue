<script lang="ts" setup>
import { onMounted, ref } from 'vue';

import { useVbenModal } from '@vben/common-ui';
import { $t } from '@vben/locales';

import { message } from 'ant-design-vue';

import { useVbenForm } from '#/adapter/form';
import {
  createRuleApi,
  getRuleDetailApi,
  updateRuleApi,
  type RulesFormParams,
} from '#/api/core/file-upload-rules';
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
      const data = modalApi.getData<{
        id: string;
      } | null>();
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
    const detail = await getRuleDetailApi(id);
    formApi.setValues({
      ruleName: detail.ruleName,
      description: detail.description,
      maxSizeKb: detail.maxSizeKb,
      fileCategory: detail.fileCategory ?? [],
      allowedExtensions: detail.allowedExtensions ?? [],
      storagePath: detail.storagePath ?? '',
      storagePrefix: detail.storagePrefix ?? '',
      isActive: detail.isActive,
    });
  } catch {
    // handled by interceptor
  }
}

async function handleSubmit() {
  const { valid } = await formApi.validate();
  if (!valid) return;
  const values = (await formApi.getValues()) as RulesFormParams;
  try {
    if (editingId) {
      await updateRuleApi(editingId, values);
      message.success('更新成功');
    } else {
      await createRuleApi(values);
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
  <Modal :title="isUpdate ? '编辑上传规则' : '新增上传规则'">
    <Form />
  </Modal>
</template>
