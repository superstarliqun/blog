<template>
  <div>
    <div class="a">12312</div>
    <div class="b">12312</div>
    <div class="c">12312</div>
    <div class="d">12312</div>
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

<style lang="scss" scoped>
.a {
  background-color: #1890FF
}

.b {
  background-color: #0F172A
}

.c {
  background-color: #1E293B
}

.d {
  background-color: #F8FAFC
}
</style>
