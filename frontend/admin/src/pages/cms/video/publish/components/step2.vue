<script setup lang="ts">
import type { FormInstance, UploadProps } from "ant-design-vue";
import type { Ref, UnwrapRef } from "vue";
import {
  addCategory,
  videoCategory,
  uploadVideoCover,
  uploadVideoInfo,
} from "~/api/cms/video";
import { message, Upload } from "ant-design-vue";

import type { CategoryType, videoInfo } from "../type";

const emit = defineEmits(["prevStep", "nextStep"]);
const formRef = ref<FormInstance>();
const labelCol = { lg: { span: 5 }, sm: { span: 5 } };
const wrapperCol = { lg: { span: 19 }, sm: { span: 19 } };

const props = defineProps({
  videoInfo: {
    type: Object,
    required: true, // 表示这个 prop 是必需的
  },
});

const formData = ref<videoInfo>({
  categoryId: undefined,
  videoCover: undefined,
  video: undefined,
  videoTitle: undefined,
  videoDescription: undefined,
  videoType: undefined,
  permission: undefined,
  videoSize: undefined,
});
// 在组件挂载时将父组件传递的值复制到 localVideoInfo 中
onMounted(async () => {
  formData.value.video = props.videoInfo.video;
  formData.value.videoTitle = props.videoInfo.videoTitle;
  formData.value.videoType = props.videoInfo.videoType;
  formData.value.permission = props.videoInfo.permission;
  formData.value.videoSize = props.videoInfo.videoSize;
  formData.value.categoryId = 1;
  await getCategory();
});

// 封面图片上传
const fileList = ref<UploadProps["fileList"]>([])

async function handleUpload() {
  const videocoverFormData = new FormData();
  fileList.value.forEach((file: UploadProps['fileList'][number]) => {
    videocoverFormData.append("videocover", file.originFileObj);
  });
  let videoAddress = formData.value.video;

  videocoverFormData.append("videoaddress", videoAddress as string);
  await uploadVideoCover(videocoverFormData).then((res) => {
    if (res.code === 200) {
      formData.value.videoCover = res.data.value;
      message.success("封面上传成功");
    }
  });
};
function getBase64(file: File) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
  });
}
const beforeUpload: UploadProps["beforeUpload"] = (file) => {
  const isJpgOrPng =
    file.type === "image/jpeg" ||
    file.type === "image/png" ||
    file.type === "image/webp";
  if (!isJpgOrPng) {
    message.error("文件格式必须是jpg或png或webp");
    return isJpgOrPng || Upload.LIST_IGNORE
  }

  const isLt5M = file.size / 1024 / 1024 < 5;
  if (!isLt5M) {
    message.error("图片必须小于 3MB");
    return isLt5M || Upload.LIST_IGNORE
  }
  return false;
};
const previewVisible = ref(false);
const previewImage = ref("");
const previewTitle = ref("");


const handleCancel = () => {
  previewVisible.value = false;
  previewTitle.value = "";
};
const handlePreview = async (file: UploadProps["fileList"][number]) => {
  if (!file.url && !file.preview) {
    file.preview = (await getBase64(file.originFileObj)) as string;
  }
  previewImage.value = file.url || file.preview;
  previewVisible.value = true;
  previewTitle.value =
    file.name || file.url.substring(file.url.lastIndexOf("/") + 1);
};

async function nextStep() {
    try {
        // 尝试执行 handleUpload 函数
        await handleUpload();

        // 如果 handleUpload 执行成功，继续执行 uploadVideoInfo 函数
        // const res = await uploadVideoInfo(formData.value);
        // if (res.code === 200) {
        //     message.success(res.msg);
        // }
    } catch (error) {
        // 捕获 handleUpload 或 uploadVideoInfo 抛出的错误
        message.error(error);
        // 可以在这里添加更多的错误处理逻辑，比如显示错误提示信息等
    }

    // emit("nextStep")
}

function prevStep() {
  emit("prevStep");
}

// 分类
const inputRef = ref();
const categoryList: Ref<UnwrapRef<CategoryType[]>> = ref([]);
const categoryName = ref();

async function getCategory() {
  const { data } = await videoCategory();
  categoryList.value = data;
  console.log(categoryList.value);
}

const categoryLoading = ref(false);

function addCategoryFunc(e: MouseEvent) {
  categoryLoading.value = true;
  e.preventDefault();
  const data = {
    categoryName: categoryName.value,
    id: categoryList.value[categoryList.value.length - 1].id + 1,
  };
  addCategory(data).then((res) => {
    if (res.code === 200) categoryLoading.value = false;
    categoryList.value.push(data);
  });
  categoryName.value = "";
  setTimeout(() => {
    inputRef.value?.focus();
  }, 0);
}
</script>

<template>
  <div>
    <a-form
      ref="formRef"
      :model="formData"
      style="max-width: 500px; margin: 40px auto 0"
    >
      <a-form-item
        label="视频地址"
        name="videoTitle"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        class="stepFormText"
      >
        {{ formData.video }}
      </a-form-item>
      <a-form-item
        label="视频大小"
        name="videoSize"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        class="stepFormText"
      >
        {{ formData.videoSize }}
      </a-form-item>
      <a-form-item
        label="视频格式"
        name="videoType"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        class="stepFormText"
      >
        {{ formData.videoType }}
      </a-form-item>
      <a-form-item
        label="视频权限"
        name="permission"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        class="stepFormText"
      >
        <!-- 三元表达式如果permission是false则展示 公开 -->
        {{ formData.permission === "false" ? "公开" : "不公开" }}
      </a-form-item>
      <a-divider />
      <a-form-item
        label="视频介绍"
        name="videoDescription"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        class="stepFormText"
        :rules="[{ required: false, message: '输入视频介绍' }]"
      >
        <a-textarea
          v-model:value="formData.videoDescription"
          show-count
          :maxlength="100"
        />
      </a-form-item>
      <a-form-item label="分类" style="margin-right: 1rem">
        <a-space>
          <a-select
            v-if="categoryList"
            v-model:value="formData.categoryId"
            :loading="categoryLoading"
            placeholder="选择分类"
            style="width: 15em"
            :options="
              categoryList.map((item) => ({
                value: item.id,
                label: item.categoryName,
              }))
            "
          >
            <template #dropdownRender="{ menuNode: menu }">
              <VNodes :vnodes="menu" />
              <a-divider style="margin: 4px 0" />
              <a-space style="padding: 4px 8px">
                <a-input
                  ref="inputRef"
                  v-model:value="categoryName"
                  placeholder="添加分类"
                />
                <a-button type="text" @click="addCategoryFunc">
                  <template #icon>
                    <plus-outlined />
                  </template>
                  添加
                </a-button>
              </a-space>
            </template>
          </a-select>
        </a-space>
      </a-form-item>
      <a-form-item name="封面选择">
        <div class="clearfix">
          <a-upload
            v-model:file-list="fileList"
            list-type="picture-card"
            @preview="handlePreview"
            :before-upload="beforeUpload"
            :max-count="1"
          >
            <div v-if="fileList.length < 1">
              <plus-outlined />
              <div style="margin-top: 8px">Upload</div>
            </div>
          </a-upload>
          <a-modal
            :open="previewVisible"
            :title="previewTitle"
            :footer="null"
            @cancel="handleCancel"
          >
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </div>
      </a-form-item>

      <a-form-item :wrapper-col="{ span: 19, offset: 5 }">
        <a-button type="primary" @click="nextStep"> 提交 </a-button>
        <a-button style="margin-left: 8px" @click="prevStep"> 上一步 </a-button>
      </a-form-item>
    </a-form>
    <a-button type="primary" @click="getCategory">测试获取标签</a-button>
  </div>
</template>

<style lang="less" scoped>
.stepFormText {
  margin-bottom: 24px;

  .ant-form-item-label,
  .ant-form-item-control {
    line-height: 22px;
  }
}
</style>
