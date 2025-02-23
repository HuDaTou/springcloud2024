<script setup lang="ts">
import IntroduceRow from './introduce-row.vue'
import SalesCard from './sales-card.vue'
import TopSearch from './components/top-search.vue'
import ProportionSales from './proportion-sales.vue'
import OfflineData from './offline-data.vue'
import { sseData } from './type'

defineOptions({
  name: 'Analysis',
})

let eventSource: EventSource
const sseUUID = ref()
const sseDataInfo = ref<sseData>({
  onlineCount: 2000,
  userCount: 0,
  articleCount: 0,
  photoCount: 0,
}

)

const getSseData = () => {
  eventSource = new EventSource('/api/sse/data')
  eventSource.onmessage = (event) => {
    const data = JSON.parse(event.data)
    console.log(event.data)
    console.log(data)
    sseDataInfo.value = data
    // console.log(sseDataInfo.value.onlineCount)
    sseUUID.value = event.lastEventId
    // console.log(sseUUID.value)
    // serverInfo.value = event.data

  } 
}

onMounted(() => {
  getSseData()
})

onUnmounted(() => {
  eventSource.close() 
})


const loading = ref(false)

const visitData = ref([])
</script>

<template>
  <page-container>
    <Suspense :fallback="null">
      <IntroduceRow :loading="loading" :sse-data-info="sseDataInfo" />
    </Suspense>

    <Suspense :fallback="null">
      <SalesCard />
    </Suspense>

    <a-row
      :gutter="24"
      :style="{ marginTop: '24px' }"
    >
      <a-col :xl="12" :lg="24" :md="24" :sm="24" :xs="24">
        <Suspense :fallback="null">
          <TopSearch />
        </Suspense>
      </a-col>
      <a-col :xl="12" :lg="24" :md="24" :sm="24" :xs="24">
        <Suspense :fallback="null">
          <ProportionSales />
        </Suspense>
      </a-col>
    </a-row>

    <!-- <Suspense :fallback="null">
      <OfflineData />
    </Suspense> -->
  </page-container>
</template>
