<template>
  <div>
    <button @click="toggle">{{ listening ? '停止' : '开始' }}识别</button>
    <p>结果：{{ text }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      listening: false,
      text: '',
      recognition: null
    }
  },
  mounted() {
    // 浏览器前缀兼容
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition
    if (!SpeechRecognition) {
      alert('当前浏览器不支持 Web Speech API')
      return
    }
    this.recognition = new SpeechRecognition()
    this.recognition.lang = 'zh-CN' // 中文
    this.recognition.interimResults = true // 实时输出中间结果
    this.recognition.continuous = true // 持续监听

    this.recognition.onresult = (e) => {
      let final = ''
      let temp = ''
      for (let i = e.resultIndex; i < e.results.length; i++) {
        const str = e.results[i][0].transcript
        e.results[i].isFinal ? (final += str) : (temp += str)
      }
      this.text = final + temp
    }
    this.recognition.onend = () => {
      // 识别意外结束自动重启（长语音场景）
      if (this.listening) this.recognition.start()
    }
  },
  beforeDestroy() {
    this.recognition && this.recognition.stop()
  },
  methods: {
    toggle() {
      if (this.listening) {
        this.recognition.stop()
        this.listening = false
      } else {
        this.recognition.start()
        this.listening = true
      }
    }
  }
}
</script>
