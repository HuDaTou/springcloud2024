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