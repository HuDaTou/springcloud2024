import { requestClient } from '#/api/request';

/**
 * 权限 API — 对接 cloud-auth 的 PermissionController (/permissions)
 *
 * SysPermission 字段映射：
 *   name          → 权限描述（旧 permissionDesc）
 *   permissonCode → 权限字符（旧 permissionKey，后端字段名有拼写 typo）
 *   menuId        → 所属菜单ID
 *   category      → 权限类别
 *   httpMethod    → HTTP 方法
 *   path          → 请求路径
 *   serviceName   → 所属服务
 */
export namespace PermissionApi {
  /** 权限列表项（与 cloud-auth SysPermission 对齐） */
  export interface PermissionVO {
    id: string;
    menuId: string | null;
    category: string | null;
    name: string;
    permissonCode: string;
    httpMethod: string | null;
    path: string | null;
    serviceName: string | null;
    createTime: string;
    updateTime: string;
  }

  /** 创建/更新权限参数 */
  export interface PermissionDTO {
    id?: string;
    menuId?: string | null;
    category?: string;
    name: string;
    permissonCode: string;
    httpMethod?: string;
    path?: string;
    serviceName?: string;
  }
}

// ==================== 权限管理 API ====================

/** 获取权限列表（cloud-auth 无搜索接口，前端自行过滤） */
export async function getPermissionListApi() {
  return requestClient.get<PermissionApi.PermissionVO[]>('/cloud-auth/permissions');
}

/** 获取单个权限详情（用于编辑回显） */
export async function getPermissionDetailApi(id: string) {
  return requestClient.get<PermissionApi.PermissionVO>(
    `/cloud-auth/permissions/${id}`,
  );
}

/** 新增权限 */
export async function addPermissionApi(data: PermissionApi.PermissionDTO) {
  return requestClient.post<void>('/cloud-auth/permissions', data);
}

/** 更新权限 */
export async function updatePermissionApi(id: string, data: PermissionApi.PermissionDTO) {
  return requestClient.put<void>(`/cloud-auth/permissions/${id}`, data);
}

/** 删除权限 */
export async function deletePermissionApi(id: string) {
  return requestClient.delete<void>(`/cloud-auth/permissions/${id}`);
}

/** 获取菜单列表（用于权限表单下拉选择所属菜单） */
export async function getPermissionMenusApi() {
  return requestClient.get<{ id: string; title: string }[]>(
    '/cloud-auth/menu',
  );
}

// ==================== 权限树 API ====================

/** 权限树节点（对接 PermissionTreeVO） */
export interface PermissionTreeNode {
  label: string;
  value: string;
  id: number | null;
  children: PermissionTreeNode[] | null;
}

/** 获取权限树（按分类分组，用于角色分配权限时的树形选择） */
export async function getPermissionTreeApi() {
  return requestClient.get<PermissionTreeNode[]>('/cloud-auth/permissions/tree');
}
