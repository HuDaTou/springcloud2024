<script lang="ts" setup>
import { useVbenModal } from '@vben/common-ui';

import { Switch, message } from 'ant-design-vue';

import { useVbenForm } from '#/adapter/form';
import {
  createRoleApi,
  updateRoleApi,
  type RoleApi,
} from '#/api/core/role';

const emit = defineEmits<{
  success: [];
}>();

const [Form, formApi] = useVbenForm({
  showDefaultActions: false,
  schema: [
    {
      component: 'Input',
      fieldName: 'roleName',
      label: '角色名称',
      rules: 'required',
      componentProps: { placeholder: '请输入角色名称' },
    },
    {
      component: 'Input',
      fieldName: 'roleKey',
      label: '角色标识',
      rules: 'required',
      componentProps: { placeholder: '如 ADMIN、USER' },
    },
    {
      component: 'InputNumber',
      fieldName: 'orderNum',
      label: '排序',
      defaultValue: 0,
      rules: 'required',
      componentProps: { min: 0, placeholder: '数字越小越靠前' },
    },
    {
      component: 'Textarea',
      fieldName: 'remark',
      label: '备注',
      componentProps: { placeholder: '请输入备注', rows: 3 },
    },
  ],
});

const [Modal, modalApi] = useVbenModal({
  onConfirm: handleSubmit,
  onOpenChange(isOpen) {
    if (isOpen) {
      formApi.resetForm();
      const data = modalApi.getData<RoleApi.RoleVO>();
      if (data?.id) {
        editingId = data.id;
        formApi.setValues({
          roleName: data.roleName,
          roleKey: data.roleKey,
          orderNum: data.orderNum ?? 0,
          remark: data.remark,
        });
      }
    }
  },
});

let editingId: string | null = null;

async function handleSubmit() {
  const { valid } = await formApi.validate();
  if (!valid) return;

  const data = await formApi.getValues();
  const params: RoleApi.RoleParams = {
    roleName: data.roleName,
    roleKey: data.roleKey,
    status: 0,
    orderNum: data.orderNum ?? 0,
    remark: data.remark || undefined,
  };

  try {
    if (editingId) {
      await updateRoleApi(editingId, params);
      message.success('更新角色成功');
    } else {
      await createRoleApi(params);
      message.success('新增角色成功');
    }
    modalApi.close();
    emit('success');
  } catch {
    // error handled by interceptor
  }
}

defineExpose({
  open(data?: RoleApi.RoleVO) {
    modalApi.setData(data ?? {}).open();
  },
});
</script>

<template>
  <Modal title="角色管理">
    <Form />
  </Modal>
</template>
