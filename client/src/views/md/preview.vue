<template>
  <super-container>
    <!-- 滚动时固定在顶部的标题栏 -->
    <transition name="slide-down">
      <div v-show="stickyShow" class="sticky-title-bar" :class="{ 'is-shown': stickyShow }">
        <span class="sticky-title">{{ formData.title }}</span>
      </div>
    </transition>
    <super-main>
      <super-content>
        <super-sidebar-left>
          <super-post-header>
            <div class="header-container">
              <h1 class="page-title">{{ formData.title }}</h1>
              <div class="meta-info-item">
                <span class="meta-info">作者：{{ formData.createBy }}</span>
                <span class="meta-info">发布：{{ formData.createTime | formatDate }}</span>
                <span class="meta-info">{{ countWords() }} 字</span>
                <span class="meta-info">分类：{{ formData.categoryName }}</span>
                <span class="meta-info">标签：{{ formData.tagsList }}</span>
                <span class="meta-info" style="cursor: pointer;" @click="fixedScroll('comment_box')">{{ comment }} 条评论</span>
                <span v-if="showEdit" class="meta-info" style="cursor: pointer;" @click="toEdit(formData.id)">编辑</span>
              </div>
            </div>
          </super-post-header>
          <super-divider />
          <v-md-preview ref="preview" :text="formData.content" class="poster-content" @image-click="imgClick" @copy-code-success="handleCopyCodeSuccess" />
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
                   }" @click="goDirectory(link)" v-html="link.text"
              />
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
// import CommentDialog from './component/comment.vue'
import Copyright from './component/copyright'
import dayjs from 'dayjs'

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
  metaInfo() {
    // 优先用异步获取的标题，无数据时用默认值
    const pageTitle = this.formData.title ? `${this.formData.title} - 灵光一刻` : '灵光一刻 - 技术博客'
    // 可以根据 formData 动态生成 description/keywords（比如从文章摘要/标签生成）
    const pageDesc = this.formData.summary || '专注分享Java后端、前端开发、系统运维、面试经验等技术干货...'
    const pageKeywords = this.formData.tagsList ? this.formData.tagsList.join(',') : 'Java,Vue,后端开发,面试总结'

    return {
      title: pageTitle,
      meta: [
        { name: 'description', content: pageDesc },
        { name: 'keywords', content: pageKeywords },
        // 可选：补充 og 标签，优化社交分享
        { property: 'title', content: this.formData.title || '灵光一刻' },
        { property: 'description', content: pageDesc }
      ]
    }
  },
  filters: {
    formatDate(value) {
      if (!value) return ''
      return dayjs(value).format('YYYY年MM月DD日')
    }
  },
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
      isReady: false,
      stickyShow: false,
      headerHeight: 0
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
    this.isReady = true
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy() {
    // 组件销毁前必须移除监听，否则会导致性能问题
    window.removeEventListener('scroll', this.handleScroll)
    if (this.observer) {
      this.observer.disconnect()
    }
  },
  methods: {
    // onPageLoaded disabled - no longer needed
    onPageLoaded() { },
    scrollActiveTocIntoView() {
      this.$nextTick(() => {
        const activeEl = this.$el.querySelector('.toc-list-container.active')
        const tocList = this.$el.querySelector('.toc-list')
        if (!activeEl || !tocList) return
        // Only scroll the toc-list container, never the main page
        const itemTop = activeEl.offsetTop - tocList.offsetTop
        const targetTop = itemTop - tocList.clientHeight / 2 + activeEl.clientHeight / 2
        tocList.scrollTop = Math.max(0, targetTop)
      })
    },
    initData(id) {
      this.getDetails()
      this.$nextTick(() => {
        // Scroll handling moved to handleScroll
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
        // 记录 super-post-header 高度，用于判断滚动阈值
        const header = this.$el.querySelector('super-post-header')
        if (header) {
          this.headerHeight = header.getBoundingClientRect().height
        }
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
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      this.stickyShow = scrollTop > this.headerHeight
    },
    // 回复文章滚动距离
    // 注释掉 restoreScroll 避免滚动回跳
    restoreScroll() {
      // 已禁用 - 自动滚动恢复导致页面滚动异常
      return
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
<style lang="scss" scoped>
super-post-header {
  max-width: 100%;
  margin: 0 auto;
  display: block;
  .page-title {
    margin: 20px 0;
  }

  .meta-info-item {
    display: flex;
    gap: 8px;
    .meta-info {
      font-size: 10px;
      border: var(--style-border);
      padding: 2px 8px;
      border-radius: 12px;
      background-color: var(--card-hover);
    }
  }
}
super-divider{
  display: block;
  width: 100%;
  height: 0;
  border-top: var(--style-border);
  margin: 32px 0;
}

// 固定在顶部的标题栏
.sticky-title-bar {
  position: fixed;
  top: 4px;
  left: 0;
  width: 100%;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;

  &.is-shown {
    transform: translateY(0);
  }

  .sticky-title {
    font-size: .9rem;
    text-align: center;
    cursor: pointer;
    border: var(--style-border);
    padding: 2px 12px;
    border-radius: 18px;
    backdrop-filter: blur(20px) saturate(180%);
    -webkit-backdrop-filter: blur(20px) saturate(180%); /* Safari / iOS 必须加 */

    /* 2. 半透底色，区分深浅模式 */
    background: rgba(255, 255, 255, 0.12);

    /* 3. 模拟玻璃菲涅尔边缘高光（液态关键） */
    border: 1px solid rgba(255, 255, 255, 0.25);
    box-shadow:
      inset 0 0 12px rgba(255,255,255,0.08), /* 内发光模拟曲面反光 */
      0 8px 32px rgba(0, 0, 0, 0.15);       /* 悬浮软阴影 */
    }
}

// 过渡动画
.slide-down-enter-active,
.slide-leave-active {
  transition: opacity 0.2s ease;
}

.slide-down-enter,
.slide-leave-to {
  opacity: 0;
}

// ---- Global layout ----
super-container {
  display: block;
  min-height: 100vh;
  background: var(--background);
}

// ---- Main layout: flex row ----
super-main {
  display: block;
  background: var(--background);
  min-height: 60vh;
}

super-content {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  gap: 24px;
  padding: 20px 20px 60px;
  align-items: flex-start;
}

// ---- Left: article content ----
super-sidebar-left {
  flex: 1;

  .poster-content {
    // background: var(--card-background);
    // border-radius: 12px;
    // padding: 10px 48px;
    // border: var(--style-border);
    // box-shadow: var(--box-shadow);
    // overflow-anchor: auto;
  }
}

// ---- Right: TOC sidebar ----
super-sidebar-right {
  width: 300px;
  flex-shrink: 0;
  align-self: flex-start;
  position: sticky;
  top: 24px;
  z-index: 10;

  .card-shadow {
    background: var(--card-background);
    border: var(--style-border);
    border-radius: 12px;
    box-shadow: var(--box-shadow);
    padding: 8px 0;

    .toc-title-container {
      padding: 0 20px 6px;
      border-bottom: var(--style-border);

      .decoration-bar {
        font-size: 15px;
        font-weight: 600;
        color: var(--text-color);
        display: flex;
        align-items: center;
        gap: 6px;

        .icon-mulu {
          color: var(--text-hover);
          font-size: 16px;
        }
      }
    }

    .toc-list {
      max-height: 60vh;
      overflow-y: auto;
      padding: 8px 0;

      &::-webkit-scrollbar {
        width: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: var(--text-color3);
        border-radius: 4px;
      }

      &::-webkit-scrollbar-track {
        background: transparent;
      }

      .toc-list-container {
        padding: 8px 20px;
        font-size: 14px;
        line-height: 1.5;
        color: var(--text-color3);
        cursor: pointer;
        transition: all 0.15s ease;
        border-left: 3px solid transparent;
        word-break: break-word;

        &:hover {
          color: var(--text-color);
          background: var(--tag-background);
          border-left-color: var(--text-hover);
        }

        &.active {
          color: var(--text-hover);
          font-weight: 600;
          border-left-color: var(--text-hover);
          background: var(--tag-background);
        }
      }
    }
  }
}

// ---- Article content typography overrides ----
::v-deep .v-md-editor-preview {
  font-size: 16px;
  line-height: 1.75;
  color: var(--text-color);

  .markdown-body {

    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
      margin: 1.8em 0 0.6em;
      font-weight: 600;
      line-height: 1.35;
      color: var(--text-color);
    }

    h1 {
      font-size: 1.85em;
    }

    h2 {
      font-size: 1.5em;
      border-bottom: 1px solid var(--article-border-bottom);
      padding-bottom: 0.3em;
    }

    h3 {
      font-size: 1.25em;
    }

    h4 {
      font-size: 1.1em;
    }

    p {
      margin: 1em 0;
      line-height: 1.75;
      color: var(--text-color);
    }

    ul,
    ol {
      margin: 0.8em 0;
      padding-left: 1.8em;
    }

    li {
      margin: 0.3em 0;
      line-height: 1.7;
    }

    blockquote {
      margin: 1.2em 0;
      padding: 10px 22px;
      border-left: 4px solid var(--text-hover);
      background: var(--tag-background);
      border-radius: 0 8px 8px 0;
      color: var(--text-color2);

      p {
        color: var(--text-color2);
        margin: 0.5em 0;
      }
    }

    code {
      font-size: 0.88em;
      padding: 0.2em 0.4em;
      border-radius: 4px;
      background: var(--tag-background);
      border: 1px solid var(--tag-border);
      color: var(--text-color);
      font-family: "Fira Code", "Cascadia Code", "JetBrains Mono", Consolas, monospace;
    }

    pre {
      background: var(--code-background) !important;
      border-radius: 8px;
      padding: 20px 24px;
      margin: 1.2em 0;
      overflow-x: auto;
      border: 1px solid rgba(255, 255, 255, 0.06);

      code {
        background: none !important;
        border: none !important;
        padding: 0;
        font-size: 14px;
        line-height: 1.6;
        color: #ccc;
      }
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin: 1em 0;
      font-size: 14px;

      th,
      td {
        padding: 8px 12px;
        border: 1px solid var(--style-border);
      }

      th {
        background: var(--tag-background);
        font-weight: 600;
        color: var(--text-color);
      }

      td {
        color: var(--text-color2);
      }

      tr:nth-child(even) td {
        background: var(--tag-background);
      }
    }

    a {
      color: var(--text-hover);
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }

    img {
      max-width: 100%;
      border-radius: 8px;
      margin: 1em 0;
    }

    hr {
      margin: 2em 0;
      border: none;
      border-top: var(--style-border);
    }
  }
}

// ---- Decorative divider ----
.jianzi {
  border: var(--style-border) !important;
  border-style: dashed !important;
  position: relative;
  margin: 40px auto;
  pointer-events: none;
  user-select: none;
}

.jianzi::before {
  position: absolute;
  top: -10px;
  left: 5%;
  z-index: 1;
  color: var(--text-color3);
  font-family: "iconfont";
  content: "\\e6ba";
  font-size: 20px;
  line-height: 1;
}

// ---- Image modal ----
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.85);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 11111;
  cursor: pointer;

  .modal-content {
    max-width: 90%;
    max-height: 90%;
    border-radius: 8px;
  }
}

// ---- Mobile ----
@media only screen and (max-width: 768px) {

  super-content {
    flex-direction: column;
    padding: 16px 0 40px;
    gap: 12px;
  }

  super-sidebar-left {
    .poster-content {
      padding: 20px;
      border-radius: 0;
    }
  }

  super-sidebar-right {
    width: 100%;
    padding: 0 12px;
    position: static;
  }
}

// ========================================
// 移动端适配
// ========================================
@media only screen and (max-width: 768px) {

}
</style>
