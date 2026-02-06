<template>
  <div>
    数组对象里我想判断一个对象中的属性是否有值，没有值告诉他哪个下标没值
    <el-button @click="submit">提交</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      list: [
        { id: 1, name: 'a', value: 1 },
        { id: 2, name: 'b', value: null },
        { id: 3, name: 'c', value: 3 },
        { id: 4, name: 'd', value: null },
        { id: 5, name: 'e', value: 4 }
      ]

    }
  },
  mounted() {
    this.handleNum()
  },
  methods: {
    submit() {
      const list = this.handleNum()
      if (list.length > 0) {
        this.$message({
          message: '提示您的题目编号[' + list.join(',') + ']答案为空',
          type: 'warning'
        })
        return false
      }
    },
    handleNum() {
      const noValue = []
      this.list.forEach((element, index) => {
        if (element.value == null) {
          noValue.push(element.id)
        }
      })
      return noValue
    }
  }
}
</script>

<style>
</style>
