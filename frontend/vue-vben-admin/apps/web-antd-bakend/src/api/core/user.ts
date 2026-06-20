import type { UserInfo } from '@vben/types';

import { requestClient } from '#/api/request';

export namespace UserApi {
  /** 用户列表项 */
  export interface UserListVO {
    id: number;
    username: string;
    avatar: string;
    email: string;
    registerType: number;
    loginAddress: string;
    isDisable: boolean;
    createTime: string;
  }

  /** 用户详情 */
  export interface UserDetailsVO {
    id: number;
    nickname: string;
    username: string;
    roles: string[];
    gender: number;
    avatar: string;
    intro: string;
    email: string;
    registerType: number;
    registerIp: string;
    registerAddress: string;
    loginType: number;
    loginIp: string;
    loginAddress: string;
    isDisable: boolean;
    loginTime: string;
    createTime: string;
    updateTime: string;
  }

  /** 用户搜索参数 */
  export interface UserSearchParams {
    username?: string;
    email?: string;
    isDisable?: number;
    createTimeStart?: string;
    createTimeEnd?: string;
  }

  /** 更新用户状态参数 */
  export interface UpdateStatusParams {
    id: number;
    status: number;
  }

  /** 删除用户参数 */
  export interface DeleteUserParams {
    Ids: number[];
  }

  /** 管理员创建用户参数 */
  export interface AdminCreateUserParams {
    username: string;
    password: string;
    nickname?: string;
    email?: string;
    roleIds?: number[];
    isDisable?: boolean;
  }
}

/**
 * 获取用户信息
 */
export async function getUserInfoApi() {
  const data = await requestClient.get<{
    id?: number;
    nickname: string;
    username: string;
    avatar: string;
    intro?: string;
    roles: string[];
  }>('/cloud-auth/user/auth/info');

  return {
    avatar: data.avatar || '',
    realName: data.nickname || data.username,
    roles: data.roles || [],
    userId: String(data.id || 0),
    username: data.username,
    desc: data.intro || '',
    homePath: '/dashboard',
    token: '',
  } as UserInfo;
}

// ==================== 用户管理 API ====================

/** 获取用户列表（支持条件搜索） */
export async function getUserListApi(data?: UserApi.UserSearchParams) {
  return requestClient.post<UserApi.UserListVO[]>('/cloud-auth/user/list', data);
}

/** 获取用户详情 */
export async function getUserDetailsApi(id: number) {
  return requestClient.get<UserApi.UserDetailsVO>(
    `/cloud-auth/user/details/${id}`,
  );
}

/** 更新用户状态（启用/禁用） */
export async function updateUserStatusApi(data: UserApi.UpdateStatusParams) {
  return requestClient.post<void>('/cloud-auth/user/update/status', data);
}

/** 批量删除用户 */
export async function deleteUserApi(data: UserApi.DeleteUserParams) {
  return requestClient.delete<void>('/cloud-auth/user/delete', {
    data,
  });
}

/** 管理员创建用户 */
export async function adminCreateUserApi(data: UserApi.AdminCreateUserParams) {
  return requestClient.post<void>('/cloud-auth/user/admin/create', data);
}
