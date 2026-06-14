import { requestClient } from '#/api/request';

/** 网关前缀 */
const BASE = '/cloud-media-asset-processing/api/media/assets';

/** 媒体资产VO */
export interface AssetVO {
  id: string;
  fileName: string;
  objectName: string;
  bucketName: string;
  fileType: string;
  fileSize: number;
  fileSizeFormatted: string;
  uploaderId: string;
  status: string;
  statusDesc: string;
  thumbnailName: string;
  width: number;
  height: number;
  duration: number;
  downloadUrl: string;
  thumbnailUrl: string;
  createTime: string;
  updateTime: string;
}

/** IPage 分页结构 */
export interface AssetPageVO {
  records: AssetVO[];
  total: number;
  size: number;
  current: number;
}

/** 创建/更新参数 */
export interface AssetFormParams {
  fileName: string;
  objectName: string;
  bucketName: string;
  fileType: string;
  fileSize: number;
  fileMd5: string;
  uploaderId: string;
  status: string;
  thumbnailName: string;
  width: number;
  height: number;
  duration: number;
}

/** 分页获取媒体资产列表 */
export async function getAssetListApi(pageNo: number, pageSize: number) {
  return requestClient.get<AssetPageVO>(
    `${BASE}?pageNo=${pageNo}&pageSize=${pageSize}`,
  );
}

/** 获取单个资产详情 */
export async function getAssetDetailApi(id: string) {
  return requestClient.get<AssetVO>(`${BASE}/${id}`);
}

/** 创建资产 */
export async function createAssetApi(data: AssetFormParams) {
  return requestClient.post<void>(BASE, data);
}

/** 更新资产 */
export async function updateAssetApi(id: string, data: AssetFormParams) {
  return requestClient.put<void>(`${BASE}/${id}`, data);
}

/** 删除资产 */
export async function deleteAssetApi(id: string) {
  return requestClient.delete<void>(`${BASE}/${id}`);
}

/** 批量删除资产 */
export async function batchDeleteAssetApi(ids: string[]) {
  return requestClient.post<void>(`${BASE}/batch/delete`, ids);
}
