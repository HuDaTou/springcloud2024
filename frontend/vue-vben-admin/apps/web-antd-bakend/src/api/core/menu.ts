import type { RouteRecordStringComponent } from '@vben/types';

import { requestClient } from '#/api/request';

export namespace MenuApi {
  /** 菜单列表项 */
  export interface MenuListVO {
    /** 菜单ID（雪花算法，后端序列化为字符串） */
    id: string;
    title: string;
    icon: string;
    path: string;
    component: string;
    redirect: string;
    affix: number;
    parentId: string | null;
    name: string;
    hideInMenu: number;
    url: string;
    hideInBreadcrumb: number;
    hideChildrenInMenu: number;
    keepAlive: number;
    target: string;
    isDisable: boolean;
    orderNum: number;
    createTime: string;
    updateTime: string;
    children?: MenuListVO[];
  }

  /** 创建/更新菜单参数 */
  export interface MenuFormParams {
    title: string;
    icon?: string;
    path?: string;
    component?: string;
    redirect?: string;
    affix?: number;
    parentId?: string | null;
    name?: string;
    hideInMenu?: number;
    url?: string;
    hideInBreadcrumb?: number;
    hideChildrenInMenu?: number;
    keepAlive?: number;
    target?: string;
    isDisable?: boolean;
    orderNum?: number;
  }
}

/**
 * 获取菜单列表（扁平列表）
 */
export async function getMenuListApi() {
  return requestClient.get<MenuApi.MenuListVO[]>('/cloud-auth/menu');
}

/**
 * 获取菜单树（树形结构）
 */
export async function getAllMenusApi() {
  return requestClient.get<RouteRecordStringComponent[]>('/cloud-auth/menu/all');
}

/**
 * 获取单个菜单
 */
export async function getMenuDetailApi(id: string) {
  return requestClient.get<MenuApi.MenuListVO>(`/cloud-auth/menu/${id}`);
}

/**
 * 创建菜单
 */
export async function createMenuApi(data: MenuApi.MenuFormParams) {
  return requestClient.post<void>('/cloud-auth/menu', data);
}

/**
 * 更新菜单
 */
export async function updateMenuApi(id: string, data: MenuApi.MenuFormParams) {
  return requestClient.put<void>(`/cloud-auth/menu/${id}`, data);
}

/**
 * 删除菜单（包含子菜单）
 */
export async function deleteMenuApi(id: string) {
  return requestClient.delete<void>(`/cloud-auth/menu/${id}`);
}
