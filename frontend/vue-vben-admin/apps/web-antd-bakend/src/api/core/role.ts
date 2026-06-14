import { requestClient } from '#/api/request';

export namespace RoleApi {
  /** 角色列表项（与后端 SysRole 对齐） */
  export interface RoleVO {
    id: string;
    roleName: string;
    roleKey: string;
    status: number;
    orderNum: string;
    remark: string;
    createTime: string;
  }

  /** 创建/更新角色参数 */
  export interface RoleParams {
    id?: string;
    roleName: string;
    roleKey: string;
    status: number;
    orderNum: string;
    remark?: string;
  }
}

// ==================== 角色管理 API ====================

/** 获取角色列表 */
export async function getRoleListApi() {
  return requestClient.get<RoleApi.RoleVO[]>('/cloud-auth/roles');
}

/** 创建角色 */
export async function createRoleApi(data: RoleApi.RoleParams) {
  return requestClient.post<void>('/cloud-auth/roles', data);
}

/** 更新角色 */
export async function updateRoleApi(id: string, data: RoleApi.RoleParams) {
  return requestClient.put<void>(`/cloud-auth/roles/${id}`, data);
}

/** 删除角色 */
export async function deleteRoleApi(id: string) {
  return requestClient.delete<void>(`/cloud-auth/roles/${id}`);
}

// ==================== 角色权限 API ====================

/** 获取角色已分配的权限ID列表 */
export async function getRolePermissionIdsApi(roleId: string) {
  return requestClient.get<string[]>(`/cloud-auth/roles/${roleId}/permissions`);
}

/** 为角色分配权限 */
export async function assignRolePermissionsApi(
  roleId: string,
  permissionIds: string[],
) {
  return requestClient.put<void>(
    `/cloud-auth/roles/${roleId}/permissions`,
    permissionIds,
  );
}
