import { baseRequestClient, requestClient } from '#/api/request';

export namespace AuthApi {
  /** 登录接口参数 */
  export interface LoginParams {
    password?: string;
    username?: string;
  }

  /** 登录接口返回值 */
  export interface LoginResult {
    accessToken: string;
  }

  export interface RefreshTokenResult {
    data: string;
    status: number;
  }
}

/**
 * 登录
 */
export async function loginApi(data: AuthApi.LoginParams) {
  const formData = new FormData();
  if (data.username) formData.append('username', data.username);
  if (data.password) formData.append('password', data.password);

  const result = await baseRequestClient.post<{
    code: string;
    token: string;
    msg: string;
    expire: string;
  }>('/cloud-auth/auth/login', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });

  return {
    accessToken: result.token,
  };
}

/**
 * 刷新accessToken
 */
export async function refreshTokenApi() {
  return baseRequestClient.post<AuthApi.RefreshTokenResult>('/cloud-auth/auth/refresh', {
    withCredentials: true,
  });
}

/**
 * 退出登录
 */
export async function logoutApi() {
  return baseRequestClient.post('/cloud-auth/auth/logout', {
    withCredentials: true,
  });
}

/**
 * 获取用户权限码
 */
export async function getAccessCodesApi() {
  return requestClient.get<string[]>('/cloud-auth/auth/codes');
}
