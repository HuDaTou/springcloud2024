<script setup lang="ts">
import { message } from 'ant-design-vue'
import Step1 from './components/step1.vue'
import Step2 from './components/step2.vue'
import Step3 from './components/step3.vue'
import { permission } from 'process'

defineOptions({
  name: 'StepForm',
})
const state = ref({
  currentTab: 0,
  form: null,
})

function nextStep() {
  if (state.value.currentTab < 2)
    state.value.currentTab += 1
}


function prevStep() {
  if (state.value.currentTab > 0)
    state.value.currentTab -= 1
}

function finish() {
  state.value.currentTab = 0
}


function uploudVideoMsgToStep2(data: any) {
  videoInfo.value.video = data.video
  videoInfo.value.videoSize = data.videoSize
  videoInfo.value.videoTitle = data.videoTitle
  videoInfo.value.videoType   = data.videoType


}
const videoInfo = ref({
  categoryId: "",
  videoCover: "",
  video: "",
  videoTitle: "",
  videoDescription: "",
  videoType: "",
  permission: "",
  videoSize: "",
});

</script>

<template>
  <page-container>
    <template #content>
      <a-divider dashed />
    </template>
    <a-card :bordered="false">
      <a-steps class="steps" :current="state.currentTab">
        <a-step title="上传视频" />
        <a-step title="确认视频信息" />
        <a-step title="完成" />
      </a-steps>
      <a-divider dashed />
      <div class="content">
        <Step1 v-if="state.currentTab === 0" @next-step="nextStep" @uploudVideoMsgToStep2="uploudVideoMsgToStep2" />
        <Step2 v-if="state.currentTab === 1" @next-step="nextStep" @prev-step="prevStep" :videoInfo="videoInfo" />
        <Step3 v-if="state.currentTab === 2" @prevs-step="prevStep" @finish="finish" />
      </div>
    </a-card>
  </page-container>
</template>

<style lang="less" scoped>
  .steps {
    max-width: 750px;
    margin: 16px auto;
  }
</style>
