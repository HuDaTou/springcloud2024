import { requestClient } from '#/api/request';

/** 网关前缀 */
const BASE = '/cloud-media-asset-processing/media/rules';

/** 文件上传规则VO */
export namespace FileUploadRulesApi {
  /** 文件上传规则 */
  export interface FileUploadRule {
    id: number;
    ruleName: string;
    fileCategory: string[];
    allowedExtensions: string[];
    maxSizeKb: number;
    storagePath: string;
    storagePrefix: string;
    isActive: boolean;
    description: string;
    createTime: string;
    updateTime: string;
  }

  /** 分页结果 */
  export interface PageResult {
    records: FileUploadRule[];
    total: number;
    size: number;
    current: number;
  }

  /** 创建/更新参数 */
  export interface FileUploadRuleParams {
    ruleName: string;
    fileCategory: string[];
    allowedExtensions: string[];
    maxSizeKb: number;
    storagePath: string;
    storagePrefix: string;
    isActive: boolean;
    description: string;
  }
}

/**
 * 获取上传规则详情
 * @param id 规则ID
 */
export async function getFileUploadRuleApi(id: number) {
  return requestClient.get<FileUploadRulesApi.FileUploadRule>(`${BASE}/${id}`);
}

/**
 * 创建上传规则
 * @param data 规则信息
 */
export async function createFileUploadRuleApi(data: FileUploadRulesApi.FileUploadRuleParams) {
  return requestClient.post<void>(BASE, data);
}

/**
 * 更新上传规则
 * @param id 规则ID
 * @param data 规则信息
 */
export async function updateFileUploadRuleApi(id: number, data: FileUploadRulesApi.FileUploadRuleParams) {
  return requestClient.put<void>(`${BASE}/${id}`, data);
}

/**
 * 删除上传规则
 * @param id 规则ID
 */
export async function deleteFileUploadRuleApi(id: number) {
  return requestClient.delete<void>(`${BASE}/${id}`);
}

/**
 * 分页获取上传规则列表
 * @param pageNo 页码
 * @param pageSize 每页数量
 */
export async function getFileUploadRuleListApi(pageNo: number, pageSize: number) {
  return requestClient.get<FileUploadRulesApi.PageResult>(
    `${BASE}/list`,
    { params: { pageNo, pageSize } }
  );
}
