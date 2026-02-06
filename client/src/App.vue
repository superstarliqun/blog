<template>
  <div id="app">
    <router-view :key="$route.fullPath" />
    <rightside />
    <ContextMenu ref="contextMenuRef" />
  </div>
</template>
<script>
import rightside from '@/components/rightside'
import ContextMenu from '@/components/menu/ContextMenu'
export default {
  components: {
    rightside,
    ContextMenu
  },
  created() {
    // 页面加载时，去读取 Cookie 并设置到 HTML 标签上
    this.$store.dispatch('app/initFontTheme')
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
<style lang="scss" scoped>
//公共自定义返回顶部
#app {
  height: 100%;
}

.custom-backtop {
  height: 100%;
  width: 100%;
  background-color: #f2f5f6;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.12);
  text-align: center;
  line-height: 40px;
  color: #1989fa;
}

.iconfont {
  font-size: 26px;
  margin-right: 12px;
  cursor: pointer;
  color: #5a5c66;
}
</style>
