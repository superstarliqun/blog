<template>
  <div>
    <span>父组件</span>
    <common v-model="children" />
    <el-button type="primary" plain @click="handleGetData()">主要按钮</el-button>
  </div>
</template>

<script>
import common from './components/common'
export default {
  components: {
    common
  },
  data() {
    return {
      children: []
    }
  },
  created() {
    this.requestData()
  },
  methods: {
    requestData() {
      console.log(this.children)
    },
    handleGetData() {
      console.log(this.children)
    }
  }

}
</script>

<style>
</style>
