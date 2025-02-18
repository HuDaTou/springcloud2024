// 获取服务监控数据
import { message } from 'ant-design-vue'
import type SystemInfo from '~/pages/system/server-monitoring/type.ts'

export async function getServiceMonitorData() {
  return useGet<SystemInfo>('/monitor/server', null).catch(msg => message.warn(msg))
}

// 获取系统监控数据sse
export async function getSseSystemMonitorData() {
  return useGet<SystemInfo>('/monitor/server/sse', null, {
    headers: {
      'Content-Type': 'text/event-stream',
      'Cache-Control': 'no-cache',
      'Connection': 'keep-alive',
    },
  }).catch(msg => message.warn(msg))
}
