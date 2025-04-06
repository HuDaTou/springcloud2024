<script setup lang="ts">
const emit = defineEmits(['finish'])
const router = useRouter()
function finish() {
  emit('finish')
}
function toOrderList() {
  router.push('/profile/advanced')
}
</script>

<template>
  <div>
    <a-form>
      <a-result title="操作成功" :is-success="true" sub-title="已上传视频文件" style="max-width: 560px; margin: 40px auto 0;">

        <template #extra>

          <a-button style="margin-left: 8px" >
            返回首页
          </a-button>
        </template>
      </a-result>
    </a-form>
  </div>
</template>

<style lang="less" scoped>
  .information {
    line-height: 22px;

    .ant-row:not(:last-child) {
      margin-bottom: 24px;
    }
  }
  .money {
    font-family: "Helvetica Neue",sans-serif;
    font-weight: 500;
    font-size: 20px;
    line-height: 14px;
  }
</style>
