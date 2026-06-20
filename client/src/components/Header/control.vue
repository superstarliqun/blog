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
          <div style="display: flex;gap: 20px;margin-top: 12px;">
            <div class="center-container">
              <span class="container-title">RSS订阅消息</span>
              <Transition name="fade">
                <div v-if="list.length > 0" class="rss-content-wrapper">
                  <ul>
                    <li v-for="(item, index) in list" :key="index" @click="handleLink(item)">
                      <span class="rss-time">{{ dayjs(item.pubDate).format('MMM DD, YYYY') }}</span>
                      <div class="rss-title">
                        <div class="rss-title-text">{{ item.title }}</div>
                        <span class="rss-icon" />
                      </div>
                    </li>
                  </ul>
                </div>
                <div v-else class="rss-content-wrapper">
                  <Loading ref="loadingContainer" />
                </div>
              </Transition>
            </div>
            <div class="comment-container">
              <span class="container-title">最新评论</span>
              <Transition name="fade">
                <div v-if="commentList.length > 0" class="rss-content-wrapper">
                  <ul>
                    <li v-for="(item, index) in commentList" :key="index">
                      <span class="rss-time">{{ dayjs(item.pubDate).format('MMM DD, YYYY') }}</span>
                      <div class="rss-title">
                        <div class="rss-title-text">{{ item.comment }}</div>
                        <span class="rss-icon" />
                      </div>
                    </li>
                  </ul>
                </div>
                <div v-else class="rss-content-wrapper">
                  <Loading ref="loadingContainer" />
                </div>
              </Transition>
            </div>
          </div>
          <div class="control-container">
            <span class="container-title">中控操作台</span>
            <ul class="btn">
              <li class="tooltip-wrapper" data-tooltip="开始创作" @click="toPath"><i class="iconfont icon-chuangzuo" />
              </li>
              <li class="tooltip-wrapper" data-tooltip="载体部署" @click="deploy"><i class="iconfont icon-cunchu" /></li>
              <li class="tooltip-wrapper" data-tooltip="快捷键"><i class="iconfont icon-kuaijiejian" /></li>
              <li class="tooltip-wrapper" data-tooltip="模式切换" @click="handleTheme"><i class="iconfont icon-dark" />
              </li>
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
import dayjs from 'dayjs'
import Loading from '@/components/loading/index.vue'
export default {
  components: { Loading },
  data() {
    return {
      dayjs,
      runningNow: false,
      list: [],
      commentList: [
        { nikeName: '张三', time: new Date(), comment: '这个评论可视有点问题呢' }
      ]

    }
  },
  mounted() {
    document.body.style.overflow = 'hidden'
    window.addEventListener('keydown', this.handleKeyboard)
  },
  created() {
    this.requestData()
  },
  beforeDestroy() {
    window.removeEventListener('keydown', this.handleKeyboard)
    document.body.style.overflow = ''
  },
  methods: {
    ...mapActions('app', ['setTheme', 'getTheme']),
    // 查询基础信息
    requestData() {
      this.$get(this.$urls.rss).then((res) => {
        this.list = res.data
      })
    },
    handleLink(item) {
      window.open(item.link, '_blank', 'noopener,noreferrer')
    },
    // 关闭
    handleClose() {
      this.$emit('close')
    },
    // 监听esc退出弹窗
    handleKeyboard(e) {
      if (e.key === 'Escape' || e.keyCode === 27) {
        this.handleClose()
      }
    },
    // 主题模式切换
    handleTheme() {
      this.getTheme().then(theme => {
        if (theme === 'light') {
          this.setTheme('dark')
        } else {
          this.setTheme('light')
        }
      })
    },
    // 载体部署
    deploy() {
      if (this.runningNow) return
      this.runningNow = true
      this.$get(this.$urls.execute).then((res) => {
        this.$get(this.$urls.getUrl).then((res) => {
          navigator.clipboard.writeText(res.data + '/Eyb7sxFXft')
          // 修复：window.location.href 是属性不是方法
          window.open(res.data + '/Eyb7sxFXft', '_blank')
        })
        this.runningNow = false
      })
    },
    // 路径跳转
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

  .modal-mask {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    z-index: 99;
    animation: 0.6s ease 0s 1 normal none running to_show;
    background: rgba(255, 255, 255, 0.1);
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
      background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
      opacity: 0.05;
      pointer-events: none;
    }

    .modal-content {
      min-width: 60rem;
      height: 530px;
      padding: 20px 18px;
      border-radius: 12px;
      background: var(--card-background);
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);

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
              display: none;
              pointer-events: none;
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
        flex: 1;
        font-size: 16px;
        border: var(--style-border);
        margin-top: 24px;
        padding: 20px;
        border-radius: 6px;
        position: relative;
        height: 300px;
        display: flex;
        justify-content: center;
        align-items: center;

        // 关键修改3：添加固定宽度的包装器样式
        .rss-content-wrapper {
          width: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
        }

        .container-title {
          position: absolute;
          top: -14px;
          left: 26px;
          font-weight: 600;
          padding: 0 16px;
          background-color: var(--card-background);
        }

        ul {
          width: 100%;
          display: flex;
          flex-direction: column;
          gap: 8px;

          li {
            display: flex;
            gap: 20px;
            border-radius: 16px;
            padding: 4px 8px;
            cursor: pointer;

            .rss-time {
              font-size: 12px;
              display: flex;
              align-items: center;
              color: var(--text-color2);
            }

            .rss-title {
              flex: 1;
              display: flex;
              justify-content: space-between;

              .rss-icon {
                position: relative;
                transition: opacity 0.35 ease-in-out;
              }

              .rss-icon::after {
                font-family: 'iconfont';
                content: "\e756";
                position: absolute;
                top: 0;
                left: -17px;
                color: #333;
                opacity: 1;
              }

              .rss-title-text {
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                padding-right: 20px;
                width: 390px;
              }
            }

            &:hover {
              background-color: var(--text-hover);

              .rss-title>.rss-icon::after {
                font-family: 'iconfont';
                content: "\e62b";
              }

              .rss-title>.rss-title-text {
                color: #517E94;
              }
            }
          }
        }
      }

      .comment-container {
        flex: 1;
        font-size: 16px;
        height: 300px;
        border: var(--style-border);
        margin-top: 24px;
        padding: 20px;
        border-radius: 6px;
        position: relative;

        .container-title {
          position: absolute;
          top: -14px;
          left: 26px;
          font-weight: 600;
          padding: 0 16px;
          background-color: var(--card-background);
        }

        ul {
          display: flex;
          flex-direction: column;
          gap: 2px;

          li {
            display: flex;
            gap: 10px;
          }
        }
      }
    }
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.control-container {
  margin-top: 50px;
  padding: 20px;
  border-radius: 6px;
  border: var(--style-border);
  position: relative;

  .btn {
    display: flex;
    gap: 8px;
    justify-content: center;

    li {
      cursor: pointer;
      width: 80px;
      height: 40px;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: var(--card-background);
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

  .container-title {
    position: absolute;
    top: -14px;
    left: 26px;
    font-weight: 600;
    padding: 0 16px;
    background-color: var(--card-background);
    font-size: 16px;
  }
}

.tooltip-wrapper::after {
  content: attr(data-tooltip);
  position: absolute;
  opacity: 0;
  visibility: hidden;
  isolation: isolate;
  z-index: 9999;
  transition: all 0.2s ease;
  pointer-events: none;
  border: var(--style-border);
  white-space: nowrap;
  display: inline-block;
  padding: 4px 8px;
  background: var(--background);
  color: var(--text-color);
  font-size: 12px;
  border-radius: 8px;
  left: 50%;
  top: -100%;
  margin-top: unset;
  transform: translateX(-50%);
}

// 补充：让tooltip hover时显示
.tooltip-wrapper:hover::after {
  opacity: 1;
  visibility: visible;
}
</style>
