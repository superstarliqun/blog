<template>
  <div class="main">
    <div ref="head" class="head">
      <ul>
        <li>应用</li>
        <li>媒体</li>
        <li>服务</li>
        <li>主题</li>
        <li>表情</li>
        <li>项目</li>
      </ul>
    </div>
    <div class="container">123123</div>
  </div>
</template>

<script>
export default {
  name: 'CategoryTabs',
  data() {
    return {
      currentWidth: 0
    }
  },
  mounted() {
    this.updatePadding()
  },

  methods: {
    updatePadding() {
      this.$refs.head.style.padding = '0px'
      this.$refs.head.style.transition = 'padding 0.5s'
      for (let index = 0; index <= 50; index++) {
        setInterval(() => {
          this.$refs.head.offsetHeight
          this.$refs.head.style.padding = `${index}px`
          this.$refs.head.style.width = `${index * 12}px`
        }, 1000)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
  background-color: #f7f9fe;
  height: 2300px;
  position: relative;
  overflow: auto;
}

.container {
  max-width: 1440px;
  margin: 0 auto;
  margin-top: 20px;
  border-radius: 12px;
  background-color: white;
  min-height: calc(80% - 20px);
  box-shadow: 0 8px 16px -4px #2c2d300c;
  border: 1px solid #e3e8f7;
  padding: 40px;
}

.head {
  position: fixed;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  width: 510px;
  margin: 0 auto;
  height: 66px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 9999px;
  transition: all 0.4s ease;
  background-color: greenyellow;

  ul {
    display: flex;
    gap: 20px;
    justify-content: center;
    top: 90px; // 贴在顶部

    li {
      cursor: pointer;
      padding: 8px;
    }
  }
}
</style>
