<template>
  <div class="scroll-demo">
    <!-- 你的页面内容 -->
    <div class="content" style="height: 2000px;">
      向下滑动页面，控制台会打印滑动方向~
    </div>
  </div>
</template>

<script>
export default {
  name: 'ScrollListener',
  data() {
    return {
      lastScrollTop: 0, // 上一次滚动位置
      scrollTimer: null, // 节流定时器
      scrollThreshold: 10 // 滑动阈值（可选，超过这个距离才判定为有效滑动）
    }
  },
  mounted() {
    // 页面挂载后绑定滚动事件
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy() {
    // 组件销毁前解绑事件，避免内存泄漏
    window.removeEventListener('scroll', this.handleScroll)
    // 清空定时器
    if (this.scrollTimer) clearTimeout(this.scrollTimer)
  },
  methods: {
    handleScroll() {
      // 节流处理：每80ms只执行一次，优化性能
      if (this.scrollTimer) clearTimeout(this.scrollTimer)

      this.scrollTimer = setTimeout(() => {
        // 获取当前滚动距离（兼容所有浏览器）
        const currentScrollTop = window.pageYOffset || document.documentElement.scrollTop

        // 判断是否向下滑动（且超过阈值）
        if (currentScrollTop - this.lastScrollTop > this.scrollThreshold) {
          console.log('Vue2中检测到：用户向下滑动')
          // 这里写你需要执行的逻辑，比如：
          // this.hideNavbar(); // 隐藏导航栏
          // this.loadMoreData(); // 加载更多数据
        } else if (this.lastScrollTop - currentScrollTop > this.scrollThreshold) {
          // 向上滑动（可选）
          console.log('Vue2中检测到：用户向上滑动')
        }

        // 更新上一次滚动位置（处理顶部边界）
        this.lastScrollTop = currentScrollTop <= 0 ? 0 : currentScrollTop
      }, 80)
    }
  }
}
</script>
