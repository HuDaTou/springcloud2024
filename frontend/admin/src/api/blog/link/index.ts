import { message } from 'ant-design-vue'

// 精选站点列表
export async function linkList() {
  return useGet('/link/back/list').catch(msg => message.warn(msg))
}

// 搜索精选站点
export async function searchLink(data: any) {
  return usePost('/link/back/search', data).catch(msg => message.warn(msg))
}

// 是否通过精选站点
export async function isCheckLink(data: any) {
  return usePost('/link/back/isCheck', data).catch(msg => message.warn(msg))
}

// 删除精选站点
export async function deleteLink(data: any) {
  return useDelete('/link/back/delete', JSON.stringify(data)).catch(msg => message.warn(msg))
}
