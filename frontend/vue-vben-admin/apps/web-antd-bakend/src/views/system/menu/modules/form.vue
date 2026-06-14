<script lang="ts" setup>
import { ref } from 'vue';

import { useVbenDrawer } from '@vben/common-ui';

import { message } from 'ant-design-vue';

import { useVbenForm } from '#/adapter/form';
import { createMenuApi, getMenuListApi, updateMenuApi } from '#/api/core/menu';
import type { MenuApi } from '#/api/core/menu';

const emit = defineEmits<{
  success: [];
}>();

const parentMenuTreeData = ref<MenuApi.MenuListVO[]>([]);

let editingId: string | null = null;

const [Form, formApi] = useVbenForm({
  showDefaultActions: false,
  schema: [
    {
      component: 'TreeSelect',
      fieldName: 'parentId',
      label: '父级菜单',
      componentProps: {
        placeholder: '请选择父级菜单（留空为顶级菜单）',
        treeDefaultExpandAll: true,
        allowClear: true,
        treeData: parentMenuTreeData,
        fieldNames: {
          label: 'title',
          value: 'id',
          children: 'children',
        },
      },
    },
    {
      component: 'Input',
      fieldName: 'title',
      label: '菜单名称',
      rules: 'required',
      componentProps: { placeholder: '请输入菜单名称' },
    },
    {
      component: 'Input',
      fieldName: 'name',
      label: '路由名称',
      componentProps: { placeholder: '请输入路由名称' },
    },
    {
      component: 'Input',
      fieldName: 'path',
      label: '路由路径',
      componentProps: { placeholder: '请输入路由路径，如 /system' },
    },
    {
      component: 'Input',
      fieldName: 'component',
      label: '组件路径',
      componentProps: { placeholder: '请输入组件路径，如 system/menu/list' },
    },
    {
      component: 'Input',
      fieldName: 'icon',
      label: '图标',
      componentProps: { placeholder: '请输入图标名称' },
    },
    {
      component: 'InputNumber',
      fieldName: 'orderNum',
      label: '排序号',
      defaultValue: 0,
      componentProps: { placeholder: '数字越小越靠前', min: 0, style: { width: '100%' } },
    },
    {
      component: 'Input',
      fieldName: 'redirect',
      label: '重定向',
      componentProps: { placeholder: '请输入重定向路径' },
    },
    {
      component: 'Input',
      fieldName: 'url',
      label: '外部链接',
      componentProps: { placeholder: '请输入外部链接地址' },
    },
    {
      component: 'Select',
      fieldName: 'target',
      label: '打开方式',
      componentProps: {
        options: [
          { label: '当前窗口', value: '_self' },
          { label: '新窗口', value: '_blank' },
        ],
        placeholder: '请选择打开方式',
        allowClear: true,
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'hideInMenu',
      label: '隐藏菜单',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '显示', value: 0 },
          { label: '隐藏', value: 1 },
        ],
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'hideInBreadcrumb',
      label: '隐藏面包屑',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '显示', value: 0 },
          { label: '隐藏', value: 1 },
        ],
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'hideChildrenInMenu',
      label: '隐藏子菜单',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '显示', value: 0 },
          { label: '隐藏', value: 1 },
        ],
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'keepAlive',
      label: '路由缓存',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '关闭', value: 0 },
          { label: '开启', value: 1 },
        ],
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'affix',
      label: '固定标签',
      defaultValue: 0,
      componentProps: {
        options: [
          { label: '不固定', value: 0 },
          { label: '固定', value: 1 },
        ],
      },
    },
    {
      component: 'RadioGroup',
      fieldName: 'isDisable',
      label: '状态',
      defaultValue: false,
      componentProps: {
        options: [
          { label: '正常', value: false },
          { label: '禁用', value: true },
        ],
      },
    },
  ],
});

const [Drawer, drawerApi] = useVbenDrawer({
  onConfirm: handleSubmit,
  onOpenChange(isOpen) {
    if (isOpen) {
      const data = drawerApi.getData() as
        | MenuApi.MenuListVO
        | { parentId: string }
        | undefined;
      editingId = null;
      formApi.resetForm();
      loadParentMenuTree();
      if (data) {
        if ('id' in data && data.id) {
          editingId = data.id;
          formApi.setValues({
            parentId: data.parentId ?? undefined,
            title: data.title,
            name: data.name || '',
            path: data.path || '',
            component: data.component || '',
            icon: data.icon || '',
            orderNum: data.orderNum ?? 0,
            redirect: data.redirect || '',
            url: data.url || '',
            target: data.target || undefined,
            hideInMenu: data.hideInMenu ?? 0,
            hideInBreadcrumb: data.hideInBreadcrumb ?? 0,
            hideChildrenInMenu: data.hideChildrenInMenu ?? 0,
            keepAlive: data.keepAlive ?? 0,
            affix: data.affix ?? 0,
            isDisable: data.isDisable ?? false,
          });
        } else if ('parentId' in data) {
          formApi.setValues({
            parentId: data.parentId ?? undefined,
          });
        }
      }
    }
  },
});

async function loadParentMenuTree() {
  try {
    const res = await getMenuListApi();
    parentMenuTreeData.value = Array.isArray(res) ? res : [];
  } catch {
    parentMenuTreeData.value = [];
  }
}

async function handleSubmit() {
  const { valid } = await formApi.validate();
  if (!valid) return;

  const data = await formApi.getValues();
  const parentId: string | null | undefined = data.parentId
    ? String(data.parentId)
    : undefined;

  try {
    if (editingId) {
      await updateMenuApi(editingId, {
        title: data.title,
        icon: data.icon || undefined,
        path: data.path || undefined,
        component: data.component || undefined,
        redirect: data.redirect || undefined,
        affix: data.affix ?? 0,
        parentId,
        name: data.name || undefined,
        hideInMenu: data.hideInMenu ?? 0,
        url: data.url || undefined,
        hideInBreadcrumb: data.hideInBreadcrumb ?? 0,
        hideChildrenInMenu: data.hideChildrenInMenu ?? 0,
        keepAlive: data.keepAlive ?? 0,
        target: data.target || undefined,
        isDisable: data.isDisable ?? false,
        orderNum: data.orderNum ?? 0,
      });
      message.success('更新菜单成功');
    } else {
      await createMenuApi({
        title: data.title,
        icon: data.icon || undefined,
        path: data.path || undefined,
        component: data.component || undefined,
        redirect: data.redirect || undefined,
        affix: data.affix ?? 0,
        parentId,
        name: data.name || undefined,
        hideInMenu: data.hideInMenu ?? 0,
        url: data.url || undefined,
        hideInBreadcrumb: data.hideInBreadcrumb ?? 0,
        hideChildrenInMenu: data.hideChildrenInMenu ?? 0,
        keepAlive: data.keepAlive ?? 0,
        target: data.target || undefined,
        isDisable: data.isDisable ?? false,
        orderNum: data.orderNum ?? 0,
      });
      message.success('创建菜单成功');
    }
    drawerApi.close();
    emit('success');
  } catch {
    // error handled by interceptor
  }
}
</script>

<template>
  <Drawer :title="editingId ? '编辑菜单' : '新增菜单'">
    <Form />
  </Drawer>
</template>
