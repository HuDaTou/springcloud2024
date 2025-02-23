<script setup lang="ts">

import Card from "@/components/Card/index.vue";

const chargingList = ref([
  {
    name: "One",
    money: "200",
    message: "overthinker，期待新功能"
  },
  {
    name: "李貌",
    money: "100",
    message: "随心"
  },
  {
    name: "曦月",
    money: "30",
    message: "仰天大笑出门去，我辈岂是蓬蒿人"
  },
  {
    name: "sn",
    money: "6",
    message: "膜拜大佬，学习了"
  }
])
</script>

<template>
  <div>
    <!-- TODO 充电榜-->
    <Card title="项目充电榜" prefix-icon="edit" :isScale="true">
      <div class="text-[12px] text-left">
        你们的每一次充电，不仅仅是电量的累积，更是对我们项目的信任和对未来的期待。每一份捐赠，无论大小，都承载着你们对我们的鼓励和对改变世界的信念。是你们，让项目持续更新得到了进一步保障，非常感谢大家！！！
      </div>
      <div class="flex items-center justify-between bg-[--mao-charging-bg-color] rounded">
        <div class="item-div">
          <div>姓名</div>
          <hr>
          <div v-for="charging in chargingList" :key="charging.name">
            {{ charging.name }}
          </div>
        </div>
        <div class="item-div">
          <div>金额</div>
          <hr>
          <div v-for="charging in chargingList" :key="charging.name">{{ '*'.repeat(charging.money.length) }}</div>
        </div>
        <div class="item-div">
          <div>留言</div>
          <hr>
          <div v-for="charging in chargingList" :key="charging.name">
            <template v-if="charging.message.length <= 4">
              {{ charging.message }}
            </template>
            <template v-else>
              <el-popover
                  placement="top-start"
                  :width="200"
                  trigger="hover"
              >
                <template #reference>
                  {{ charging.message.slice(0, 4) }}...
                </template>
                <template #default>
                  <span class="text-[#999] text-[13px]">{{ charging.message }}</span>
                </template>
              </el-popover>
            </template>
          </div>
        </div>
      </div>
      <div class="text-left text-[12px] mt-2 font-bold">
      </div>
    </Card>
  </div>
</template>

<style scoped lang="scss">
.item-div {
  width: 33%;

  div:nth-child(1) {
    text-align: center;
    font-size: 15px;
    font-weight: 600;
  }

  font-size: 13px;

  div {
    margin: 5px 0;
    text-align: center;
    white-space: nowrap; /* 确保文本在一行内显示 */
    overflow: hidden; /* 隐藏溢出的内容 */
    text-overflow: ellipsis; /* 显示省略号 */
  }

  &:nth-child(3) {
    text-align: left;
  }
}
</style>