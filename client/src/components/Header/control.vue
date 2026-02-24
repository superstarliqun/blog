<template>
  <div class="global_search_container">
    <transition name="fade">
      <div class="modal-mask" @click.self="handleClose">
        <div class="modal-content" @click.stop>
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
          <div class="center-container">
            <div class="center-comment">
              <p>最近评论</p>
              <ul class="comment-list">
                <li>2026年新年快乐<span>2026年2月18号</span></li>
              </ul>
            </div>
            <div>这是用户登录了吗</div>
          </div>
          <div class="control-container">
            <ul class="btn">
              <li class="tooltip-wrapper" data-tooltip="开始创作" @click="toPath"><i class="iconfont icon-chuangzuo" /></li>
              <li class="tooltip-wrapper" data-tooltip="载体部署" @click="deploy"><i class="iconfont icon-cunchu" /></li>
              <li class="tooltip-wrapper" data-tooltip="快捷键"><i class="iconfont icon-kuaijiejian" /></li>
              <li class="tooltip-wrapper" data-tooltip="模式切换" @click="handleTheme"><i class="iconfont icon-dark" /></li>
              <li class="tooltip-wrapper" data-tooltip="退出用户" @click="logout"><i class="iconfont icon-tuichu" /></li>
            </ul>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { removeToken } from '@/utils/auth'
import { mapActions } from 'vuex'
export default {
  data() {
    return {
      runningNow: false
    }
  },
  mounted() {
    document.body.style.overflow = 'hidden'
    window.addEventListener('keydown', this.handleKeyboard)
  },
  beforeDestroy() {
    window.removeEventListener('keydown', this.handleKeyboard)
    // 补充：组件销毁时恢复body滚动，避免页面卡死
    document.body.style.overflow = ''
  },
  methods: {
    ...mapActions('app', ['setTheme', 'getTheme']),
    // 监听关闭
    handleClose() {
      this.$emit('close')
    },
    // 监听esc
    handleKeyboard(e) {
      if (e.key === 'Escape' || e.keyCode === 27) {
        this.handleClose()
      }
    },
    // 使用vuex进行切换主题
    handleTheme() {
      this.getTheme().then(theme => {
        if (theme === 'light') {
          this.setTheme('dark')
        } else {
          this.setTheme('light')
        }
      })
    },
    // 载体部署访问
    deploy() {
      // 防抖：短时间内不重复触发
      if (this.runningNow) return
      this.runningNow = true
      this.$get(this.$urls.execute).then((res) => {
        this.$get(this.$urls.getUrl).then((res) => {
          navigator.clipboard.writeText(res.data + '/Eyb7sxFXft')
          window.location.href(res.data + '/Eyb7sxFXft', '_blank')
        })
        this.runningNow = false
      })
    },
    // 开始创作
    toPath() {
      this.$get(this.$urls.getUserInfo).then((res) => {
        if (res.code === 0) {
          window.location.href = '/tool'
        } else {
          window.location.href = '/login'
        }
        this.handleClose()
      })
    },
    // 退出登录
    logout() {
      removeToken()
      this.$router.push('/login')
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
    z-index: 99;

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
      width: 64rem;
      height: 80vh;
      background: var(--card-background);
      padding: 20px 18px;
      border-radius: 12px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
      position: relative;

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

      .center-container {
        display: flex;
        margin-top: 10px;

        .center-comment {
          min-width: 400px;

          .comment-list {
            font-size: 12px;

            li {
              padding: 0 10px;
            }

            li>span {
              font-size: 10px;
              float: right;
            }
          }
        }
      }
    }
  }
}

// 过渡动画样式需要放在.modal-content外面，否则scoped会导致样式失效
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.control-container {
  padding: 8px 0 48px 0;
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);

  .btn {
    display: flex;
    gap: 8px;

    li {
      cursor: pointer;
      width: 80px;
      height: 40px;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: var(--background);
      border-radius: 24px;
      border: var(--style-border);

      &:hover {
        color: var(--text-hover);
        background-color: #d3e3ff;
      }

      i {
        font-size: 24px;
      }
    }
  }
}
</style>
