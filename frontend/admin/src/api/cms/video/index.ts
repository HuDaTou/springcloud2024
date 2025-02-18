import { message } from 'ant-design-vue'

// 获取上传文件的id
export async function getUploadId() {
  return useGet('/upload/getUploadId').catch(msg => message.warn(msg))
}

// 上传视频
export async function videoUpload(data: any) {
  return usePost('/video/upload', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).catch(msg => message.warn(msg))
}

export async function videoCategory() {
  return useGet('/category/list').catch(msg => message.warn(msg))
}

// 新增分类
export async function addCategory(data: any) {
  return usePut('/category', data).catch(msg => message.warn(msg))
}

// 上传视频封面
export async function uploadVideoCover(data: any) {
  return usePost('/video/upload/videocover', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).catch(msg => message.warn(msg))
}

// 上传视频的信息
export async function uploadVideoInfo(data: any) {
  return usePost('/video/upload/videoInfo', data).catch(msg => message.warn(msg))
}
