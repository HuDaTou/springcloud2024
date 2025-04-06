<script setup lang="ts">
import type { Ref, UnwrapRef } from 'vue'
import { createVNode } from 'vue'
import { Modal, message } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import type { CategoryType, TagType } from '~/pages/blog/essay/publish/type.ts'
import {

  videoList,
  videoCategory,
  videoTag,
  videoUpload,
  deleteVideo,
  updateVideopermission,
  videoSearch
} from '~/api/cms/video'

const formData = reactive({
  categoryId: undefined,
  userName: undefined,
  tagId: undefined,
  videoTitle: undefined,
  videoType: undefined,
  permission: undefined,
})

// 分类
const categoryList: Ref<UnwrapRef<CategoryType[]>> = ref([])
// 标签
const tagList: Ref<TagType[]> = ref([])

onMounted(async () => {
  await refreshFunc()
  await getCategory()
  await getTag()
})

async function getCategory() {
  const { data } = await videoCategory()
  categoryList.value = data
}
async function getTag() {
  const { data } = await videoTag()
  tagList.value = data
}

type Key = string | number

const state = reactive<{
  selectedRowKeys: Key[]
  loading: boolean
}>({
  selectedRowKeys: [], // Check here to configure the default column
  loading: false,
})

interface DataType {
  id: string
  title: string
  key: string
  orderNum: number
  status: boolean
  createTime: string
}

const loading = ref(false)
const tabData: Ref<UnwrapRef<DataType[]>> = ref([])

/**
 * 选中表格
 */
function onSelectChange(selectedRowKeys: Key[]) {
  state.selectedRowKeys = selectedRowKeys
}

const columns: any = [
  {
    title: '编号',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '封面',
    dataIndex: 'videoCover',
    align: 'center',
  },
  {
    title: '标题',
    dataIndex: 'videoTitle',
    align: 'center',
  },

  {
    title: '分类',
    dataIndex: 'categoryName',
    align: 'center',
  },
  {
    title: '标签',
    dataIndex: 'tagsName',
    align: 'center',
  },
  {
    title: '访问量',
    dataIndex: 'videoVisit',
    align: 'center',
  },
  {
    title: '作者',
    dataIndex: 'userName',
    align: 'center',
  },
  {
    title: '权限',
    dataIndex: 'permission',
    align: 'center', 
  },
  {
    title: '状态',
    dataIndex: 'status',
    align: 'center',
    slots: { customRender: 'status' },
  },
  {
    title: '发布时间',
    dataIndex: 'createTime',
    align: 'center',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
  },
]

async function refreshFunc() {
  loading.value = true
  const { data } = await videoList()
  loading.value = false
  tabData.value = data.value
}

async function onFinish(values: any) {
  loading.value = true
  const { data } = await videoSearch(values)
  if (data && data.length > 0) {
    tabData.value = data.map((item: any) => {
      item.isTop = item.isTop === 1
      return item
    })
  }
  else {
    message.warn('没有查询到相关视频')
    tabData.value = []
  }

  loading.value = false
}

// 修改视频权限
function updatevideopermissionFunc(id: number, permission: boolean, record: any) {
  record.statusLoading = true
  updateVideopermission({ id, permission }).then((res) => {
    if (res.code === 200) {
      message.success('状态修改成功')
      record.statusLoading = false
    }
  })
}



// 删除文章
function onDelete(ids?: string[]) {
  if (ids) {
    deleteVideo(ids).then((res) => {
      if (res.code === 200) {
        message.success('删除成功')
        refreshFunc()
      }
    })
  }
  else {
    // 批量删除
    const ids = state.selectedRowKeys
    Modal.confirm({
      title: '注意',
      icon: createVNode(ExclamationCircleOutlined),
      content: `确定删除编号为 【${ids.join(',')}】 的文章吗？`,
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        deleteVideo(ids as string[]).then((res) => {
          if (res.code === 200) {
            message.success('删除成功')
            refreshFunc()
          }
        })
      },
    })
  }
}

// 前台域名
const domain = import.meta.env.VITE_APP_DOMAIN_NAME_FRONT
</script>

<template>
  <layout
    :form-state="formData"
    @update:refresh-func="refreshFunc"
    @update:on-finish="onFinish"
  >
    <template #form-items>
      <a-form-item label="标题" name="videoTitle" style="margin-right: 1rem">
        <a-input v-model:value="formData.videoTitle" placeholder="输入视频标题" style="width: 15em" />
      </a-form-item>
      <a-form-item label="作者" name="userName" style="margin-right: 1rem">
        <a-input v-model:value="formData.userName" placeholder="输入作者" style="width: 15em" />
      </a-form-item>
      <a-form-item label="分类" name="categoryId" style="margin-right: 1rem">
        <a-space>
          <a-select
            v-if="categoryList"
            v-model:value="formData.categoryId"
            placeholder="选择分类"
            style="width: 13em"
            :options="categoryList.map(item => ({ value: item.id, label: item.categoryName }))"
          />
        </a-space>
      </a-form-item>
      <a-form-item label="状态" name="permission" style="margin-right: 1rem">
        <a-space>
          <a-select
            v-model:value="formData.permission"
            style="width: 10em"
            placeholder="选择状态"
          >
            <a-select-option :value="0">
              公开
            </a-select-option>
            <a-select-option :value="1">
              私密
            </a-select-option>
          </a-select>
        </a-space>
      </a-form-item>
    </template>
    <template #operate-btn>
      <a-button type="primary" style="margin-right: 10px" @click="$router.push({ path: '/cms/video/publish' })">
        <template #icon>
          <PlusOutlined />
        </template>
        添加
      </a-button>
      <a-button type="dashed" danger ghost :disabled="state.selectedRowKeys.length === 0" @click="onDelete()">
        <template #icon>
          <DeleteOutlined />
        </template>
        删除
      </a-button>
      <a-button
        class="green"
        style="margin-right: 10px"
        :disabled="state.selectedRowKeys.length === 0 || state.selectedRowKeys.length > 1"
        @click="$router.push({ path: '/blog/essay/publish', query: { id: state.selectedRowKeys[0] } })"
      >
        <template #icon>
          <FileSyncOutlined />
        </template>
        修改
      </a-button>
    </template>
    <template #table-content>

    </template>
  </layout>
  <button @click="refreshFunc">刷新</button>
</template>

<style scoped lang="scss">

</style>
