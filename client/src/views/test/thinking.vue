<template>
  <div>
    <span v-for="(item,index ) in array" :key="index">
      {{ item }} <br>
    </span>
    <span>公共：{{ publicStr }}</span>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 源数据
      array: ['flower', 'flow', 'flight'],
      // 公共
      publicStr: ''
    }
  },
  mounted() {
    this.requestData()
  },
  methods: {
    requestData() {
      const list = []
      var minLength = 0
      // 将所有字符分割成二维数组，并找到所有字符中最少的字符
      for (let i = 0; i < this.array.length; i++) {
        const item = this.array[i].split('')
        list.push(item)
        if (minLength === 0 || item.length < minLength) {
          minLength = item.length
        }
      }
      // 循环遍历
      for (let i = 0; i < minLength; i++) {
        const char = list[0][i]
        // 查询数组某一个位置是否都等于某一个字符
        if (list.every(s => s[i] === char)) {
          this.publicStr += char
        } else {
          break
        }
      }
      console.log(this.publicStr)
    }
  }
}
</script>

<style>
</style>
