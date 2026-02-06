<template>
  <div v-show="visible" class="context-menu" :style="{ left: adjustedX + 'px', top: adjustedY + 'px' }">
    <div class="menu-item" @click="handleRefresh">
      刷新页面
    </div>
    <div class="menu-item" @click="handleCopyUrl">
      复制链接
    </div>
    <div class="divider"></div>
    <div class="menu-item" @click="toggleTheme">
      {{ isDark ? '浅色模式' : '深色模式' }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'ContextMenu',
  data() {
    return {
      visible: false,
      x: 0,
      y: 0,
      menuWidth: 0,
      menuHeight: 0,
      isDark: false,
      excludeSelectors: ['.chat-messages'] // 需要过滤的元素，此元素下不展示右键菜单，如有自定义的就展示自定义的
    }
  },
  computed: {
    adjustedX() {
      const windowWidth = window.innerWidth
      return this.x + this.menuWidth > windowWidth ? this.x - this.menuWidth : this.x
    },
    adjustedY() {
      const windowHeight = window.innerHeight
      return this.y + this.menuHeight > windowHeight ? this.y - this.menuHeight : this.y
    }
  },
  created() {
    this.$nextTick(() => {
      // this.isDark = getThemeMode() === 'dark'
    })
  },
  methods: {
    /**
     * 显示右键菜单
     * @param event
     */
    show(event) {
      const isInExcludeArea = this.excludeSelectors.some(selector => {
        const element = event.target.closest(selector)
        return element !== null
      })

      if (isInExcludeArea) {
        return
      }

      event.preventDefault()
      this.x = event.clientX
      this.y = event.clientY
      this.visible = true

      this.$nextTick(() => {
        this.menuWidth = this.$el.offsetWidth
        this.menuHeight = this.$el.offsetHeight
        this.x = event.clientX
        this.y = event.clientY
      })
    },
    /**
     * 隐藏右键菜单
     */
    hide() {
      this.visible = false
    },
    /**
     * 刷新页面
     */
    handleRefresh() {
      window.location.reload()
      this.hide()
    },
    /**
     * 返回上页
     */
    handleBack() {
      this.$router.back()
      this.hide()
    },
    /**
     * 前进下页
     */
    handleForward() {
      this.$router.forward()
      this.hide()
    },
    /**
     * 复制链接
     */
    handleCopyUrl() {
      navigator.clipboard.writeText(window.location.href)
      this.$message.success('链接已复制到剪贴板')
      this.hide()
    },
    /**
     * 切换主题
     */
    toggleTheme() {
    }
  }
}
</script>

<style scoped>
.context-menu {
  width: 180px;
  position: fixed;
  background: #e3e3e3;
  border: 1px solid rgba(0, 0, 0, .1);
  box-shadow: 0 8px 24px rgba(0, 0, 0, .1);
  border-radius: 4px;
  padding: 5px 0;
  display: flex;
  gap: 2px;
  flex-direction: column;
  z-index: 9999;
  font-size: 14px !important;
}

.menu-item {
  padding: 4px 10px;
  margin: 0 4px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  gap: 8px;
  color: #1f2937;
}

.menu-item:hover {
  background-color: #b2b2b2;
  border-radius: 4px;
  /* margin: 0 4px; */
}

.menu-item i {
  width: 14px;
}

.divider {
  height: 1px;
  background-color: rgba(0, 0, 0, .1);
  margin: 5px 0;
}
</style>
