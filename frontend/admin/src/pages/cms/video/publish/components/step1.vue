<script setup lang="ts">
import type { UploadChangeParam, UploadProps } from 'ant-design-vue'
import { message, Upload } from 'ant-design-vue'
const env = import.meta.env

const emit = defineEmits(['uploudVideoMsgToStep2', 'nextStep'])
import {
  videoUpload
} from '~/api/cms/video'



const fileList = ref<UploadProps['fileList']>([])

async function nextStep() {
  try {
    emit('nextStep')
  }
  catch (errorInfo) {
    console.log('Failed:', errorInfo)
  }
}

async function loadVideoInfo(data: any) {  
  emit('uploudVideoMsgToStep2', data)
}


const beforeUpload: UploadProps['beforeUpload'] = file => {
  const videoFormat = ['video/mp4', 'video/webm', 'video/ogg']
  const isVideo = videoFormat.includes(file.type)
  if (!isVideo) {
    message.error('视频文件格式必须是mp4或webm或ogg')
    return isVideo || Upload.LIST_IGNORE
  }
  const isLt5M = file.size / 1024 / 1024 < 1024
  if (!isLt5M) {
    message.error('视频必须小于 1024MB')
    return isLt5M || Upload.LIST_IGNORE
  }

  return true
}

const chunkSize = 10 * 1024 * 1024

const customRequest = async (file: any) => {
  // message.loading(`视频上传中${file.file.name}`)
  const formData = new FormData()
  formData.append('VideoFile', file.file)
  videoUpload(formData).then(res => {
    if (res.code === 200) {
      fileList.value[0].status = 'done'
      message.success("视频上传成功")


      loadVideoInfo(res.data).then(nextStep) // 等待 loadVideoInfo 完成
    } else {
      fileList.value[0].status = 'error'
      message.error("视频上传失败")
    }
  })
}




const handleChange = (info: UploadChangeParam) => {
  const status = info.file.status;
  if (status == 'uploading') {
    return
  }
  if (status === 'done' && info.file.response.code === 200) {
    info.file.response.data.value

    message.success(info.file.response.data.value)


  } else if (status === 'error') {
    message.error(`${info.file.name} 视频上传失败`);
  }
};
function handleDrop(e: DragEvent) {
  console.log(e);
}
</script>

<template>
  <a-divider dashed />
  <div>
    <a-upload-dragger v-model:fileList="fileList" name="videoFile" :before-upload="beforeUpload"
      :max-count="1" :custom-request="customRequest" @change="handleChange" @drop="handleDrop">
      <p class="ant-upload-drag-icon">
        <inbox-outlined></inbox-outlined>
      </p>
      <p class="ant-upload-text">Click or drag file to this area to upload</p>
      <p class="ant-upload-hint">
        Support for a single or bulk upload. Strictly prohibit from uploading company data or other
        band files
      </p>
    </a-upload-dragger>


    <a-divider />
    <div class="step-form-style-desc ant-steps-item-title">
      <h3>
        说明
      </h3>
      <h4>有手就行</h4>

    </div>
  </div>
</template>

<style lang="less" scoped>
.step-form-style-desc {
  padding: 0 56px;

  h3 {
    margin: 0 0 12px;
    font-size: 16px;
    line-height: 32px;
  }

  h4 {
    margin: 0 0 4px;
    font-size: 14px;
    line-height: 22px;
  }

  p {
    margin-top: 0;
    margin-bottom: 12px;
    line-height: 22px;
  }
}
</style>
