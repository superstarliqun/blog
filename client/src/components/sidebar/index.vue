<template>
  <div id="rightside" :style="rightSideStyle">
    <div>
      <div class="rightside-button" title="主页" @click="handleButton(0)">
        <i class="iconfont icon-zhuye" />
      </div>
      <div class="rightside-button" title="设置" @click="handleButton(1)">
        <i class="iconfont icon-shezhi" />
      </div>
      <div class="rightside-button" title="刷新" @click="handleButton(2)">
        <i class="iconfont icon-shuaxin" />
      </div>
      <div class="rightside-button" title="回到顶部" @click="handleButton(3)">
        <i class="iconfont icon-zhiding" />
      </div>
    </div>
    <Setting ref="settingRef" />
  </div>
</template>

<script>
import Setting from '../setting/index.vue'
export default {
  components: { Setting },
  data() {
    return {
      scrollPercentage: 0,
      show: false,
      rightSideStyle: {
        opacity: 0,
        transform: 'translateX(0)'
      }
    }
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    // 检测滑动
    handleScroll() {
      // 当滚动事件触发时，执行相关操作
      const scrollTop = document.documentElement.scrollTop
      const scrollHeight =
        document.documentElement.scrollHeight -
        document.documentElement.clientHeight
      const percentage = (scrollTop / scrollHeight) * 100
      this.scrollPercentage = Math.min(Math.floor(percentage), 100) // 取整并防止超过100%
      if (this.scrollPercentage > 0) {
        this.show = true
        this.rightSideStyle = {
          opacity: 1,
          transform: 'translateX(-58px)'
        }
      } else {
        this.show = false
        this.rightSideStyle = {
          opacity: 0,
          transform: 'translateX(0)'
        }
      }
    },
    // 按钮点击执行方法
    handleButton(num) {
      if (num === 0) {
        window.scrollTo({ top: 0, behavior: 'smooth' })
        this.$router.push('/')
      } else if (num === 1) {
        this.$refs.settingRef.dialogVisible = true
      } else if (num === 2) {
        window.scrollTo({ top: 0, behavior: 'smooth' })
        setTimeout(() => {
          window.location.reload()
        }, 500)
      } else if (num === 3) {
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }
    }
  }
}
</script>
<style>
/* 右下角设置按钮间距 */
#rightside {
  position: fixed;
  right: -48px;
  bottom: 100px;
  z-index: 100;
  opacity: 0;
  filter: alpha(opacity=0);
  transition: all 0.5s;
}

#rightside.show {
  transform: translateX(-58px);
}

.rightside-button {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 5px;
  width: 35px;
  height: 35px;
  background-color: var(--theme-color2);
  color: #ffffff;
  line-height: 35px;
  border-radius: 5px;
  cursor: pointer;
}

.rightside-button>i {
  font-size: 20px;
}

.rightside-button:hover {
  background-color: #66b1ff;
}

@media only screen and (max-width: 768px) {
  #rightside {
    display: none;
  }
}
</style>
