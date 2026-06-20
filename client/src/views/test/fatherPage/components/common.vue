<template>
  <div>
    <span>子组件</span>
    <el-checkbox-group v-model="selectiveList" @change="handleGetData()">
      <el-checkbox label="复选框 A" />
      <el-checkbox label="复选框 B" />
      <el-checkbox label="复选框 C" />
      <el-checkbox label="禁用" />
      <el-checkbox label="选中且禁用" />
    </el-checkbox-group>
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: Array,
      // eslint-disable-next-line vue/require-valid-default-prop
      default: []
    }
  },
  data() {
    return {
      selectiveList: []
    }
  },
  methods: {
    handleGetData(row) {
      console.log(row)
      this.$emit('input', this.selectiveList)
      console.log(this.selectiveList)
    }

  }
}

</script>

<style>
</style>
