<template>
  <div class="global_search_container">
    <transition name="fade">
      <div class="modal-mask" @click.self="handleClose">
        <div iv class="modal-content" @click.stop>
          <!-- 左上角工具栏 -->
          <div class="mac-control-bar">
            <div class="btns">
              <div class="dot close" @click="handleClose">
                <span class="icon">×</span>
              </div>
              <div class="dot minimize" />
              <div class="dot maximize" />
            </div>
          </div>
          <input ref="searchInput" v-model="input" class="search_input" type="text" placeholder="请输入搜索内容..."
            @input="debouncedSearch">
          <ul class="result-container">
            <li v-for="(item, index) in queryData" :key="index" class="result_item"
              :class="{ active: index === selectedIndex }">
              <p class="result_item_title" @click="handleTo(item)"
                v-html="item.highlights?.title?.[0] || item.title || '暂无标题'" />
              <div v-for="(item3, index3) in item.highlights.contentText" :key="index3">
                <span v-html="item3" />
              </div>
            </li>
          </ul>
          <div v-if="loading" class="op">
            找到 {{ total }} 条结果，用时 {{ searchDuration }} 毫秒
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { debounce } from 'lodash'
export default {
  data() {
    return {
      queryData: [],
      input: null,
      selectedIndex: -1,
      total: 0,
      searchDuration: null,
      loading: false
    }
  },
  watch: {
    selectedIndex() {
      this.$nextTick(() => {
        const el = this.$el.querySelector('.result_item.active')
        if (el) {
          el.scrollIntoView({
            block: 'nearest',
            behavior: 'smooth'
          })
        }
      })
    }
  },
  mounted() {
    document.body.style.overflow = 'hidden'
    this.$refs.searchInput.focus()
    this.debouncedSearch()
    window.addEventListener('keydown', this.handleKeyboard)
  },
  beforeDestroy() {
    window.removeEventListener('keydown', this.handleKeyboard)
  },
  methods: {
    // 防抖
    debouncedSearch: debounce(function () {
      this.loading = false
      if (this.input == null || !this.input.trim()) {
        this.queryData = []
        return
      }
      this.handleSearch()
    }, 500),

    // 监听搜索
    handleSearch() {
      this.loading = true
      const startTime = performance.now()
      this.$get(this.$urls.elasticSearchFindHighlight, { keywords: this.input }).then((res) => {
        const endTime = performance.now()
        this.searchDuration = ((endTime - startTime) / 1000).toFixed(2)
        this.total = res.data.length
        this.queryData = res.data
        // ⭐ 搜索结果出来后，默认选中第一条
        this.selectedIndex = res.data.length > 0 ? 0 : -1
      })
    },

    // 监听输入
    handleInput() {
      this.handleSearch()
    },

    // 监听关闭
    handleClose() {
      document.body.style.overflow = ''
      this.$emit('close')
    },

    // 地址跳转
    handleTo(row) {
      this.$router.push({
        name: 'postViewer',
        params: { id: row.mysqlId }
      })
      this.handleClose()
    },

    // 监听esc
    handleKeyboard(e) {
      if (e.key === 'Escape' || e.keyCode === 27) {
        this.handleClose()
      }
      if (!this.queryData.length) return
      // ↓ 向下
      if (e.key === 'ArrowDown') {
        e.preventDefault()
        if (this.selectedIndex < this.queryData.length - 1) {
          this.selectedIndex++
        }
      }
      // ↑ 向上
      if (e.key === 'ArrowUp') {
        e.preventDefault()
        if (this.selectedIndex > 0) {
          this.selectedIndex--
        }
      }
      // Enter 跳转
      if (e.key === 'Enter') {
        e.preventDefault()
        const current = this.queryData[this.selectedIndex]
        if (current) {
          this.handleTo(current)
        }
      }
    }

  }
}
</script>

<style lang="scss" scoped>
.global_search_container {
  position: relative;
  width: 100%;
  min-height: 100vh;

  // 蒙版核心代码
  .modal-mask {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    z-index: 9999;

    /* 关键：蒙版本身不要太厚，主要靠 blur 透出底层内容 */
    background: rgba(255, 255, 255, 0.1);

    /* 标准毛玻璃写法 */
    backdrop-filter: blur(18px) saturate(180%);
    -webkit-backdrop-filter: blur(18px) saturate(180%);

    display: flex;
    justify-content: center;
    align-items: flex-start;
    padding-top: 10vh;

    &::before {
      content: "";
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      z-index: -1;
      /* 注入 SVG 噪点数据 */
      background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
      /* 透明度必须极低，建议 0.03 - 0.08 */
      opacity: 0.05;
      pointer-events: none;
    }

    // 内容区域
    .modal-content {
      background: var(--card-background);
      padding: 20px 18px;
      border-radius: 12px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
      text-align: center;
      width: 45rem;
      display: flex;
      flex-direction: column;

      max-height: 80vh;
      height: fit-content;

      .fade-enter-active,
      .fade-leave-active {
        transition: opacity 0.3s;
      }

      .fade-enter,
      .fade-leave-to {
        opacity: 0;
      }

      // 右上角工具栏
      .mac-control-bar {
        display: flex;
        align-items: center;

        .btns {
          display: flex;
          gap: 8px;

          .dot {
            width: 12px;
            height: 12px;
            border-radius: 50%;
            position: relative;
            cursor: default;
            display: flex;
            align-items: center;
            justify-content: center;

            .icon {
              font-size: 10px;
              color: rgba(0, 0, 0, 0.5);
              display: none; // 默认隐藏图标
              pointer-events: none; // 防止干扰点击
            }

            &.close {
              background: #ff5f56;
              cursor: pointer;

              &:hover {
                .icon {
                  display: block;
                }
              }
            }

            &.minimize {
              background: #ffbd2e;
            }

            &.maximize {
              background: #27c93f;
            }
          }
        }
      }

      // 搜索框
      .search_input {
        border: 1px solid rgb(232, 232, 232);
        background-color: var(--background);
        line-height: 38px;
        border-radius: 8px;
        font-size: 18px;
        font-weight: 600;
        margin-top: 18px;
        padding: 4px 18px;
        color: var(--text-color);

        &:focus-visible {
          outline: none;
          border-color: #78aff9;
          box-shadow: 0 0 2px rgba(120, 175, 249, 0.9);
        }
      }

      // 搜索结果集
      .result-container {
        flex: 1;
        overflow-y: auto; // 开启纵向滚动
        margin-top: 16px;
        padding: 0 10px;
        list-style: none;
        text-align: left; // 结果列表通常左对齐

        /* 优化滚动条样式（可选） */
        &::-webkit-scrollbar {
          width: 6px;
        }

        &::-webkit-scrollbar-track {
          background: #f7f8fa;
        }

        &::-webkit-scrollbar-thumb {
          background: #eef1f5;
        }

        &::-webkit-scrollbar-thumb:hover {
          background-color: #c2c8d0;
        }

        display: flex;
        flex-direction: column;
        gap: 16px;

        .result_item {
          font-size: 14px;
          color: #606266;
          padding: 10px 12px;
          border-radius: 8px;
          transition: all 0.15s ease;

          &:hover {
            background: rgba(120, 175, 249, 0.18);
            box-shadow: inset 0 0 0 1px rgba(120, 175, 249, 0.15);
          }

          &.active {
            background: rgba(120, 175, 249, 0.32);
            box-shadow: inset 0 0 0 1px rgba(120, 175, 249, 0.35);

          }

          .result_item_title {
            font-size: 16px;
            line-height: 1.2;
            font-weight: 600;
            color: black;
            cursor: pointer;
            cursor: pointer;
          }

          ::v-deep em {
            font-style: normal;
            color: #1976d2;
          }
        }
      }

      .op {
        text-align: left;
        color: #9b9b9b;
        font-size: 12px;
        margin-top: 12px;
      }
    }
  }
}
</style>
