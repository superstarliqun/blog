<template>
  <super-container>
    <div v-if="formData.type == 1">
      <span>{{ formData.id }}、</span>
      <span v-for="(item, index) in formData.questionList" :key="index">
        {{ item }}
        <el-input v-if="index !== formData.questionList.length - 1" v-model="answers[index]" size="medium" :style="{
          width: (formData.answer.split('、')[index].length + 1) * 30 + 'px'
        }" />
      </span>
      <el-button type="primary" plain @click="handleNext()">下一题</el-button>
      <el-button type="primary" plain @click="submit()">提交</el-button>
    </div>
    <div v-if="formData.type == 2">
      <span>{{ formData.id }}、</span>
      <span>{{ formData.question }}</span>
      <div>
        <el-button type="primary" plain style="width: 400px" @click="judge(1)">正确</el-button>
        <el-button type="danger" plain style="width: 400px" @click="judge(2)">错误</el-button>
      </div>
    </div>
  </super-container>
</template>

<script>
export default {
  data() {
    return {
      formData: {},
      answers: []
    }
  },
  mounted() {
    this.requestData()
    window.addEventListener('keydown', this.handleGlobalKey)
  },
  beforeDestroy() {
    window.removeEventListener('keydown', this.handleGlobalKey)
  },
  methods: {
    requestData() {
      this.$get(this.$urls.getQuestions, null, [this.$route.params.id]).then(
        (result) => {
          if (result.data != null) {
            this.formData = result.data
            this.formData.questionList = this.formData.question.split('__')
          }
        }
      )
    },
    handleNext() {
      this.formData = {}
      this.$route.params.id++
      this.requestData()
      this.answers = []
    },
    submit() {
      if (this.formData.type === 1) {
        const errorResult = []
        if (this.answers.length === 0) {
          this.$message({ message: '数据为空', type: 'error' })
          return false
        }
        this.answers.forEach((item, index) => {
          const list = this.formData.answer.split('、')
          if (item !== list[index]) {
            errorResult.push('第' + (index + 1) + '个填空错误')
          }
        })
        if (errorResult.length > 0) {
          this.saveError()
          this.$message({ message: '答题错误：' + errorResult, type: 'error' })
        } else {
          this.$message({ message: '答题成功', type: 'success' })
          this.handleNext()
        }
      } else if (this.formData.type === 2) {
        if (this.answers[0] === this.formData.answer) {
          this.$message({ message: '答题成功', type: 'success' })
        } else {
          this.saveError()
          this.$message({ message: '答题错误', type: 'error' })
        }
      }
    },
    saveError() {
      // const formData = {
      //     questionsId: this.formData.id
      // }
      // this.$post(this.$urls.saveQuestionsError, formData).then(res => {
      // })
    },
    // 设置全局监听方法
    handleGlobalKey(e) {
      if (e.key === 'Enter') {
        this.handleEnter()
      }
    },
    // 监听到回车方法
    handleEnter() {
      this.submit()
    },
    judge(index) {
      this.answers = [index]
      this.submit()
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
</style>
