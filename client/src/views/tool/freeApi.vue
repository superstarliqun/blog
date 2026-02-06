<template>
  <super-container>
    <div
      style="
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 50px;
      "
    >
      <h2>
        <span v-if="!isAnalyze">JSON 输入验证：</span>
        <span v-else>JSON 解析结果：</span>
      </h2>
      <div class="tool-button">
        <el-button type="primary" size="small" plain @click="validateJson">{{
          !isAnalyze ? '解析/格式化' : '返回'
        }}</el-button>
        <el-button type="primary" size="small" plain @click="submit"
          >保存</el-button
        >
        <el-button type="primary" size="small" plain>查询</el-button>
        <el-button type="primary" size="small" plain>历史</el-button>
      </div>
    </div>
    <textarea
      v-if="!isAnalyze"
      v-model="jsonInput"
      placeholder="请输入JSON格式的数据..."
      rows="10"
      style="width: 100%"
    >
    </textarea>
    <pre v-else v-html="parsedData"></pre>
  </super-container>
</template>

<script>
// Using require
const hljs = require('highlight.js/lib/core')
// Load any languages you need
hljs.registerLanguage('javascript', require('highlight.js/lib/languages/json'))
import 'highlight.js/styles/vs.css'
export default {
  data() {
    return {
      isAnalyze: false,
      jsonInput:
        '[{"age": 42, "key": "2", "name": "胡彦祖", "address": "西湖区湖底公园1号"}, {"age": 42, "key": "2", "name": "胡彦祖", "address": "西湖区湖底公园1号"}]',
      errorMessage: '',
      parsedData: null,
      isValid: false
    }
  },
  methods: {
    // 验证json格式
    validateJson() {
      try {
        this.isAnalyze = !this.isAnalyze

        // 解析原始输入
        const inputData = this.jsonInput.trim()
        let jsonObject

        if (inputData.startsWith('{') && inputData.endsWith('}')) {
          jsonObject = JSON.parse(inputData)
        } else if (inputData.startsWith('[') && inputData.endsWith(']')) {
          jsonObject = JSON.parse(inputData)
        } else {
          throw new Error('Invalid JSON format')
        }
        // 格式化JSON对象（缩进2个空格）
        const formattedJson = JSON.stringify(jsonObject, null, 2)
        this.parsedData = hljs.highlight(formattedJson, {
          language: 'javascript'
        }).value
        console.log(this.parsedData)
        this.errorMessage = ''
        this.isValid = true
      } catch (e) {
        this.parsedData = '无效的JSON格式！'
        this.isValid = false
      }
    },
    //
    submit() {
      this.$post(this.$urls.saveJsonApi, JSON.parse(this.jsonInput)).then(
        (res) => {
          if (res.code === 0) {
            this.$message.success('保存成功')
          }
        }
      )
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
  padding-top: 20px;
}

h2 {
  display: inline-block;
}

pre {
  border: 1px solid black;
  border-radius: 4px;
  padding: 14px;
  margin: 0;
  // background-color: #282c33;
}

textarea {
  padding: 14px;
}
</style>
