<template>
  <div class="p1">
    <div v-for="(item,index1) in data[0].optionsArr" :key="index1" class="block">
      <el-button :class="data[0].uids[index]!== undefined ?'active':''" @click="goToQuestion(index)">{{ data[0].uids[index]=== undefined?true:false+'---' }}{{ index+1 }}</el-button>
    </div>
    <div>
      <div>{{ question.name }}</div>
      <el-radio-group v-model="radio">
        <el-radio :label="'A'">选项一</el-radio>
        <el-radio :label="'B'">选项二</el-radio>
        <el-radio :label="'C'">选项三</el-radio>
      </el-radio-group>
      <el-button @click="top">上一题</el-button>
      <el-button @click="next">下一题</el-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      data: [{
        orderIndex: 1,
        uids: '1,2,3',
        optionsArr: [
          { id: 1, name: '第一个问题的题目' },
          { id: 2, name: '第二个问题的题目' },
          { id: 3, name: '第三个问题的题目' },
          { id: 4, name: '第四个问题的题目' },
          { id: 5, name: '第五个问题的题目' },
          { id: 6, name: '第六个问题的题目' },
          { id: 7, name: '第七个问题的题目' },
          { id: 8, name: '第八个问题的题目' }
        ]
      }],
      question: {},
      index: 0,
      radio: null
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    // 页面初始化的方法 获取题目以及答题记录
    initData() {
      this.data[0].uids = this.data[0].uids.split(',')
      this.index = this.data[0].uids.length
      // 默认第一题显示
      this.question = this.data[0].optionsArr[this.index]
    },
    // 执行的是保存数据的接口，此时你的data没有刷新对不对，没有刷新是不是两种选择，一种是你手动push  一种是获取页面初始化的方法
    next(row) {
      console.log(this.data[0].uids, this.index, this.data[0].uids[this.index])
      this.data[0].uids.push(this.radio)
      this.index++
      this.question = this.data[0].optionsArr[this.index]
    },
    top() {
      this.index--
      this.question = this.data[0].optionsArr[this.index]
    },
    goToQuestion(index) {
      if (this.data[0].uids[index] === undefined) {
        alert('暂未答题，不能跳过')
        return false
      }
      this.index = index
      this.question = this.data[0].optionsArr[index]
    }
  }
}
</script>

<style lang="scss" scoped>
.p1 {
  display: flex;
  height: 100px;
}

.block {
  justify-content: center;
  text-align: center;
  p {
    width: 30px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    background-color: #ccc;
    color: #fff;
    margin: 2px;
  }
}
.active {
  background-color: blue !important ;
}
</style>
