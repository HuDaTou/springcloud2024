<script lang="ts" setup>
import { ref } from 'vue';

import { useVbenModal } from '@vben/common-ui';

import { message, Tree } from 'ant-design-vue';

import {
  assignRolePermissionsApi,
  getRolePermissionIdsApi,
} from '#/api/core/role';
import {
  getPermissionTreeApi,
  type PermissionTreeNode,
} from '#/api/core/permission';

type TreeNode = {
  key: string;
  title: string;
  children?: TreeNode[];
};

const emit = defineEmits<{ success: [] }>();

const treeData = ref<PermissionTreeNode[]>([]);
const checkedKeys = ref<string[]>([]);
const expandedKeys = ref<string[]>([]);
const loading = ref(false);
let roleId: string | null = null;

function toTreeNodes(nodes: PermissionTreeNode[]): TreeNode[] {
  const result: TreeNode[] = [];
  for (const node of nodes) {
    const hasChildren = node.children && node.children.length > 0;
    if (hasChildren) {
      const childNodes: TreeNode[] = node.children!.map(function (child) {
        const childKey = child.id != null ? String(child.id) : child.value;
        return { title: `${child.label} (${child.value})`, key: childKey };
      });
      result.push({
        key: `cat_${node.value}`,
        title: node.label,
        children: childNodes,
      });
    } else {
      const leafKey = node.id != null ? String(node.id) : node.value;
      result.push({ title: `${node.label} (${node.value})`, key: leafKey });
    }
  }
  return result;
}

const [Modal, modalApi] = useVbenModal({
  confirmText: '保存',
  onConfirm: handleSave,
  async onOpenChange(isOpen) {
    if (isOpen) {
      const data = modalApi.getData<{ roleId: string }>();
      roleId = data?.roleId ?? null;
      await loadData();
    }
  },
});

async function loadData() {
  loading.value = true;
  try {
    const [tree, assigned] = await Promise.all([
      getPermissionTreeApi(),
      roleId ? getRolePermissionIdsApi(roleId) : Promise.resolve([]),
    ]);
    treeData.value = Array.isArray(tree) ? tree : [];
    checkedKeys.value = Array.isArray(assigned) ? assigned : [];
    expandedKeys.value = treeData.value
      .filter(function (n) {
        return n.children && n.children.length > 0;
      })
      .map(function (n) {
        return `cat_${n.value}`;
      });
  } catch {
    // error handled by interceptor
  } finally {
    loading.value = false;
  }
}

async function handleSave() {
  if (!roleId) return;
  try {
    await assignRolePermissionsApi(roleId, checkedKeys.value);
    message.success('权限分配成功');
    modalApi.close();
    emit('success');
  } catch {
    // error handled by interceptor
  }
}

defineExpose({
  open(data?: { roleId: string }) {
    modalApi.setData(data ?? {}).open();
  },
});
</script>

<template>
  <Modal title="分配权限" :loading="loading">
    <Tree
      v-if="treeData.length > 0"
      v-model:checkedKeys="checkedKeys"
      v-model:expandedKeys="expandedKeys"
      :tree-data="toTreeNodes(treeData)"
      checkable
      check-strictly
      default-expand-all
      block-node
    />
    <div v-else style="text-align: center; padding: 40px; color: #999">
      暂无权限数据，请先在权限管理中添加
    </div>
  </Modal>
</template>
