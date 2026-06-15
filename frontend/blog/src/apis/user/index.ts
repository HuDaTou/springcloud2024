import http from "@/utils/http.ts";

export interface ApiResult<T> {
    code: number;
    msg: string;
    data: T;
    timestamp: number;
}

export interface UserInfo {
    nickname: string;
    username: string;
    avatar: string;
    intro: string;
    registerType: number;
    email: string;
    roles: string[];
    gender: number;
    permissions: string[];
    loginTime: string;
    createTime: string;
}

export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    accessToken: string;
    expire: string;
}

export interface RegisterRequest {
    username: string;
    password: string;
    code: string;
    email: string;
}

export function login(data: LoginRequest): Promise<ApiResult<LoginResponse>> {
    return http({
        url: '/cloud-auth/auth/login',
        data: data,
        method: 'post'
    })
}

export interface ResetConfirmRequest {
    code: string;
    email: string;
}

export interface ResetPasswordRequest {
    password: string;
    code: string;
    email: string;
}

export interface UpdateEmailRequest {
    code: string;
    email: string;
    password: string;
}

export interface UpdateUserRequest {
    nickname: string;
    gender: number;
    avatar: string;
    intro: string;
}

export interface OauthLoginData {
    username: string;
    password: string;
}

// 退出登录
export function logout(): Promise<ApiResult<void>> {
    return http({
        url: '/user/logout',
        method: 'post'
    })
}

// 第三方登录
export function oauthLogin(accessToken: string, type: string, username: string): Promise<ApiResult<LoginResponse>> {
    return http({
        url: '/user/login',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Login-Type': type,
            'Access-Token': accessToken,
        },
        data: {
            username: username,
            password: accessToken,
        },
        method: 'post'
    })
}

// 获取用户信息
export function getUserInfo(): Promise<ApiResult<UserInfo>> {
    return http({
        url: '/user/auth/info',
        method: 'get'
    })
}

// 用户注册
export function register(data: RegisterRequest): Promise<ApiResult<void>> {
    return http({
        url: '/cloud-auth/auth/register',
        data: data,
        method: 'post'
    })
}

// 重置密码步骤一
export function resetPasswordStepOne(data: ResetConfirmRequest): Promise<ApiResult<void>> {
    return http({
        url: '/user/reset-confirm',
        data: data,
        method: 'post'
    })
}

// 重置密码步骤二
export function resetPasswordStepTwo(data: ResetPasswordRequest): Promise<ApiResult<void>> {
    return http({
        url: '/user/reset-password',
        data: data,
        method: 'post'
    })
}

// 修改用户信息
export function updateUserAccount(data: UpdateUserRequest): Promise<ApiResult<void>> {
    return http({
        url: '/user/auth/update',
        data: data,
        method: 'post'
    })
}

// 修改电子邮箱
export function updateEmail(data: UpdateEmailRequest): Promise<ApiResult<void>> {
    return http({
        url: '/user/auth/update/email',
        data: data,
        method: 'post'
    })
}

// 修改第三方登录电子邮箱
export function updateThirdEmail(data: UpdateEmailRequest): Promise<ApiResult<void>> {
    return http({
        url: '/user/auth/third/update/email',
        data: data,
        method: 'post'
    })
}
