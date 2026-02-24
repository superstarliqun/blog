<template>
  <div id="app">
    <router-view :key="$route.fullPath" />
    <RightSide />
  </div>
</template>
<script>
import RightSide from '@/components/sidebar'
export default {
  components: { RightSide },
  created() {
    this.$store.dispatch('app/initTheme')
  },
  methods: {
    initContextMenu() {
      const mq = window.matchMedia('(max-width: 768px)')
      // 当前是否匹配
      if (!mq.matches) {
        const handleContextMenu = (e) => {
          this.$refs.contextMenuRef.show(e)
        }
        const handleClick = () => {
          this.$refs.contextMenuRef.hide()
        }
        document.addEventListener('contextmenu', handleContextMenu)
        document.addEventListener('click', handleClick)

        // 在组件销毁时移除事件监听
        this.$once('hook:beforeDestroy', () => {
          document.removeEventListener('contextmenu', handleContextMenu)
          document.removeEventListener('click', handleClick)
        })
      }
    }

  }
}
</script>
<style>
/* 注意：这里不要加 scoped */
html,
body {
  margin: 0;
  padding: 0;
  /* 核心代码：阻止触控板/手机端回弹白边 */
  /* overscroll-behavior-y: none; */
}
</style>
