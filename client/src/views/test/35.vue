<template>
  <div>123123</div>
</template>

<script>
export default {
  data() {
    return {
      // 保留你的data数据
      tags: [],
      categories: []
    }
  },
  mounted() {
    this.requestData()
  },
  methods: {
    requestData() {
      // width: calc(100% - 300px);
      this.$get(this.$urls.getDefaultCategory).then((res) => {
        if (res.code === 0) {
          this.categories = res.data
        }
      })
      this.$get(this.$urls.getTagList).then((res) => {
        this.tags = res.data
      })
    },
    replaceImg(index) {
      return require(`@/assets/img/category/${index}.png`)
    }
  }
}
</script>

<style lang="scss" scoped></style>
