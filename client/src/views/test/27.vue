<template>
  <div class="wrap">
    <div class="content">
      <div class="debounce">
        <div>
          <h1>防抖演示</h1>
          <span>没有防抖</span>
          <el-input v-model="input1" placeholder="请输入内容" @input="handleInput(1)" />
          <span class="margintop10">添加防抖</span>
          <el-input v-model="input2" placeholder="请输入内容" @input="handleInput(2)" />
        </div>
        <div class="outbox">
          <div v-for="(line, idx) in out1" :key="idx">{{ line }}</div>
        </div>
      </div>
      <div class="throttle" style="margin-top: 80px;">
        <div>
          <h1>节流演示</h1>
          <span>没有节流</span>
          <el-input v-model="input3" placeholder="请输入内容" @input="handleInput(3)" />
          <span class="margintop10">添加节流</span>
          <el-input v-model="input4" placeholder="请输入内容" @input="handleInput(4)" />
        </div>
        <div class="outbox">
          <div v-for="(line, idx) in out2" :key="idx">{{ line }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import debounce from 'lodash/debounce'
import throttle from 'lodash/throttle'
export default {
  name: 'CategoryTabs',
  data() {
    return {
      input1: null,
      input2: null,
      input3: null,
      input4: null,
      out1: [],
      out2: []
    }
  },
  created() {
    // 给“有防抖”输入框用的真正防抖函数
    this.debouncedPush = debounce((val, target) => {
      target.push(val)
      if (target.length > 7) target.shift()
    }, 500)

    // 给“有节流”输入框用的节流函数
    this.throttledPush = throttle((val, target) => {
      target.push(val)
      if (target.length > 7) target.shift()
    }, 500)
  },
  mounted() {
    this.handleInput()
  },
  methods: {
    handleInput(type) {
      let val, target
      if (type === 1) { // 无防抖
        val = this.input1
        if (!val) return
        target = this.out1
        target.push(val)
      } else if (type === 2) { // 有防抖
        val = this.input2
        if (!val) return
        this.debouncedPush(val, this.out1)
        return // 直接返回，不再走后面逻辑
      } else if (type === 3) { // 无节流
        val = this.input3
        if (!val) return
        target = this.out2
        target.push(val)
      } else if (type === 4) { // 有节流
        val = this.input4
        if (!val) return
        this.throttledPush(val, this.out2)
        return
      }

      // 只有 1、3 会走到这里
      if (target.length > 7) target.shift()
    }
  }
}
</script>

<style lang="scss" scoped>
.wrap {
  background-color: #f7f9fe;
  position: relative;
  overflow: auto;
  display: flex;
  justify-content: center;
  height: 100%;

  .content {
    max-width: 500px;
    margin-top: 80px;

    .debounce {
      display: flex;
    }

    .throttle {
      display: flex;
    }

    .outbox {
      border: 1px solid #ccc;
      min-width: 0;
      width: 300px;
      margin-left: 20px;
      border-radius: 8px;
      padding: 8px;

      >div {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }
}

.margintop10 {
  margin-top: 10px;
  display: block;
}
</style>
