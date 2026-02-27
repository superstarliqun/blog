<template>
  <div class="loading-container">
    <!-- 插槽：可自定义加载提示文字，可选 -->
    <slot>
      <!-- 默认只显示动画，无文字 -->
    </slot>
  </div>
</template>

<script>
import lottie from 'lottie-web'

export default {
  name: 'LoadingAnimation',
  props: {
    animationPath: {
      type: String,
      default: '/loading.json'
    },
    loop: {
      type: Boolean,
      default: true
    },
    height: {
      type: [String, Number],
      default: '100%'
    },
    width: {
      type: [String, Number],
      default: '100%'
    }
  },
  data() {
    return {
      animationInstance: null
    }
  },
  mounted() {
    this.initLoadingAnimation()
  },
  beforeDestroy() {
    // 组件销毁时销毁动画实例，避免内存泄漏
    if (this.animationInstance) {
      this.animationInstance.destroy()
    }
  },
  methods: {
    initLoadingAnimation() {
      // 初始化lottie动画
      this.animationInstance = lottie.loadAnimation({
        container: this.$el, // 组件根元素作为容器
        renderer: 'svg',
        loop: this.loop,
        autoplay: true,
        path: this.animationPath
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.loading-container {
  height: 100px;
}
</style>
