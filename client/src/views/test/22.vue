<template>
  <div class="category-container">
    <!-- 分类标签容器 -->
    <div class="category-tabs">
      <!-- 滑动的选中边框（完整border） -->
      <div class="active-border" :style="{
        left: activeLeft + 'px',
        top: activeTop + 'px',
        width: activeWidth + 'px',
        height: activeHeight + 'px',
        transition:
          'left 0.3s ease, top 0.3s ease, width 0.3s ease, height 0.3s ease'
      }"
      />

      <!-- 分类标签列表 -->
      <div v-for="(item, index) in categoryList" :key="index" class="category-tab"
           @click="handleTabClick(index, $event)" @mouseenter="handleMouseEnter(index, $event)"
           @mouseleave="handleMouseLeave()"
      >
        {{ item.name }}
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'CategoryTabs',
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
      activeIndex: 3, // 当前选中索引
      activeLeft: 0, // 选中边框左偏移
      activeTop: 0, // 选中边框上偏移
      activeWidth: 0, // 选中边框宽度
      activeHeight: 0 // 选中边框高度
    }
  },
  mounted() {
    // 初始化选中状态的位置和尺寸
    this.updateActiveStyle()
    // 监听窗口大小变化，重新计算位置
    window.addEventListener('resize', this.updateActiveStyle)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateActiveStyle)
  },
  methods: {
    // 点击切换分类
    handleTabClick(index, e) {
      this.activeIndex = index
      this.updateActiveStyle(e.target)
    },
    // 鼠标悬浮
    handleMouseEnter(index, e) {
      // 悬浮时仅视觉变化，不改变选中状态
      const el = e.target
      const rect = el.getBoundingClientRect()
      const containerRect = document
        .querySelector('.category-tabs')
        .getBoundingClientRect()
      console.log(rect, containerRect)

      // 悬浮时可添加额外样式（可选）
      el.classList.add('hover')
    },
    // 鼠标离开
    handleMouseLeave() {
      const tabs = document.querySelectorAll('.category-tab')
      tabs.forEach((tab) => tab.classList.remove('hover'))
    },
    // 更新选中边框的位置和尺寸
    updateActiveStyle(target) {
      // 获取目标元素（初始化时取第一个，点击时取当前点击的元素）
      const el =
        target ||
        document.querySelector(
          `.category-tab:nth-child(${this.activeIndex + 2})`
        )
      if (el) {
        const containerRect = document
          .querySelector('.category-tabs')
          .getBoundingClientRect()
        // 获取元素的位置和尺寸（相对于容器）
        const rect = el.getBoundingClientRect()
        this.activeLeft = rect.left - containerRect.left
        this.activeTop = rect.top - containerRect.top
        this.activeWidth = rect.width
        this.activeHeight = rect.height
      }
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
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 15px 10px;
}

/* 分类标签基础样式 */
.category-tab {
  position: relative;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  transition: color 0.2s ease;
  white-space: nowrap;
  z-index: 1;
  /* 确保文字在边框上方 */
  border-radius: 4px;
}

/* 悬浮样式：边框+轻微变色 */
.category-tab.hover {
  color: #1890ff;
  /* 悬浮边框（浅灰色） */
  box-shadow: 0 0 0 1px #e0e0e0;
}

/* 选中状态的滑动边框（主色） */
.active-border {
  position: absolute;
  border: 1px solid #1890ff;
  border-radius: 4px;
  background: transparent;
  z-index: 0;
  pointer-events: none;
  /* 避免遮挡点击事件 */
}

/* 选中标签文字样式 */
.category-tabs .category-tab {
  &:nth-child(2) {
    color: #1890ff;
    font-weight: 500;
  }
}
</style>
