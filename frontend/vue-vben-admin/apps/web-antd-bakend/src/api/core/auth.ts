import { requestClient } from '#/api/request';

export namespace AuthApi {
  /** 登录接口参数 */
  export interface LoginParams {
    password?: string;
    username?: string;
  }

  /** 登录接口返回的 data 字段内容 */
  export interface LoginResult {
    accessToken: string;
    expire?: string;
  }

  export interface RefreshTokenResult {
    data: string;
    status: number;
  }

  /** 注册接口参数 */
  export interface RegisterParams {
    username: string;
    password: string;
    email: string;
    code: string;
  }

  /** 发送邮箱验证码参数 */
  export interface SendEmailCodeParams {
    email: string;
    type: string;
  }

}

/**
 * 登录
 */
export async function loginApi(data: AuthApi.LoginParams) {
  return requestClient.post<AuthApi.LoginResult>('/cloud-auth/auth/login', data);
}

/**
 * 刷新accessToken
 */
export async function refreshTokenApi() {
  return requestClient.post<AuthApi.RefreshTokenResult>('/cloud-auth/auth/refresh', {
    withCredentials: true,
  });
}

/**
 * 退出登录
 */
export async function logoutApi() {
  return requestClient.post('/cloud-auth/auth/logout', {
    withCredentials: true,
  });
}

/**
 * 获取用户权限码
 */
export async function getAccessCodesApi() {
  return requestClient.get<string[]>('/cloud-auth/auth/codes');
}

/**
 * 注册
 */
export async function registerApi(data: AuthApi.RegisterParams) {
  return requestClient.post<void>('/cloud-auth/auth/register', data);
}

/**
 * 发送邮箱验证码
 */
export async function sendEmailCodeApi(data: AuthApi.SendEmailCodeParams) {
  return requestClient.post<void>('/cloud-auth/Email/send-code', data);
}
