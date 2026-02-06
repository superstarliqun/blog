<template>
  <super-container>
    <super-post-header>
      <div class="header-content">
        <div class="post-first-info">
          <span>原创</span>
          <span v-if="formData.categoryName" class="cat"># {{ formData.categoryName }}</span>
          <span v-for="tag in formData.tagsList" :key="tag" class="tag"><i class="iconfont icon-icon-biaoqian" />{{ tag
            }}</span>
        </div>
        <h1 class="post-title">{{ formData.title }}</h1>
        <div class="post-mate">
          <span><i class="iconfont icon-Word" />{{ countWords() }}</span>
          <span><i class="iconfont icon-daohang_daohanglan_xungeng" />阅读{{
            Math.floor(countWords() / 500)
            }}分钟</span>
          <span v-if="formData.updateTime != undefined"><i class="iconfont icon-riqi" />{{
            formData.createTime.slice(0, 10)
            }}</span>
          <span @click="fixedScroll('comment_box')"><i class="iconfont icon-pinglun1" />{{ comment }}</span>
          <span v-if="showEdit" class="edit-button" @click="toEdit(formData.id)"><i
              class="iconfont icon-bianji" /></span>
        </div>
      </div>
    </super-post-header>
    <div class="post-radius-bottom" />
    <super-main>
      <super-content>
        <super-sidebar-left>

          <v-md-preview ref="preview" :text="formData.content" class="poster-content" preview-class="poster-content"
            @image-click="imgClick" @copy-code-success="handleCopyCodeSuccess" />
          <Copyright />
          <div class="jianzi" />
          <CommentBox id="comment_box" @call-back="handleCommentNumber" />
        </super-sidebar-left>
        <super-sidebar-right>
          <div class="sticky-box card-shadow">
            <div class="toc-title-container">
              <span class="decoration-bar">
                <i class="iconfont icon-mulu" />目录
              </span>
            </div>
            <div class="toc-list">
              <div v-for="(link, index) in tocList" :key="link.id" class="toc-list-container"
                :class="link.line == mdLine ? 'active' : ''" :style="{
                  'margin-left': link.level * 20 + 'px',
                  color: index === highlight ? '#409eff' : ''
                }" @click="goDirectory(link)" v-html="link.text" />
            </div>
          </div>
        </super-sidebar-right>
      </super-content>
    </super-main>
    <div v-if="isModalOpen" class="modal" @click="closeModal">
      <img :src="selectedImage" class="modal-content">
    </div>
  </super-container>
</template>

<script>
import Vue from 'vue'
import VMdPreview from '@kangc/v-md-editor/lib/preview'
import '@kangc/v-md-editor/lib/style/preview.css'

import creatPrismTheme from '@kangc/v-md-editor/lib/theme/prism'
// 代码高亮主题
import Prism from 'prismjs'
import 'prismjs/components/prism-java'
import 'prismjs/components/prism-yaml'
import 'prismjs/components/prism-bash'
import 'prismjs/components/prism-sql'
import 'prismjs/components/prism-json'
import 'prismjs/components/prism-css'
import 'prismjs/components/prism-cshtml'
import 'prismjs/components/prism-python'
import './css/index.scss'
// 快捷复制代码
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index'
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css'
// 内容定位
import createAlignPlugin from '@kangc/v-md-editor/lib/plugins/align'
// 任务列表
import createTodoListPlugin from '@kangc/v-md-editor/lib/plugins/todo-list/index'
import '@kangc/v-md-editor/lib/plugins/todo-list/todo-list.css'
import CommentBox from '@/components/comment/index.vue'
import Copyright from './component/copyright'

const prismTheme = creatPrismTheme({
  Prism,
  config: {
    toc: {
      includeLevel: [1, 2, 3]
    }
  }
})
VMdPreview.theme(prismTheme)
VMdPreview.use(createCopyCodePlugin())
VMdPreview.use(createAlignPlugin())
VMdPreview.use(createTodoListPlugin())

Vue.use(VMdPreview)
import { getUserInfo } from '@/utils/auth'
export default {
  components: { CommentBox, Copyright },
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      formData: {},
      directoryShow: true,
      scrollAmount: 0,
      showEdit: false,
      screenHeight: 0,
      tocList: [],
      directoryWidth: 0,
      selectedImage: '',
      isModalOpen: false,
      comment: 0,
      // 滚动百分比
      scrollPercent: 0,
      observer: null,
      mdLine: '', // 当前高亮的目录 ID
      isReady: false
    }
  },
  watch: {
    $route(to, from) {
      if (to.params.id !== from.params.id) {
        this.initData(to.params.id)
      }
    }
  },
  mounted() {
    this.initData(this.$route.params.id)
    // 页面所有组件加载完毕
    window.addEventListener('load', this.onPageLoaded)
  },
  beforeDestroy() {
    // 组件销毁前必须移除监听，否则会导致性能问题
    window.removeEventListener('scroll', this.handleScroll)
    window.removeEventListener('load', this.onPageLoaded)
    if (this.observer) {
      this.observer.disconnect()
    }
  },
  methods: {
    onPageLoaded() {
      console.log('页面所有资源加载完成')
      this.isReady = true
    },
    scrollActiveTocIntoView() {
      this.$nextTick(() => {
        const activeEl = this.$el.querySelector('.toc-list-container.active')
        if (activeEl) {
          // 自动滚动，避免改变整体布局
          activeEl.scrollIntoView({
            block: 'nearest', // 保持在可视区域，最小滚动
            inline: 'nearest'
          })
        }
      })
    },
    initData(id) {
      this.getDetails()
      this.$nextTick(() => {
        window.addEventListener('scroll', this.handleScroll)
        if (this.isReady) {
          this.restoreScroll()
        }
      })
    },
    // 获取详情信息
    async getDetails() {
      await this.$get(this.$urls.articleGet, null, [this.$route.params.id])
        .then((result) => {
          this.formData = result.data
          if (this.formData.content) {
            this.formData.content = this.transformMarkdownHeaders(
              this.formData.content
            )
          }
          const userInfo = getUserInfo()
          if (userInfo.id === this.formData.authorId) {
            this.showEdit = true
          }
          this.timer = new Date().getTime()
        })
        .catch(() => {
          console.log('错误请求!')
        })
      this.$nextTick(() => {
        this.handleReady()
      })
    },
    // 监听子组件传递的数量
    handleCommentNumber(index) {
      this.comment = index
    },
    fixedScroll(keywords) {
      const element = document.getElementById(keywords)
      if (element) {
        element.scrollIntoView({
          // behavior: 'smooth',
          block: 'start'
        })
      }
    },
    initObserver() {
      this.observer = new IntersectionObserver(
        (entries) => {
          entries.forEach((entry) => {
            if (entry.isIntersecting) {
              this.mdLine = entry.target.getAttribute('data-v-md-line')
              // 保证选中目录可见
              this.scrollActiveTocIntoView()
            } else if (entry.boundingClientRect.top > 130) {
              this.activatePreviousHeading(entry.target)
              this.scrollActiveTocIntoView()
            }
          })
        },
        {
          rootMargin: '-130px 0px -86% 0px',
          threshold: 0
        }
      )
      const headings = this.$el.querySelectorAll(
        '.poster-content h2, .poster-content h3, .poster-content h4'
      )
      headings.forEach((h) => this.observer.observe(h))
    },
    // 辅助方法：激活上一个标题
    activatePreviousHeading(currentElement) {
      const headings = Array.from(
        this.$el.querySelectorAll(
          '.poster-content h2, .poster-content h3, .poster-content h4'
        )
      )
      const index = headings.indexOf(currentElement)
      if (index > 0) {
        const prev = headings[index - 1]
        this.activeId = prev.id
        this.mdLine = prev.getAttribute('data-v-md-line')
      }
    },
    // 利用正则将h标签降级
    transformMarkdownHeaders(content) {
      if (!content) return ''
      // 正则解释：
      // ^(#+)  -> 匹配行首的一个或多个 #
      // (?=\s) -> 必须紧跟一个空格 (标准的 Markdown 语法)
      // /gm    -> 全局多行匹配
      return content.replace(/^(#+)(?=\s)/gm, (match) => {
        // 只有当层级小于 6 时才增加 #，防止溢出
        return match.length < 6 ? match + '#' : match
      })
    },
    // 点击图片执行的方法
    imgClick(row, index) {
      this.selectedImage = row[index]
      this.isModalOpen = true
    },
    // 关闭预览图片
    closeModal() {
      this.isModalOpen = false
    },
    // 通过dom元素获取h1-6的标签元素
    handleReady() {
      // 获取类名为 markdown-body 的元素
      const markdownBody = document.querySelector('.poster-content')
      // 获取所有 h1 到 h6 的标签
      const headings = []
      for (let i = 1; i <= 6; i++) {
        const headingTags = markdownBody.querySelectorAll(`h${i}`)
        headings.push(...headingTags)
      }
      const arraySort = Array.from(headings).sort(function (a, b) {
        var dataA = a.getAttribute('data-v-md-line')
        var dataB = b.getAttribute('data-v-md-line')
        var numericA = parseInt(dataA, 10)
        var numericB = parseInt(dataB, 10)
        return numericA - numericB
      })

      let tempHeadings = Array.from(arraySort).map((heading) => ({
        tagName: heading.tagName,
        text: heading.textContent.trim(),
        level: parseInt(heading.tagName[1]),
        line: heading.attributes[1].value
      }))

      // 2. 计算最小层级 (如果只有 H2, H3，那么 minLevel 就是 2)
      if (tempHeadings.length > 0) {
        const minLevel = Math.min(...tempHeadings.map((h) => h.level))

        // 3. 重新校准 level，让最顶层从 0 或 1 开始，避免目录右移
        tempHeadings = tempHeadings.map((h) => ({
          ...h,
          // 如果 minLevel 是 2，当前是 2，那么 2-2 = 0。
          // 你的 template 里是用 padding-left: level * 20 + 'px'
          // 以前 H1 是 level 1 (20px)，现在 H2 是 level 2 (40px)
          // 我们希望它保持 20px 或 0px，这里演示归零处理，你可以根据喜好 +1
          level: h.level - minLevel
        }))
      }

      this.tocList = tempHeadings
      this.initObserver()
    },
    // 通过指定锚点跳转
    goDirectory(row) {
      const section = document.querySelector(
        '[data-v-md-line="' + row.line + '"]'
      )
      if (section) {
        section.scrollIntoView({ behavior: 'smooth' })
      }
    },
    // 代码复制方法
    handleCopyCodeSuccess(code) {
      this.$message({
        message: '复制成功!',
        type: 'success'
      })
    },
    // 跳转编辑页面
    toEdit(id) {
      this.$get(this.$urls.getUserInfo).then((res) => {
        if (res.code === 0) {
          this.$router.push({
            name: 'tool',
            params: { id: id }
          })
        }
      })
    },
    highlight() {
    },
    countWords() {
      // 去掉所有空行、换行符和符号
      const content = this.formData.content || ''
      const cleanText = content
        .replace(/[\n\r]/g, ' ') // 替换换行符为空格
        .replace(/[^\w\u4e00-\u9fa5]/g, '') // 去掉符号和非汉字字符
      // 统计字数
      return cleanText.length
    },
    // 监听滚动
    handleScroll() {
      if (this.isReady) {
        const CACHE_KEY = 'scroll_snapshots_cache'
        let cacheData = {}
        try {
          const localData = localStorage.getItem(CACHE_KEY)
          cacheData = localData ? JSON.parse(localData) : {}
        } catch (e) {
          console.error('解析缓存失败', e)
          cacheData = {}
        }

        // 计算滚动百分比（此时已修复返回数字类型）
        this.scrollPercent = this.screenHeightFunction()
        const articleId = this.$route.params.id

        // 只更新当前文章的缓存，不覆盖整个缓存对象（关键修复）
        cacheData[articleId] = {
          percent: this.scrollPercent,
          updateTime: new Date().getTime(),
          title: this.formData.title
        }

        // 整体序列化存储缓存对象（唯一正确的存储方式）
        localStorage.setItem(CACHE_KEY, JSON.stringify(cacheData))
      }
    },
    // 回复文章滚动距离
    restoreScroll() {
      const CACHE_KEY = 'scroll_snapshots_cache'
      const articleId = this.$route.params.id
      const localData = localStorage.getItem(CACHE_KEY)
      if (!localData) return

      let cacheData = {}
      try {
        cacheData = JSON.parse(localData)
      } catch (e) {
        console.error('解析滚动缓存失败', e)
        return
      }
      const responseResult = cacheData[articleId]
      if (
        !responseResult ||
        responseResult.percent <= 0 ||
        responseResult.percent > 100
      ) {
        return
      }

      // 轮询配置：检测间隔、超时时间（避免无限轮询）
      const checkInterval = 100 // 每100ms检测一次（可调整）
      const timeout = 5000 // 5秒后超时停止（避免异常情况）
      let lastScrollHeight = 0 // 上一次的页面总高度
      let checkTimer = null // 轮询定时器
      let timeoutTimer = null // 超时定时器

      // 核心：检测页面高度是否稳定
      const checkScrollHeightStable = () => {
        const currentScrollHeight = document.documentElement.scrollHeight
        // 判断条件：当前高度 > 0 且 和上一次高度一致（说明高度稳定，内容加载完成）
        if (
          currentScrollHeight > 0 &&
          currentScrollHeight === lastScrollHeight
        ) {
          // 执行滚动恢复
          const clientHeight = document.documentElement.clientHeight
          const scrollTop =
            (currentScrollHeight - clientHeight) *
            (responseResult.percent / 100)
          window.scrollTo({ top: scrollTop, behavior: 'auto' })
          // 清除定时器，停止轮询
          clearInterval(checkTimer)
          clearTimeout(timeoutTimer)
        } else {
          // 高度不稳定，更新lastScrollHeight，继续轮询
          lastScrollHeight = currentScrollHeight
        }
      }

      // 初始化轮询
      lastScrollHeight = document.documentElement.scrollHeight
      checkTimer = setInterval(checkScrollHeightStable, checkInterval)
      // 设置超时保护
      timeoutTimer = setTimeout(() => {
        clearInterval(checkTimer)
        // 超时后仍尝试执行一次滚动恢复（兜底）
        const scrollHeight = document.documentElement.scrollHeight
        const clientHeight = document.documentElement.clientHeight
        const scrollTop =
          (scrollHeight - clientHeight) * (responseResult.percent / 100)
        window.scrollTo({ top: scrollTop, behavior: 'auto' })
      }, timeout)
    },
    screenHeightFunction(scrollTop) {
      if (scrollTop == null) {
        scrollTop =
          window.pageYOffset ||
          document.documentElement.scrollTop ||
          document.body.scrollTop
      }
      const scrollHeight = document.documentElement.scrollHeight
      const clientHeight = document.documentElement.clientHeight

      if (scrollHeight > clientHeight) {
        const percent = (scrollTop / (scrollHeight - clientHeight)) * 100
        // 修复：先限制范围，再转为数字类型（避免字符串返回）
        const validPercent = Math.min(100, Math.max(0, percent))
        return Number(validPercent.toFixed(2)) // 转为数字类型
      }
      return 0
    }
  }
}
</script>
