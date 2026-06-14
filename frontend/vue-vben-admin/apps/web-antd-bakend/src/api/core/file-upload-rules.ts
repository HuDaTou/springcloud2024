import { requestClient } from '#/api/request';

/** 网关前缀 */
const BASE = '/cloud-media-asset-processing/api/media/rules';

/** 列表项 */
export interface RulesVO {
  id: string;
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

/** IPage 分页结构 */
export interface RulesPageVO {
  records: RulesVO[];
  total: number;
  size: number;
  current: number;
}

/** 创建/更新参数 */
export interface RulesFormParams {
  ruleName: string;
  fileCategory: string[];
  allowedExtensions: string[];
  maxSizeKb: number;
  storagePath: string;
  storagePrefix: string;
  isActive: boolean;
  description: string;
}

/** 分页获取文件上传规则 */
export async function getRulesListApi(pageNo: number, pageSize: number) {
  return requestClient.get<RulesPageVO>(
    `${BASE}/list?pageNo=${pageNo}&pageSize=${pageSize}`,
  );
}

/** 获取单个规则 */
export async function getRuleDetailApi(id: string) {
  return requestClient.get<RulesVO>(`${BASE}/${id}`);
}

/** 创建规则 */
export async function createRuleApi(data: RulesFormParams) {
  return requestClient.post<void>(BASE, data);
}

/** 更新规则 */
export async function updateRuleApi(id: string, data: RulesFormParams) {
  return requestClient.put<void>(`${BASE}/${id}`, data);
}

/** 删除规则 */
export async function deleteRuleApi(id: string) {
  return requestClient.delete<void>(`${BASE}/${id}`);
}
