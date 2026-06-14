<script lang="ts" setup>
import { onMounted, ref } from 'vue';

import { useVbenModal } from '@vben/common-ui';

import { message } from 'ant-design-vue';

import { useVbenForm } from '#/adapter/form';
import {
  addPermissionApi,
  getPermissionDetailApi,
  getPermissionMenusApi,
  updatePermissionApi,
} from '#/api/core/permission';

const emit = defineEmits<{
  success: [];
}>();

const menuOptions = ref<{ label: string; value: string }[]>([]);

onMounted(async () => {
  try {
    const menus = await getPermissionMenusApi();
    menuOptions.value = (menus || []).map((m) => ({
      label: m.title,
      value: m.id,
    }));
  } catch {
    // handled by interceptor
  }
});

const [Form, formApi] = useVbenForm({
  showDefaultActions: false,
  schema: [
    {
      component: 'Input',
      fieldName: 'name',
      label: '权限描述',
      rules: 'required',
      componentProps: { placeholder: '如 查询菜单列表' },
    },
    {
      component: 'Input',
      fieldName: 'permissonCode',
      label: '权限字符',
      rules: 'required',
      componentProps: { placeholder: '如 system:menu:list' },
    },
    {
      component: 'Select',
      fieldName: 'menuId',
      label: '所属菜单',
      componentProps: {
        placeholder: '请选择所属菜单',
        options: menuOptions,
        showSearch: true,
        allowClear: true,
        filterOption: (input: string, option: any) =>
          option?.label?.toLowerCase().includes(input.toLowerCase()),
      },
    },
    {
      component: 'Input',
      fieldName: 'category',
      label: '权限类别',
      componentProps: { placeholder: '如 用户管理' },
    },
    {
      component: 'Input',
      fieldName: 'httpMethod',
      label: 'HTTP 方法',
      componentProps: { placeholder: 'GET / POST / PUT / DELETE' },
    },
    {
      component: 'Input',
      fieldName: 'path',
      label: '请求路径',
      componentProps: { placeholder: '如 /api/menu/list' },
    },
    {
      component: 'Input',
      fieldName: 'serviceName',
      label: '所属服务',
      componentProps: { placeholder: '如 cloud-auth' },
    },
  ],
});

let editingId: string | null = null;

const [Modal, modalApi] = useVbenModal({
  onConfirm: handleSubmit,
  async onOpenChange(isOpen) {
    if (isOpen) {
      formApi.resetForm();
      editingId = null;
      const data = modalApi.getData<{ id?: string }>();
      if (data?.id) {
        editingId = data.id;
        try {
          const detail = await getPermissionDetailApi(data.id);
          await formApi.setValues({
            name: detail.name,
            permissonCode: detail.permissonCode,
            menuId: detail.menuId ?? undefined,
            category: detail.category ?? undefined,
            httpMethod: detail.httpMethod ?? undefined,
            path: detail.path ?? undefined,
            serviceName: detail.serviceName ?? undefined,
          });
        } catch {
          message.error('获取权限信息失败');
        }
      }
    }
  },
});

async function handleSubmit() {
  const { valid } = await formApi.validate();
  if (!valid) return;

  const data = await formApi.getValues();
  try {
    if (editingId) {
      await updatePermissionApi(editingId, {
        name: data.name,
        permissonCode: data.permissonCode,
        menuId: data.menuId || null,
        category: data.category || undefined,
        httpMethod: data.httpMethod || undefined,
        path: data.path || undefined,
        serviceName: data.serviceName || undefined,
      });
      message.success('更新权限成功');
    } else {
      await addPermissionApi({
        name: data.name,
        permissonCode: data.permissonCode,
        menuId: data.menuId || null,
        category: data.category || undefined,
        httpMethod: data.httpMethod || undefined,
        path: data.path || undefined,
        serviceName: data.serviceName || undefined,
      });
      message.success('新增权限成功');
    }
    modalApi.close();
    emit('success');
  } catch {
    // error handled by interceptor
  }
}

defineExpose({
  open(data?: { id?: string }) {
    modalApi.setData(data ?? {}).open();
  },
});
</script>

<template>
  <Modal title="权限管理">
    <Form />
  </Modal>
</template>
