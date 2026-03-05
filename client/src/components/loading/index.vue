<template>
  <div class="github-loader-container">
    <div class="github-loader" />
  </div>
</template>

<script>
export default {
  name: 'GitHubLoader'
}
</script>

<style scoped>
.github-loader-container {
  display: inline-flex;
  justify-content: center;
  align-items: center;
}

/* 核心加载动画样式 */
.github-loader {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border-style: solid;
  border-width: 2px;
  border-color: #e1e4e8;
  border-top-color: #000000;
  box-sizing: border-box;
  animation: github-spin linear infinite 1s;
}

/* 旋转关键帧 */
@keyframes github-spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}
</style>
