<template>
  <div class="category-container">
    <!-- 分类标签容器 -->
    <div ref="tabsContainer" class="category-tabs">
      <!-- 滑动的边框（核心动效元素） -->
      <div class="slide-border" :style="{
        left: slideLeft + 'px',
        top: slideTop + 'px',
        width: slideWidth + 'px',
        height: slideHeight + 'px',
        transition: 'left 0.2s ease, top 0.2s ease, width 0.2s ease, height 0.2s ease'
      }" />

      <!-- 分类标签列表 -->
      <div v-for="(item, index) in categoryList" :key="index" ref="tabItems" class="category-tab"
        @mouseenter="handleMouseEnter(index)">
        {{ item.name }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HoverSlideTabs',
  data() {
    return {
      // 分类列表数据
      categoryList: [
        { name: '全部' },
        { name: '精选' },
        { name: '新品' },
        { name: '热销' },
        { name: '折扣' }
      ],
      slideLeft: 0, // 边框左偏移
      slideTop: 0, // 边框上偏移
      slideWidth: 0, // 边框宽度
      slideHeight: 0 // 边框高度
    }
  },
  mounted() {
    // 初始化：默认定位到第一个标签（可选，也可隐藏初始边框）
    this.initFirstTab()
    // 监听窗口大小变化，重新计算边框位置
    window.addEventListener('resize', this.initFirstTab)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.initFirstTab)
  },
  methods: {
    // 初始化第一个标签的边框位置
    initFirstTab() {
      const firstTab = this.$refs.tabItems?.[0]
      const container = this.$refs.tabsContainer
      if (firstTab && container) {
        this.calcTabPosition(firstTab, container)
      }
    },
    // 鼠标悬浮到标签时更新边框位置
    handleMouseEnter(index) {
      const targetTab = this.$refs.tabItems[index]
      const container = this.$refs.tabsContainer
      if (targetTab && container) {
        this.calcTabPosition(targetTab, container)
      }
    },
    // 计算标签的位置和尺寸（核心方法）
    calcTabPosition(tabEl, containerEl) {
      // 获取元素相对于视口的位置
      const tabRect = tabEl.getBoundingClientRect()
      const containerRect = containerEl.getBoundingClientRect()

      // 计算边框相对于容器的位置（关键：抵消容器的偏移）
      this.slideLeft = tabRect.left - containerRect.left
      this.slideTop = tabRect.top - containerRect.top
      this.slideWidth = tabRect.width
      this.slideHeight = tabRect.height
    }
  }
}
</script>

<style scoped>
.category-container {
  width: 100%;
  max-width: 600px;
  margin: 20px auto;
  padding: 0 20px;
}

.category-tabs {
  position: relative;
  /* 绝对定位参考系 */
  display: flex;
  align-items: center;
  gap: 20px;
  /* 标签间距 */
  padding: 15px 10px;
}

/* 分类标签基础样式 */
.category-tab {
  padding: 8px 16px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  white-space: nowrap;
  z-index: 1;
  /* 文字在边框上方 */
  transition: color 0.2s ease;
  /* 文字颜色过渡 */
}

/* 鼠标悬浮标签时的文字样式（可选） */
.category-tab:hover {
  color: #1890ff;
  font-weight: 500;
}

/* 滑动边框样式 */
.slide-border {
  position: absolute;
  border: 1px solid #1890ff;
  /* 边框颜色可自定义 */
  border-radius: 4px;
  /* 圆角和标签匹配 */
  background: transparent;
  /* 仅边框，无背景 */
  pointer-events: none;
  /* 关键：不拦截鼠标事件 */
  z-index: 0;
  /* 低于标签层级 */
}
</style>
