import type { UserInfo } from '@vben/types';

import { requestClient } from '#/api/request';

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
