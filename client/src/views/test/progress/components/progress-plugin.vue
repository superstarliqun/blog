<template>
  <div class="progress-container">
    <div class="progress-left" :style="{ width: '20%' }"></div>
    <div class="progress-right" :style="{ width: '40%' }"></div>
  </div>
</template>

<script>
export default {
  name: 'ProgressBar'

}
</script>

<style>
.progress-container {
  border-radius: 20px;
  background-color: #e5e5e5;
  display: flex;
  justify-content: space-between; /* 让子元素平均分布，第一个靠左，最后一个靠右 */
  align-items: center; /* 保持子元素垂直居中 */
  height: 40px;
  border: 1px solid #e5e5e5;
  overflow: hidden;
}

.progress-left,
.progress-right {
  height: 100%;
  transition: width 1s ease; /* 进度条的过渡动画 */
}

.progress-right {
  width: 50%;
  background-color: #659af8; /* 进度条的颜色 */
  border-radius: 0 20px 20px 0; /* 右边圆角 */
}

.progress-left {
  width: 50%;
  background-color: #827ef7; /* 进度条的颜色 */
  border-radius: 20px 0 0 20px; /* 左边圆角 */
}
</style>
