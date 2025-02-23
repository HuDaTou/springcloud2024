<template>
  <div>
    <router-view></router-view>
  </div>
  <!-- 全局loading -->
  <loading></loading>
  <!-- <Music />
   <SseCount /> -->
</template>

<script setup lang="ts">
import {useDark, useToggle} from "@vueuse/core";
import useWebsiteStore from "@/store/modules/website.ts";

const useWebsite = useWebsiteStore()

let eventSource: EventSource

const getSseData = () => {
  if (!!window.EventSource) {
    eventSource = new EventSource('/api/sse/user/count')
  eventSource.onmessage = (event) => {
    console.log(event.lastEventId)

  } 
    
  }

}

// 定义处理 beforeunload 事件的函数
const handleBeforeUnload = () => {
  if (eventSource) {
    eventSource.close();
    console.log('SSE 连接已关闭');
  }
};

onMounted(() => {
  useWebsite.getInfo()
  getSseData()
  // 监听 beforeunload 事件
  window.addEventListener('beforeunload', handleBeforeUnload);
})





//  深色切换
useDark({
  selector: 'html',
  attribute: 'class',
  valueLight: 'light',
  valueDark: 'dark'
})

useDark({
  onChanged(dark) {
    useToggle(dark)
  }
})
</script>

<style scoped lang="scss">

</style>