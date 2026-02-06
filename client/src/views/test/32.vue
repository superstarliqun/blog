<template>
  <div class="padding-zoom-div">
    鼠标悬浮我会通过padding放大，文字不动
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 保留你的data数据
    }
  },
  mounted() {
    // 保留原有业务逻辑
    this.getIpInfo()
  },
  methods: {
    getIpInfo() {
      console.log('获取IP信息')
    }
  }
}
</script>

<style scoped>
.padding-zoom-div {
  width: 200px;
  background-color: #42b983;
  color: #fff;
  text-align: center;
  line-height: 60px;
  border-radius: 8px;
  margin: 50px auto;
  padding: 20px;
  transition: padding 0.3s ease;
  font-size: 16px;
}

.padding-zoom-div:hover {
  padding: 50px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
</style>
