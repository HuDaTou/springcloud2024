import { message } from 'ant-design-vue'

// 查询视频标签
export async function videoTag() {
  return useGet('/tag/list').catch(msg => message.warn(msg))
}

// 新增标签
export async function addTag(data: any) {
  return usePut('/tag', data).catch(msg => message.warn(msg))
}

// 新增分类
export async function addCategory(data: any) {
  return usePut('/category', data).catch(msg => message.warn(msg))
}

// 查询文章分类
export async function videoCategory() {
  return useGet('/category/list').catch(msg => message.warn(msg))
}

// 上传视频封面
export async function uploadCover(data: any) {
  return usePost('/video/upload/cover', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).catch(msg => message.warn(msg))
}

// 上传视频
export async function videoUpload(data: any) {
  return usePost('/video/upload/video', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).catch(msg => message.warn(msg))
}

// 获取上传文件的id
export async function getUploadId() {
  return useGet('/upload/getUploadId').catch(msg => message.warn(msg))
}

// 上传视频封面
export async function uploadVideoCover(data: any) {
  return usePost('/video/upload/cover', data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  }).catch(msg => message.warn(msg))
}

// 上传视频的信息
export async function uploadVideoInfo(data: any) {
  return usePost('/video/upload/videoInfo', data).catch(msg => message.warn(msg))
}

// 删除视频
export async function deleteVideo(data: any) {
  return useDelete('/video', data).catch(msg => message.warn(msg))
}

// 视频搜索
export async function videoSearch(data: any) {
  return usePost('/video/search', data).catch(msg => message.warn(msg))
}

// 视频列表
export async function videoList() {
  return useGet('/video/back/list').catch(msg => message.warn(msg))
}

// 修改视频权限
export async function updateVideopermission(data: any) {
  return usePost('/video/permisson', data).catch(msg => message.warn(msg))
}
