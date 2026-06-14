<script lang="ts" setup>
import { useVbenModal } from '@vben/common-ui';

import { message } from 'ant-design-vue';

import { useVbenForm } from '#/adapter/form';
import { adminCreateUserApi } from '#/api/core/user';

const emit = defineEmits<{
  success: [];
}>();

const [Form, formApi] = useVbenForm({
  showDefaultActions: false,
  schema: [
    {
      component: 'Input',
      fieldName: 'username',
      label: '用户名',
      rules: 'required',
      componentProps: { placeholder: '请输入用户名' },
    },
    {
      component: 'InputPassword',
      fieldName: 'password',
      label: '密码',
      rules: 'required',
      componentProps: { placeholder: '请输入密码' },
    },
    {
      component: 'Input',
      fieldName: 'nickname',
      label: '昵称',
      componentProps: { placeholder: '请输入昵称' },
    },
    {
      component: 'Input',
      fieldName: 'email',
      label: '邮箱',
      componentProps: { placeholder: '请输入邮箱' },
    },
    {
      component: 'Switch',
      fieldName: 'isDisable',
      label: '是否禁用',
      defaultValue: false,
    },
  ],
});

const [Modal, modalApi] = useVbenModal({
  onConfirm: handleSubmit,
  onOpenChange(isOpen) {
    if (isOpen) {
      formApi.resetForm();
    }
  },
});

async function handleSubmit() {
  const { valid } = await formApi.validate();
  if (!valid) return;

  const data = await formApi.getValues();
  try {
    await adminCreateUserApi({
      username: data.username,
      password: data.password,
      nickname: data.nickname || undefined,
      email: data.email || undefined,
      isDisable: data.isDisable ?? false,
    });
    message.success('创建用户成功');
    modalApi.close();
    emit('success');
  } catch {
    // error handled by interceptor
  }
}

defineExpose({
  open() {
    modalApi.open();
  },
});
</script>

<template>
  <Modal title="新增用户">
    <Form />
  </Modal>
</template>
