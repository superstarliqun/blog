<template>
  <div>13</div>
</template>

<script>
export default {

  data() {
    return {}
  },
  created() {
    this.requestData()
  },
  methods: {
    requestData() {
    }
    // requestData() {
    //   var array = [12, 3, 56, 23, 7, 33, 89, 1, 45, 20]
    //   this.quickSorts(array, 0, array.length - 1)
    // },
    // 快速排序递归方法
    // quickSorts(num, left, right) {
    //   var i = left
    //   var j = right
    //   var temp = num[left]
    //   0 >= 9
    //   if (i >= j) {
    //     return
    //   } else {
    //     while (i < j) {
    //       while (num[j] >= temp && i < j) j--
    //       num[i] = num[j]
    //       while (num[i] <= temp && i < j) i++
    //       num[j] = num[i]
    //     }
    //     num[i] = temp
    //     this.quickSorts(num, left, i - 1)
    //     this.quickSorts(num, i + 1, right)
    //   }
    // }
  }

}
</script>

<style></style>
