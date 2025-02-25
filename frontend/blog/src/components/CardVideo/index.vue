<script setup lang="ts">
import {getVideoList} from "@/apis/video";
import {ElMessage} from "element-plus";
import usePaginationStore from "@/store/modules/pagination";
import {useWindowSize} from "@vueuse/core";

const videoList = ref([]);

const paginationStore = usePaginationStore();

// 监听页数
watch(() => paginationStore.videoPagination.current, () => {
  getvideoListFunc()
  // 滚动到顶部
  window.scrollTo(0, 300);
})

// 屏幕宽度
const { width } = useWindowSize()

function getvideoListFunc() {
  getVideoList(paginationStore.videoPagination.current, paginationStore.videoPagination.pageSize).then(res => {
    if (res.code === 200) {
      paginationStore.videoPagination.total = res.data.total;
      // 过滤内容
      res.data.page = res.data.page.map((item: any) => {
        item.videoContent = item.videoContent.replace(/[*#>`~\-\\[\]()\s]|(\n\n)/g, '')
        // 提取前 50 个字符
        item.videoContent = item.videoContent.substring(0, 60) + '...';
        return item;
      });
      videoList.value = res.data.page;
    } else {
      ElMessage.error(res.msg);
    }
  })
}
function loadContent() {
  getvideoListFunc()
}
</script>

<template>
  <!-- 封装文章列表卡片 -->
  <div v-view-request="{ callback: loadContent }">
    <template v-for="(video,index) in videoList" :key="video.id" v-if="videoList.length > 0">
      <div v-slide-in @click="$router.push('/video/'+video.id)" class=" h-92 md:h-60 mt-4 flex flex-col md:flex-row card overflow-hidden shadow-md mb-5 mx-2 dark:bg-[#1D1D1D]">
        <div class="w-full md:h-full md:w-1/2 h-40" v-if="index % 2 == 1 || width < 768">
          <div class="relative w-full h-full">
            <div class="relative img w-full h-full">
              <img class="w-full h-full object-cover hover:scale-110 ease-linear duration-300" v-lazy="true" :data-src="video.videoCover" alt="文章封面">
            </div>
            <div class="classify text-white text-xs p-1.5 w-11 absolute top-0 left-0 rounded-br-lg">
              {{ video.categoryName }}
            </div>
          </div>
        </div>
        <div class="md:w-1/2 w-full m-1 px-2 flex flex-col pl-5 pt-2 leading-7">
          <div class="hover:text-[#409EFF] transition-colors text-2xl font-bold w-fit">{{ video.videoTitle }}</div>
          <div class="flex text-xs mt-2 space-x-2">
            <div class="flex">
              <SvgIcon name="reading"/>
              <span class="ml-1">{{ video.visitCount }}</span>
            </div>
            <div class="flex">
              <SvgIcon name="comments"/>
              <span class="ml-1">{{ video.commentCount }}</span>
            </div>
            <div class="flex">
              <SvgIcon name="like"/>
              <span class="ml-1">{{ video.likeCount }}</span>
            </div>
            <div class="flex">
              <SvgIcon name="collection"/>
              <span class="ml-1">{{ video.favoriteCount }}</span>
            </div>
          </div>
          <p class="overflow-ellipsis overflow-hidden h-10 md:h-[3.9rem] leading-tight mt-2 text-[#475569]">
            {{ video.videoContent }}
          </p>
          <div class="flex space-x-2">
            <div class="tag">
              <span>标签：</span>
              <el-tag size="small" class="mr-2" v-for="tag in video.tags">{{ tag }}</el-tag>
            </div>
          </div>
          <div class=" text-xs mt-4 flex">
            <p class="mr-4 mb-1">发布于：{{ video.createTime }}</p>
            <p>更新于：{{ video.updateTime }}</p>
          </div>
        </div  >
        <div class="w-full md:h-full md:w-1/2 h-40" v-if="index % 2 == 0 && width > 768">
          <div class="relative w-full h-full">
            <div class="relative img w-full h-full">
              <img class="w-full h-full object-cover" v-lazy="true" :data-src="video.videoCover" alt="文章封面">
            </div>
            <div class="classify text-white text-xs p-1.5 w-11 absolute top-0 left-0 rounded-br-lg">
              {{ video.categoryName }}
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
  <template v-if="videoList.length == 0">
    <el-skeleton :rows="8" animated />
  </template>
</template>

<style scoped lang="scss">

.card {
  border-radius: $border-radius;

  &:hover img {
    transform: scale(1.1);
  }
  .img {
    position: relative;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s linear;
    }
  }
}


.classify {
  z-index: 1;
  position: absolute;
  top: 0;
  color: white;
  padding: 10px;
  backdrop-filter: blur(5px);
}
</style>