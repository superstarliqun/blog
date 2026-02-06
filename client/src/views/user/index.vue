<template>
  <super-container>
    <el-tabs v-model="activeName">
      <el-tab-pane label="文章管理" name="first">
        <Article />
      </el-tab-pane>
      <el-tab-pane label="文章分类" name="fourth">文章分类</el-tab-pane>
      <el-tab-pane label="用户管理" name="second">用户管理</el-tab-pane>
    </el-tabs>
  </super-container>
</template>

<script>
import Article from './article/index'
export default {
  components: { Article },
  data() {
    return {
      activeName: 'first'
    }
  },
  methods: {
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
  padding-top: 20px;
}
</style>
