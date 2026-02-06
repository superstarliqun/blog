<template>
  <super-container>
    <ul class="question-list">
      <li @click="path(1)">有序</li>
      <li @click="path(2)">随机</li>
      <li @click="path(3)">错误集合</li>
    </ul>
  </super-container>
</template>

<script>
export default {
  data() {
    return {}
  },
  mounted() {},
  methods: {
    path(index) {
      if (index === 1) {
        this.$router.push({ path: '/questions/1' })
      }
      if (index === 2) {
        this.$router.push({ path: '/questions/2' })
      }
      if (index === 3) {
        this.$router.push({ path: '/error-questions' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
super-container {
  max-width: 1440px;
  padding: 0 1.5rem;
  height: 100%;
  margin: 0 auto;
  display: block;
  padding-top: 100px;
}

.question-list {
  display: flex;
  gap: 20px;
  margin: 0 auto;
  justify-content: center;

  li {
    background-color: #ddebfd;
    height: 200px;
    width: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    font-size: 28px;
    cursor: pointer;
  }
}
</style>
