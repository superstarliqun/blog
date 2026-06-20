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
      const str = '中影股份1123123〔2026〕11111号'
      const pattern = /^(.*?)〔(\d+)〕(\d+)号$/
      const match = str.match(pattern)
      if (match) {
        const prefix = match[1] // 中影股份
        const year = match[2] // 2026
        const seq = match[3] // 1
        console.log(prefix, year, seq)
      }
    }
  }
}
</script>

<style lang="scss" scoped></style>
